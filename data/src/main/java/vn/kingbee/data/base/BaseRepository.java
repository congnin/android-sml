package vn.kingbee.data.base;

import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Function;
import okhttp3.Headers;
import okhttp3.ResponseBody;
import retrofit2.Response;
import timber.log.Timber;
import vn.kingbee.data.base.model.ErrorModel;
import vn.kingbee.domain.ApiCode;
import vn.kingbee.domain.core.exception.BaseException;
import vn.kingbee.domain.core.exception.BusinessErrorException;
import vn.kingbee.domain.core.exception.NetworkException;
import vn.kingbee.domain.core.exception.RetrofitException;
import vn.kingbee.domain.core.exception.SessionTokenExpiredException;
import vn.kingbee.domain.core.exception.TechnicalErrorException;
import vn.kingbee.domain.core.exception.UnknownException;
import vn.kingbee.domain.enums.HttpStatus;

public class BaseRepository {
    private static final String MESSAGE = "message: ";
    private static final String TO_STRING_METHOD = "toString: ";

    public <T> Observable<T> processRequest(@NonNull final Observable<T> observableRequest) {
        return observableRequest.onErrorResumeNext((Function<Throwable, ObservableSource<? extends T>>) this::getObservableOnErrorV2);
    }

    private <T> Observable<T> getObservableOnErrorV2(final Throwable throwable) {
        return Observable.create(subscriber -> {
            try {

                if (throwable != null && (throwable.getCause() instanceof NetworkException ||
                        throwable.getCause() instanceof SessionTokenExpiredException)) {
                    subscriber.onError(throwable.getCause());
                    return;
                }
                Timber.tag(ApiCode.TAG).d("handling throwable of retrofit");
                Timber.tag(ApiCode.TAG).d(MESSAGE + (throwable != null ? throwable.getMessage() : null) + " - " + TO_STRING_METHOD + (throwable != null ? throwable.toString() : null));
                RetrofitException retrofitException = RetrofitException.fromThrowable(throwable);
                if (retrofitException.getKind() == RetrofitException.Kind.HTTP) {
                    Timber.tag(ApiCode.TAG).d("retrofitException is HTTP");
                    Response response = retrofitException.getResponse();
                    Timber.tag(ApiCode.TAG).d("response: %s", response.toString());
                    HttpStatus httpStatus = HttpStatus.valueOf(String.valueOf(response.code()));
                    ErrorModel errorModel = null;
                    if (response.errorBody() != null) {
                        try {
                            errorModel = new Gson().fromJson(BaseRepository.this.getStringFromResponseBody(response.errorBody()), ErrorModel.class);
                        } catch (JsonSyntaxException e) {
                            Timber.tag(ApiCode.TAG).d("not json model or error model");
                        }
                    }
                    // Sometime response.errorBody() non null but empty -> errorModel is parse to null
                    errorModel = errorModel == null ? new ErrorModel() : errorModel;
                    // set http code value
                    errorModel.setHttpCode(response.code());
                    Timber.tag(ApiCode.TAG).d("response: %s", response.toString());
                    Timber.tag(ApiCode.TAG).d("ErrorModel: %s", new Gson().toJson(errorModel));

                    // analyze exception
                    BaseException exception = new UnknownException();
                    if (BaseRepository.this.isBusinessException(response, httpStatus)) {
                        exception = new BusinessErrorException();
                        Timber.tag(ApiCode.TAG).d("retrofitException is BUSINESS exception");
                    } else if (BaseRepository.this.isTechnicalException(httpStatus)) {
                        exception = new TechnicalErrorException();
                        Timber.tag(ApiCode.TAG).d("retrofitException is TECHNICAL exception");
                    } else if (BaseRepository.this.isRequestTimeoutException(httpStatus)) {
                        exception = new NetworkException();
                        Timber.tag(ApiCode.TAG).d("retrofitException is REQUEST TIMEOUT exception");
                    } else if (BaseRepository.this.isLoginLogoutException(httpStatus)) {
                        Timber.tag(ApiCode.TAG).d("retrofitException is LOGIN/LOGOUT exception");
                        return;
                    }
                    BaseRepository.this.handleDefaultException(subscriber, errorModel, exception, response.headers());
                } else if (retrofitException.getKind() == RetrofitException.Kind.NETWORK) {
                    Timber.tag(ApiCode.TAG).d("retrofitException is NETWORK exception");
                    NetworkException networkException = new NetworkException();
                    networkException.setMessage(retrofitException.getMessage());
                    subscriber.onError(networkException);
                } else {
                    Timber.tag(ApiCode.TAG).d("retrofitException is UNEXPECTED exception");
                    subscriber.onError(throwable);
                }
            } catch (Exception e) {
                Timber.tag(ApiCode.TAG).d("get exception when handling throwable in getObservableOnErrorV2 method");
                Timber.tag(ApiCode.TAG).d(MESSAGE + e.getMessage() + " - " + TO_STRING_METHOD + e.toString());
                subscriber.onError(throwable);
            }
        });
    }

    private <T> void handleDefaultException(
            ObservableEmitter<T> subscriber,
            ErrorModel errorModel,
            BaseException exception,
            Headers headers) {
        exception.setHttpCode(errorModel.getHttpCode());
        exception.setHeaders(headers);
        if (errorModel.getErrorCode() != null) {
            exception.setCode(errorModel.getErrorCode());
            exception.setMessage(errorModel.getErrorMessage());
        } else if (errorModel.getErrorList() != null && errorModel.getErrorList().size() > 0) {
            exception.setCode(errorModel.getErrorList().get(0).getErrorCode());
            exception.setMessage(errorModel.getErrorList().get(0).getErrorMessage());
        }
        Timber.tag(ApiCode.TAG).d("BaseException: %s", new Gson().toJson(exception));
        subscriber.onError(exception);
    }

    private String getStringFromResponseBody(ResponseBody responseBody) {
        BufferedReader reader = null;
        StringBuilder sb = new StringBuilder();
        try {
            reader = new BufferedReader(new InputStreamReader(responseBody.byteStream(), StandardCharsets.UTF_8));
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
        } catch (java.io.IOException e) {
            Timber.e(e, "Get string error");
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (java.io.IOException e) {
                    Timber.e(e, "Get string error");
                }
            }
        }
        return sb.toString();
    }

    private boolean isLoginLogoutException(HttpStatus httpStatus) {
        return httpStatus == HttpStatus.FOUND;
    }

    private boolean isTechnicalException(HttpStatus httpStatus) {
        switch (httpStatus) {
            case UNAUTHORIZED:
            case METHOD_NOT_ALLOWED:
            case NOT_FOUND:
            case INTERNAL_SERVER_ERROR:
                return true;
            default:
                return false;
        }
    }

    private boolean isRequestTimeoutException(HttpStatus httpStatus) {
        return httpStatus == HttpStatus.REQUEST_TIMEOUT;
    }

    protected boolean isBusinessException(Response response, HttpStatus httpStatus) {
        return httpStatus == HttpStatus.BAD_REQUEST && response.errorBody() != null;
    }
}
