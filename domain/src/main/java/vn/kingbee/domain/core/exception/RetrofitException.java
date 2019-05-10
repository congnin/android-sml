package vn.kingbee.domain.core.exception;

import java.io.IOException;

import retrofit2.HttpException;
import retrofit2.Response;

public class RetrofitException extends RuntimeException {
    private String url;
    private Response response;
    private RetrofitException.Kind kind;

    RetrofitException(String message, String url, Response response, RetrofitException.Kind kind, Throwable exception) {
        super(message, exception);
        this.url = url;
        this.response = response;
        this.kind = kind;
    }

    public static RetrofitException httpError(String url, Response response) {
        String message = response.code() + " " + response.message();
        return new RetrofitException(message, url, response, RetrofitException.Kind.HTTP, null);
    }

    public static RetrofitException networkError(String url, Response response, IOException exception) {
        return new RetrofitException(exception.getMessage(), url, response, RetrofitException.Kind.NETWORK, exception);
    }

    public static RetrofitException unexpectedError(String url, Response response, Throwable exception) {
        return new RetrofitException(exception.getMessage(), url, response, RetrofitException.Kind.UNEXPECTED, exception);
    }

    public static RetrofitException fromThrowable(Throwable throwable) {
        if (throwable instanceof HttpException) {
            HttpException httpException = (HttpException) throwable;
            Response response = httpException.response();
            return httpError(response.raw().request().url().toString(), response);
        } else {
            return throwable instanceof IOException ? networkError(null, null, (IOException) throwable) : unexpectedError(null, null, throwable);
        }
    }

    public String getUrl() {
        return this.url;
    }

    public Response getResponse() {
        return this.response;
    }

    public RetrofitException.Kind getKind() {
        return this.kind;
    }

    public enum Kind {
        NETWORK,
        HTTP,
        UNEXPECTED;

        Kind() {
        }
    }
}
