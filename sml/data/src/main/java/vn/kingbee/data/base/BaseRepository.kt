package vn.kingbee.data.base

import com.google.gson.Gson
import com.google.gson.JsonSyntaxException

import java.io.BufferedReader
import java.io.InputStreamReader
import java.nio.charset.StandardCharsets

import io.reactivex.Observable
import io.reactivex.ObservableEmitter
import io.reactivex.ObservableSource
import io.reactivex.functions.Function
import okhttp3.Headers
import okhttp3.ResponseBody
import retrofit2.Response
import timber.log.Timber
import vn.kingbee.data.base.model.ErrorModel
import vn.kingbee.domain.ApiCode
import vn.kingbee.domain.core.exception.BaseException
import vn.kingbee.domain.core.exception.BusinessErrorException
import vn.kingbee.domain.core.exception.NetworkException
import vn.kingbee.domain.core.exception.RetrofitException
import vn.kingbee.domain.core.exception.SessionTokenExpiredException
import vn.kingbee.domain.core.exception.TechnicalErrorException
import vn.kingbee.domain.core.exception.UnknownException
import vn.kingbee.domain.enums.HttpStatus

open class BaseRepository {

    fun <T> processRequest(observableRequest: Observable<T>): Observable<T> {
        return observableRequest.onErrorResumeNext(Function { this.getObservableOnErrorV2(it) })
    }

    private fun <T> getObservableOnErrorV2(throwable: Throwable?): Observable<T> {
        return Observable.create { subscriber ->
            try {

                if (throwable != null && (throwable.cause is NetworkException || throwable.cause is SessionTokenExpiredException)) {
                    subscriber.onError(throwable.cause!!)
                    return@create
                }
                Timber.tag(ApiCode.TAG).d("handling throwable of retrofit")
                Timber.tag(ApiCode.TAG)
                    .d(MESSAGE + throwable?.message + " - " + TO_STRING_METHOD + throwable?.toString())
                val retrofitException = RetrofitException.fromThrowable(throwable!!)
                when {
                    retrofitException.kind == RetrofitException.Kind.HTTP -> {
                        Timber.tag(ApiCode.TAG).d("retrofitException is HTTP")
                        val response = retrofitException.response
                        Timber.tag(ApiCode.TAG).d("response: %s", response.toString())
                        val httpStatus = HttpStatus.valueOf(response?.code().toString())
                        var errorModel: ErrorModel? = null
                        if (response?.errorBody() != null) {
                            try {
                                errorModel = Gson().fromJson(
                                    this@BaseRepository.getStringFromResponseBody(response.errorBody()!!),
                                    ErrorModel::class.java
                                )
                            } catch (e: JsonSyntaxException) {
                                Timber.tag(ApiCode.TAG).d("not json model or error model")
                            }

                        }
                        // Sometime response.errorBody() non null but empty -> errorModel is parse to null
                        errorModel = errorModel ?: ErrorModel()
                        // set http code value
                        errorModel.httpCode = response?.code()!!
                        Timber.tag(ApiCode.TAG).d("response: %s", response.toString())
                        Timber.tag(ApiCode.TAG).d("ErrorModel: %s", Gson().toJson(errorModel))

                        // analyze exception
                        var exception: BaseException = UnknownException()
                        if (this@BaseRepository.isBusinessException(response, httpStatus)) {
                            exception = BusinessErrorException()
                            Timber.tag(ApiCode.TAG).d("retrofitException is BUSINESS exception")
                        } else if (this@BaseRepository.isTechnicalException(httpStatus)) {
                            exception = TechnicalErrorException()
                            Timber.tag(ApiCode.TAG).d("retrofitException is TECHNICAL exception")
                        } else if (this@BaseRepository.isRequestTimeoutException(httpStatus)) {
                            exception = NetworkException()
                            Timber.tag(ApiCode.TAG).d("retrofitException is REQUEST TIMEOUT exception")
                        } else if (this@BaseRepository.isLoginLogoutException(httpStatus)) {
                            Timber.tag(ApiCode.TAG).d("retrofitException is LOGIN/LOGOUT exception")
                            return@create
                        }
                        this@BaseRepository.handleDefaultException(subscriber, errorModel, exception, response.headers())
                    }
                    retrofitException.kind == RetrofitException.Kind.NETWORK -> {
                        Timber.tag(ApiCode.TAG).d("retrofitException is NETWORK exception")
                        val networkException = NetworkException()
                        networkException.setMessage(retrofitException.message)
                        subscriber.onError(networkException)
                    }
                    else -> {
                        Timber.tag(ApiCode.TAG).d("retrofitException is UNEXPECTED exception")
                        subscriber.onError(throwable)
                    }
                }
            } catch (e: Exception) {
                Timber.tag(ApiCode.TAG).d("get exception when handling throwable in getObservableOnErrorV2 method")
                Timber.tag(ApiCode.TAG).d(MESSAGE + e.message + " - " + TO_STRING_METHOD + e.toString())
                subscriber.onError(throwable!!)
            }
        }
    }

    private fun <T> handleDefaultException(
        subscriber: ObservableEmitter<T>,
        errorModel: ErrorModel,
        exception: BaseException,
        headers: Headers
    ) {
        exception.httpCode = errorModel.httpCode
        exception.headers = headers
        if (errorModel.errorCode != null) {
            exception.code = errorModel.errorCode
            exception.setMessage(errorModel.errorMessage)
        } else if (errorModel.errorList != null && errorModel.errorList!!.isNotEmpty()) {
            exception.code = errorModel.errorList!![0].errorCode
            exception.setMessage(errorModel.errorList!![0].errorMessage)
        }
        Timber.tag(ApiCode.TAG).d("BaseException: %s", Gson().toJson(exception))
        subscriber.onError(exception)
    }

    private fun getStringFromResponseBody(responseBody: ResponseBody): String {
        var reader: BufferedReader? = null
        val sb = StringBuilder()
        try {
            reader = BufferedReader(InputStreamReader(responseBody.byteStream(), StandardCharsets.UTF_8))
            var line: String? = reader.readLine()
            while (line != null) {
                sb.append(line)
                line = reader.readLine()
            }
        } catch (e: java.io.IOException) {
            Timber.e(e, "Get string error")
        } finally {
            if (reader != null) {
                try {
                    reader.close()
                } catch (e: java.io.IOException) {
                    Timber.e(e, "Get string error")
                }

            }
        }
        return sb.toString()
    }

    private fun isLoginLogoutException(httpStatus: HttpStatus): Boolean = (httpStatus == HttpStatus.FOUND)

    private fun isTechnicalException(httpStatus: HttpStatus): Boolean {
        return when (httpStatus) {
            HttpStatus.UNAUTHORIZED, HttpStatus.METHOD_NOT_ALLOWED, HttpStatus.NOT_FOUND, HttpStatus.INTERNAL_SERVER_ERROR -> true
            else -> false
        }
    }

    private fun isRequestTimeoutException(httpStatus: HttpStatus): Boolean = (httpStatus == HttpStatus.REQUEST_TIMEOUT)

    protected fun isBusinessException(response: Response<*>, httpStatus: HttpStatus): Boolean {
        return httpStatus === HttpStatus.BAD_REQUEST && response.errorBody() != null
    }

    companion object {
        private val MESSAGE = "message: "
        private val TO_STRING_METHOD = "toString: "
    }
}
