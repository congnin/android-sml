package vn.kingbee.mock

import android.content.Context
import okhttp3.*
import okhttp3.internal.http.HttpEngine
import okio.Buffer
import vn.kingbee.data.base.net.AdapterInterceptor
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.io.InputStream
import java.nio.charset.Charset

class MockAdapterInterceptor(mLogger: MyLogger, context: Context) : AdapterInterceptor(mLogger, context) {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response? {
        val original = chain.request()

        val config = FullMockConfigData.mockConfig(original)
        if (config != null) {
            return getMockResponse(chain,
                original,
                config.responseBodyPath,
                config.responseBodyFile,
                config.responseHeader)
        }

        val functionName = original.header(MOCK_DATA)
        if (functionName != null) {
            val file = MockAssetManager.getSelection(functionName) ?: return super.intercept(chain)
            // Request customization: add request headers
            return getMockResponse(chain, original, functionName, file)
        } else {
            return super.intercept(chain)
        }
    }

    @Throws(IOException::class)
    private fun getMockResponse(chain: Interceptor.Chain, original: Request, functionName: String, file: String): Response {
        return getMockResponse(chain, original, functionName, file, null)
    }

    @Throws(IOException::class)
    private fun getMockResponse(chain: Interceptor.Chain, original: Request, functionName: String, fileName: String?, responseHeader: List<Pair<String, String>>?): Response {
        try {
            Thread.sleep(500)
        } catch (e: InterruptedException) {
            // ignore
        }

        val response: Response
        val requestBody = chain.request().body()
        val connection = chain.connection()
        val protocol = if (connection != null) connection.protocol() else Protocol.HTTP_1_1
        myLogger.log("--> ${chain.request().method()} ${chain.request().url()} $protocol")

        val hasRequestBody = requestBody != null
        if (hasRequestBody) {
            val buffer = Buffer()
            requestBody!!.writeTo(buffer)
            var charset = UTF8
            val contentType = requestBody.contentType()
            if (contentType != null) {
                charset = contentType.charset(UTF8)
            }
            myLogger.log(buffer.readString(charset))
            myLogger.log("$END_SIGN ${chain.request().method()} (${requestBody.contentLength()} -byte body)")
        }

        try {
            val data = MockAssetManager.openMockFile(mContext, functionName, fileName!!)
            response = buildResponse(original, data, JSON_MIME_TYPE, MockAssetManager.extractHttpCodeFromFile(fileName), responseHeader)
            val responseBody = response.body()
            if (HttpEngine.hasBody(response)) {
                val source = responseBody.source()
                source.request(java.lang.Long.MAX_VALUE) // Buffer the entire body.
                val buffer = source.buffer()
                myLogger.log(buffer.clone().readString(UTF8))
            }
        } catch (e: Exception) {
            myLogger.log("Error:" + e.message)
            throw e
        }

        return response
    }

    @Throws(IOException::class)
    private fun buildResponse(request: Request, inputStream: InputStream, mimeType: String,
                              code: Int, headers: List<Pair<String, String>>?): Response {
        val responseBuilder = Response.Builder()
            .body(ResponseBody.create(MediaType.parse(mimeType), readBytes(inputStream)))
            .request(request)
            .protocol(Protocol.HTTP_1_1)
            .code(code)

        headers?.let {
            for (pair in it) {
                responseBuilder.addHeader(pair.first, pair.second)
            }
        }

        return responseBuilder.build()
    }

    companion object {
        private val UTF8 = Charset.forName("UTF-8")
        private const val MOCK_DATA = "mockdata"
        private const val JSON_MIME_TYPE = "application/json"

        @Throws(IOException::class)
        private fun readBytes(inputStream: InputStream): ByteArray {
            // this dynamically extends to take the bytes you read
            val byteBuffer = ByteArrayOutputStream()

            // this is storage overwritten on each iteration with bytes
            val bufferSize = 1024
            val buffer = ByteArray(bufferSize)

            // we need to know how may bytes were read to write them to the byteBuffer
            var len = 0
            while (len != -1) {
                byteBuffer.write(buffer, 0, len)
                len = inputStream.read(buffer)
            }
            // and then we can return your byte array.
            return byteBuffer.toByteArray()
        }
    }
}