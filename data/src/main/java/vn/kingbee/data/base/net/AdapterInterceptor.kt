package vn.kingbee.data.base.net

import android.content.Context
import okhttp3.Interceptor
import okhttp3.Response
import java.nio.charset.Charset
import okhttp3.internal.Platform
import okhttp3.internal.Platform.INFO
import java.io.IOException
import vn.kingbee.domain.core.exception.NetworkException
import android.net.ConnectivityManager
import okhttp3.Protocol
import vn.kingbee.domain.core.exception.SessionTokenExpiredException
import vn.kingbee.domain.enums.HttpStatus
import okhttp3.Headers
import okio.Buffer
import java.io.EOFException
import java.util.concurrent.TimeUnit
import okhttp3.internal.http.HttpEngine
import java.nio.charset.UnsupportedCharsetException


open class AdapterInterceptor(var myLogger: MyLogger, context: Context) : Interceptor {
    private var sessionToken = ""
    var mContext: Context = context

    @Volatile
    var level = Level.NONE

    fun setLevel(level: Level): AdapterInterceptor {
        this.level = level
        return this
    }

    constructor(context: Context) : this(MyLogger.DEFAULT, context)

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response? {
        val cm = mContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val netInfo = cm.activeNetworkInfo

        if (netInfo == null || !netInfo.isConnected) {
            val networkException = NetworkException()
            networkException.isInternetConnectionError = true
            throw networkException
        }
        val level = this.level

        val request =
            chain.request()?.newBuilder()?.addHeader(HEADER_SESSION_TOKEN, sessionToken)?.build()
        if (level == Level.NONE) {
            val response = chain.proceed(request)
            if (response != null) {
                val headers = response.headers()
                if (headers?.get(HEADER_SESSION_TOKEN) != null) {
                    sessionToken = headers.get(HEADER_SESSION_TOKEN)
                }
                // session token expired
                if (headers.get(BE_ERROR).isEmpty() && response.code() == HttpStatus.ACCEPTED.value()
                    || headers.get(BE_ERROR).isEmpty() && response.code() == HttpStatus.UNAUTHORIZED.value()) {
                    throw SessionTokenExpiredException()
                }
            }
        }

        val logBody = level == Level.BODY
        val logHeaders = logBody || level == Level.HEADERS

        val requestBody = request?.body()
        val hasRequestBody = requestBody != null

        val connection = chain.connection()
        val protocol = if (connection != null) connection.protocol() else Protocol.HTTP_1_1
        var requestStartMessage = "--> " + request?.method() + ' ' + request?.url() + ' ' + protocol
        if (!logHeaders && hasRequestBody) {
            requestStartMessage += " (" + requestBody?.contentLength() + "-byte body)"
        }
        myLogger.log(requestStartMessage)

        if (logHeaders) {
            if (hasRequestBody) {
                // Request body headers are only present when installed as a network interceptor. Force
                // them to be included (when available) so there values are known.
                if (requestBody?.contentType() != null) {
                    myLogger.log("Content-Type: " + requestBody.contentType())
                }
                if (requestBody?.contentLength() != -1L) {
                    myLogger.log("Content-Length: " + requestBody?.contentLength())
                }
            }

            val headers = request?.headers()
            var i = 0
            val count = headers?.size() ?: 0
            while (i < count) {
                val name = headers?.name(i)
                // Skip headers from the request body as they are explicitly logged above.
                if (!"Content-Type".equals(name, ignoreCase = true)
                    && !"Content-Length".equals(name, ignoreCase = true)) {
                    myLogger.log(name + ": " + headers?.value(i))
                }
                i++
            }

            if (!logBody || !hasRequestBody) {
                myLogger.log(END_SIGN + request?.method())
            } else if (request?.headers() != null &&
                bodyEncoded(request.headers())) {
                myLogger.log(END_SIGN + request.method() + " (encoded body omitted)")
            } else {
                val buffer = Buffer()
                requestBody?.writeTo(buffer)

                var charset = UTF8
                val contentType = requestBody?.contentType()
                if (contentType != null) {
                    charset = contentType.charset(UTF8)
                }

                myLogger.log("")
                if (isPlaintext(buffer)) {
                    myLogger.log(buffer.readString(charset))
                    myLogger.log(END_SIGN + request?.method()
                            + " (" + requestBody?.contentLength() + "-byte body)")
                } else {
                    myLogger.log(END_SIGN + request?.method() + " (binary "
                            + requestBody?.contentLength() + "-byte body omitted)")
                }
            }
        }

        val startNs = System.nanoTime()
        val response: Response?
        try {
            response = chain.proceed(request)
            if (response != null) {
                val headers = response.headers()
                if (headers?.get(HEADER_SESSION_TOKEN) != null) {
                    sessionToken = headers.get(HEADER_SESSION_TOKEN)
                }
                // session token expired
                if (headers!!.get(BE_ERROR).isEmpty() && response.code() == HttpStatus.ACCEPTED.value()
                    || headers.get(BE_ERROR).isEmpty() && response.code() == HttpStatus.UNAUTHORIZED.value()) {
                    throw SessionTokenExpiredException()
                }
            }
        } catch (e: Exception) {
            myLogger.log("<-- HTTP FAILED: $e")
            throw e
        }
        val tookMs = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startNs)

        val responseBody = response?.body()
        val contentLength = responseBody?.contentLength()
        val bodySize = if (contentLength != -1L) (contentLength).toString() + "-byte" else "unknown-length"
        myLogger.log(("<-- " + response?.code() + ' ' + response?.message() + ' '
                + response?.request()?.url() + " (" + tookMs + "ms" + (if (!logHeaders) ", $bodySize body" else "") + ')'))

        if (logHeaders) {
            val headers = response?.headers()
            var i = 0
            val count = headers?.size() ?: 0
            while (i < count) {
                myLogger.log(headers?.name(i) + ": " + headers?.value(i))
                i++
            }

            if (!logBody || !HttpEngine.hasBody(response)) {
                myLogger.log("<-- END HTTP")
            } else if (response?.headers() != null &&
                bodyEncoded(response.headers())) {
                myLogger.log("<-- END HTTP (encoded body omitted)")
            } else {
                val source = responseBody?.source()
                source?.request(java.lang.Long.MAX_VALUE) // Buffer the entire body.
                val buffer = source?.buffer()

                var charset = UTF8
                val contentType = responseBody?.contentType()
                if (contentType != null) {
                    try {
                        charset = contentType.charset(UTF8)
                    } catch (e: UnsupportedCharsetException) {
                        myLogger.log("")
                        myLogger.log("Couldn't decode the response body; charset is likely malformed.")
                        myLogger.log("<-- END HTTP")

                        return response
                    }

                }

                if (buffer != null && !isPlaintext(buffer)) {
                    myLogger.log("")
                    myLogger.log("<-- END HTTP (binary " + buffer.size() + "-byte body omitted)")
                    return response
                }

                if (buffer != null && contentLength != 0L) {
                    myLogger.log("")
                    myLogger.log(buffer.clone().readString(charset))
                }

                myLogger.log("<-- END HTTP (" + buffer?.size() + "-byte body)")
            }
        }

        return response!!
    }

    private fun bodyEncoded(headers: Headers): Boolean {
        val contentEncoding = headers.get("Content-Encoding")
        return contentEncoding != null && !contentEncoding.equals("identity", ignoreCase = true)
    }

    enum class Level {
        NONE, BASIC, HEADERS, BODY
    }

    interface MyLogger {
        fun log(message: String)

        companion object {
            val DEFAULT: MyLogger = object : MyLogger {
                override fun log(message: String) {
                    Platform.get().log(INFO, message, null)
                }
            }
        }
    }

    companion object {
        private val UTF8 = Charset.forName("UTF-8")
        const val END_SIGN = "--> END "
        const val BE_ERROR = "BE_ERROR"
        private const val HEADER_SESSION_TOKEN = "sessiontoken"

        /**
         * Returns true if the body in question probably contains human readable text. Uses a small sample
         * of code points to detect unicode control characters commonly used in binary file signatures.
         */
        @Throws(EOFException::class)
        fun isPlaintext(buffer: Buffer): Boolean {
            try {
                val prefix = Buffer()
                val byteCount = (if (buffer.size() < 64) buffer.size() else 64).toLong()
                buffer.copyTo(prefix, 0, byteCount)
                for (i in 0..15) {
                    if (prefix.exhausted()) {
                        break
                    }
                    val codePoint = prefix.readUtf8CodePoint()
                    if (Character.isISOControl(codePoint) && !Character.isWhitespace(codePoint)) {
                        return false
                    }
                }
                return true
            } catch (e: EOFException) {
                return false // Truncated UTF-8 sequence.
            }

        }
    }
}