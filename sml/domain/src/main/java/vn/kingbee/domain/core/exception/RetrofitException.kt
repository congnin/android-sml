package vn.kingbee.domain.core.exception

import java.io.IOException

import retrofit2.HttpException
import retrofit2.Response

class RetrofitException internal constructor(
    message: String?,
    val url: String?,
    val response: Response<*>?,
    val kind: Kind,
    exception: Throwable?
) : RuntimeException(message, exception) {

    enum class Kind {
        NETWORK,
        HTTP,
        UNEXPECTED
    }

    companion object {

        fun httpError(url: String, response: Response<*>): RetrofitException {
            val message = response.code().toString() + " " + response.message()
            return RetrofitException(message, url, response, Kind.HTTP, null)
        }

        fun networkError(url: String?, response: Response<*>?, exception: IOException): RetrofitException {
            return RetrofitException(exception.message, url, response, Kind.NETWORK, exception)
        }

        fun unexpectedError(url: String?, response: Response<*>?, exception: Throwable): RetrofitException {
            return RetrofitException(exception.message, url, response, Kind.UNEXPECTED, exception)
        }

        fun fromThrowable(throwable: Throwable): RetrofitException {
            if (throwable is HttpException) {
                val response = throwable.response()
                return httpError(response.raw().request().url().toString(), response)
            } else {
                return if (throwable is IOException) networkError(null, null, throwable) else unexpectedError(
                    null,
                    null,
                    throwable
                )
            }
        }
    }
}
