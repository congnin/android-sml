package vn.kingbee.achilles.util

import okhttp3.Headers
import okhttp3.Interceptor
import okhttp3.Protocol
import okhttp3.Response
import okio.Buffer
import timber.log.Timber
import java.io.EOFException
import java.io.IOException
import java.nio.charset.Charset
import java.nio.charset.UnsupportedCharsetException
import java.util.concurrent.TimeUnit


class OkHttpLogUtils {
    companion object {
        private val UTF8 = Charset.forName("UTF-8")

        @Throws(IOException::class)
        fun logRequest(chain: Interceptor.Chain) {
            val builder = StringBuilder()
            val request = chain.request()
            val requestBody = request.body()
            val hasRequestBody = requestBody != null
            val connection = chain.connection()
            val protocol = if (connection != null) connection.protocol() else Protocol.HTTP_1_1
            var requestStartMessage = "--> " + request.method() + ' '.toString() + request.url() + ' '.toString() + protocol
            if (hasRequestBody) {
                requestStartMessage = requestStartMessage + " (" + requestBody!!.contentLength() + "-byte body)"
            }

            builder.append(requestStartMessage)
            if (hasRequestBody) {
                if (requestBody!!.contentType() != null) {
                    builder.append("Content-Type: " + requestBody.contentType())
                }

                if (requestBody.contentLength() != -1L) {
                    builder.append("Content-Length: " + requestBody.contentLength())
                }
            }

            val headers = request.headers()
            var i = 0

            val count = headers.size()
            while (i < count) {
                val name = headers.name(i)
                if (!"Content-Type".equals(name, ignoreCase = true) && !"Content-Length".equals(name, ignoreCase = true)) {
                    builder.append(name + ": " + headers.value(i))
                }
                ++i
            }

            if (!hasRequestBody) {
                builder.append("--> END " + request.method())
            } else if (bodyEncoded(request.headers())) {
                builder.append("--> END " + request.method() + " (encoded body omitted)")
            } else {
                val buffer = Buffer()
                requestBody!!.writeTo(buffer)
                var charset = UTF8
                val contentType = requestBody.contentType()
                if (contentType != null) {
                    charset = contentType.charset(UTF8)
                }

                builder.append("")
                if (isPlaintext(buffer)) {
                    builder.append(buffer.readString(charset))
                    builder.append("--> END " + request.method() + " (" + requestBody.contentLength() + "-byte body)")
                } else {
                    builder.append("--> END " + request.method() + " (binary " + requestBody.contentLength() + "-byte body omitted)")
                }
            }

            Timber.tag("Request").i(builder.toString(), *arrayOfNulls<Any>(0))
        }

        @Throws(IOException::class)
        fun logResponse(response: Response) {
            val builder = StringBuilder()
            val startNs = System.nanoTime()
            TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startNs)
            val responseBody = response.body()
            val contentLength = responseBody.contentLength()
            val bodySize = if (contentLength != -1L) "$contentLength-byte" else "unknown-length"
            builder.append("<-- " + response.code() + ' '.toString() + response.message() + ' '.toString() + response.request().url() + " (" + bodySize + " body" + ')'.toString())
            val headers = response.headers()
            var i = 0

            val count = headers.size()
            while (i < count) {
                builder.append(headers.name(i) + ": " + headers.value(i))
                ++i
            }

            if (response.body() == null) {
                builder.append("<-- END HTTP")
            } else if (bodyEncoded(response.headers())) {
                builder.append("<-- END HTTP (encoded body omitted)")
            } else {
                val source = responseBody.source()
                source.request(9223372036854775807L)
                val buffer = source.buffer()
                var charset = UTF8
                val contentType = responseBody.contentType()
                if (contentType != null) {
                    try {
                        charset = contentType.charset(UTF8)
                    } catch (var16: UnsupportedCharsetException) {
                        builder.append("")
                        builder.append("Couldn't decode the response body; charset is likely malformed.")
                        builder.append("<-- END HTTP")
                        return
                    }

                } else if (!isPlaintext(buffer)) {
                    builder.append("")
                    builder.append("<-- END HTTP (binary " + buffer.size() + "-byte body omitted)")
                } else if (contentLength != 0L) {
                    builder.append("")
                    builder.append(buffer.clone().readString(charset))
                }

                builder.append("<-- END HTTP (" + buffer.size() + "-byte body)")
            }

            Timber.tag("Response").i(builder.toString(), *arrayOfNulls<Any>(0))
        }

        internal fun isPlaintext(buffer: Buffer): Boolean {
            try {
                val prefix = Buffer()
                val byteCount = if (buffer.size() < 64L) buffer.size() else 64L
                buffer.copyTo(prefix, 0L, byteCount)

                var i = 0
                while (i < 16 && !prefix.exhausted()) {
                    val codePoint = prefix.readUtf8CodePoint()
                    if (Character.isISOControl(codePoint) && !Character.isWhitespace(codePoint)) {
                        return false
                    }
                    ++i
                }

                return true
            } catch (var6: EOFException) {
                return false
            }

        }

        private fun bodyEncoded(headers: Headers): Boolean {
            val contentEncoding = headers.get("Content-Encoding")
            return contentEncoding != null && !contentEncoding.equals("identity", ignoreCase = true)
        }
    }
}