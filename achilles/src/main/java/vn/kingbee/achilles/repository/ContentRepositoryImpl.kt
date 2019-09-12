package vn.kingbee.achilles.repository

import android.content.Context
import androidx.annotation.NonNull
import okhttp3.*
import org.json.JSONException
import org.json.JSONObject
import timber.log.Timber
import vn.kingbee.achilles.exceptions.InvalidScenarioException
import vn.kingbee.achilles.model.*
import vn.kingbee.achilles.util.JsonPathUtil
import vn.kingbee.achilles.util.StringUtils
import vn.kingbee.achilles.util.Utils
import java.io.ByteArrayInputStream
import java.io.IOException
import java.io.InputStream
import java.io.UnsupportedEncodingException
import java.net.MalformedURLException
import java.net.URLConnection
import java.util.Comparator
import java.util.HashMap

abstract class ContentRepositoryImpl(
    protected var context: Context,
    protected var mockApiConfiguration: MockApiConfiguration
) : ContentRepository {
    protected val alphabeticallySortOrder: Comparator<String> =
        Comparator { text1, text2 -> text1.compareTo(text2, ignoreCase = true) }
    private var mCurrentSequenceCounter = 1
    private var mCurrentFilePath = ""

    protected abstract val tag: String

    protected abstract fun getFileNameFromPrefix(context: Context, filePath: String, prefixFileName: String): String?

    protected abstract fun readContent(context: Context, assertUrl: String): String?

    @Throws(IOException::class, JSONException::class)
    override fun getDataResponse(request: Request, fileName: String, customerResponseFilePath: String): Response {
        val jsonRequestBody = this.getJsonStringFromInterceptorChain(request)
        var jObject: JSONObject? = null

        try {
            jObject = if (jsonRequestBody != null) JSONObject(jsonRequestBody) else null
        } catch (e: JSONException) {
            Timber.d("Achiles - " + e.message)
        }

        val endpointElement = this.getEndpointConfigs(fileName)
        val dataFields = HashMap<String, String>()
        val endpointElementDataField = endpointElement!!.dataField
        if (jObject != null && endpointElementDataField != null) {
            this.parseDataFields(dataFields, endpointElementDataField, jObject)
        }

        val responseFilePath = if (StringUtils.isEmpty(customerResponseFilePath)) this.getDataResponseFilePath(
            endpointElement,
            fileName,
            dataFields
        ) else customerResponseFilePath
        val code = this.getCodeFromMockFile(fileName, responseFilePath)
        val headers = this.getResponseHeaderFromPath(fileName, responseFilePath)
        val inputStream = this.getResponseBodyInputStreamFromPath(fileName, responseFilePath)
        var mimeType: String? = URLConnection.guessContentTypeFromStream(inputStream)
        if (mimeType == null) {
            mimeType = JSON_MIMETYPE
        }

        return this.getResponse(request, inputStream, headers, mimeType, code)
    }

    private fun parseDataFields(
        dataFields: MutableMap<String, String>, @NonNull endpointElementDataField: List<EndpointElement.Data>,
        jObject: JSONObject
    ) {
        val lstData = endpointElementDataField.iterator()

        while (lstData.hasNext()) {
            val field = lstData.next()

            try {
                dataFields[field.dataField!!] = JsonPathUtil.getJsonValue(field.dataField!!, jObject)
            } catch (e: JSONException) {
                Timber.tag(this.tag).e(e.message, *arrayOfNulls(0))
            }
        }
    }

    @Throws(InvalidScenarioException::class)
    override fun getScenario(scenarioPath: String, scenarioName: String): Scenario {
        val fileName = this.getFileNameFromPrefix(this.context, scenarioPath, scenarioName)
        if (StringUtils.isEmpty(fileName)) {
            throw InvalidScenarioException("Invalid Scenario")
        } else {
            val fName = String.format("%s/%s", scenarioPath, fileName)
            val content = this.getScenarioJsonString(fName)
            return if (StringUtils.isEmpty(content)) {
                throw InvalidScenarioException("Invalid Scenario")
            } else {
                JsonPathUtil.createGson().fromJson(content, Scenario::class.java) as Scenario
            }
        }
    }

    @NonNull
    @Throws(IOException::class, JSONException::class)
    override fun getScenarioResponse(request: Request, apiFileName: String): Response {
        var scenario: Scenario? = null

        try {
            scenario =
                this.getScenario(this.mockApiConfiguration.scenarioPath!!, this.mockApiConfiguration.scenarioName!!)
        } catch (e: InvalidScenarioException) {
            Timber.tag(this.tag).e(e.message, arrayOfNulls<Any>(0))
        }

        val responseFilePath = this.getApiResponseFromScenario(scenario, apiFileName)
        val inputStream = this.getResponseBodyInputStreamFromPath(apiFileName, responseFilePath)
        var mimeType: String? = URLConnection.guessContentTypeFromStream(inputStream)
        if (mimeType == null) {
            mimeType = JSON_MIMETYPE
        }

        return this.getResponse(request, inputStream, null as Headers?, mimeType, SUCCESS)
    }

    @NonNull
    @Throws(IOException::class)
    protected fun getResponse(
        request: Request,
        inputStream: InputStream,
        headers: Headers?,
        mimeType: String,
        code: Int
    ): Response {
        val responseBody = ResponseBody.create(MediaType.parse(mimeType), Utils.readBytes(inputStream))
        return Response.Builder().body(responseBody).headers(
            headers
                ?: Headers.Builder().build()
        ).request(request).message("").protocol(Protocol.HTTP_1_1).message("Mock message").code(code).build()
    }

    @NonNull
    protected fun getApiResponseFromScenario(scenario: Scenario?, filePath: String): String {
        var responseFilePath = filePath
        if (this.isScenarioValid(scenario)) {
            val ls = scenario!!.configs
            val lsIterator = ls?.iterator()

            while (lsIterator!!.hasNext()) {
                val apiConfig = lsIterator.next()
                if (apiConfig.response!!.contains(filePath)) {
                    responseFilePath = this.getResponseFilePath(apiConfig, filePath)
                    break
                }
            }
        }

        return responseFilePath
    }

    protected fun isScenarioValid(scenario: Scenario?): Boolean {
        return scenario?.configs != null && scenario.configs!!.isNotEmpty()
    }

    protected fun getResponseFilePath(apiConfig: ApiConfig, filePath: String): String {
        if (StringUtils.isEmpty(this.mCurrentFilePath)) {
            this.mCurrentFilePath = filePath
        } else if (this.mCurrentFilePath != filePath) {
            this.mCurrentFilePath = filePath
            this.mCurrentSequenceCounter = 1
        }

        if (apiConfig.sequence > 0 && this.mCurrentSequenceCounter == apiConfig.sequence) {
            this.mCurrentSequenceCounter = apiConfig.sequence
            ++this.mCurrentSequenceCounter
            return apiConfig.response.toString()
        } else {
            this.mCurrentSequenceCounter = 1
            return apiConfig.response.toString()
        }
    }

    private fun getScenarioJsonString(fName: String): String? {
        return this.readContent(this.context, fName)
    }

    @Throws(JSONException::class)
    protected fun getResponseHeaderFromPath(filePath: String, prefixFileName: String): Headers {
        val fileName = this.getFileNameFromPrefix(
            this.context,
            String.format("%s/%s", this.mockApiConfiguration.apiPath, filePath),
            prefixFileName
        )
        val assertUrl = String.format(
            "%s/%s/%s", this.mockApiConfiguration.apiPath, filePath, fileName
                ?: filePath
        )
        val content = this.readContent(this.context, assertUrl)
        val headerBuilder = Headers.Builder()
        var jObject: JSONObject? = null

        try {
            jObject = if (content != null) JSONObject(content) else null
        } catch (var11: Exception) {
            Timber.tag(this.tag).d(var11.message, *arrayOfNulls(0))
        }

        if (jObject != null && jObject.has(KEY_HEADER)) {
            jObject = jObject.getJSONObject(KEY_HEADER)
            val temp = jObject!!.keys()

            while (temp.hasNext()) {
                val key = temp.next() as String
                val value = jObject.getString(key)
                headerBuilder.add(key, value)
            }
        }

        return headerBuilder.build()
    }

    @Throws(JSONException::class, UnsupportedEncodingException::class)
    protected fun getResponseBodyInputStreamFromPath(filePath: String, prefixFileName: String): InputStream {
        val fileName = this.getFileNameFromPrefix(
            this.context,
            String.format("%s/%s", this.mockApiConfiguration.apiPath, filePath),
            prefixFileName
        )
        val assertUrl = String.format(
            "%s/%s/%s", this.mockApiConfiguration.apiPath, filePath, fileName
                ?: filePath
        )
        var content = this.readContent(this.context, assertUrl)
        var jObject: JSONObject? = null

        try {
            jObject = if (content != null) JSONObject(content) else null
        } catch (e: Exception) {
            Timber.tag(this.tag).d(e.message, *arrayOfNulls(0))
        }

        if (jObject != null) {
            if (jObject.has(KEY_DETAIL)) {
                jObject = jObject.getJSONObject(KEY_DETAIL)
                content = jObject!!.toString()
            } else if (jObject.has(KEY_HEADER)) {
                content = ""
            }
        }

        return ByteArrayInputStream(content!!.toByteArray(charset(UTF_8)))
    }

    protected fun getCodeFromMockFile(filePath: String, prefixFileName: String): Int {
        val fileName = this.getFileNameFromPrefix(
            this.context,
            String.format("%s/%s", this.mockApiConfiguration.apiPath, filePath),
            prefixFileName
        )
        val assertUrl = String.format(
            "%s/%s/%s", this.mockApiConfiguration.apiPath, filePath, fileName
                ?: filePath
        )
        return this.getExceptionCode(assertUrl)
    }

    private fun getExceptionCode(@NonNull assertUrl: String): Int {
        val index = assertUrl.indexOf(KEY_EXCEPTION)

        try {
            return if (index != -1) Integer.valueOf(assertUrl.substring(index + 1, assertUrl.lastIndexOf(KEY_EXCEPTION))) else SUCCESS
        } catch (numformatEx: NumberFormatException) {
            return SUCCESS
        } catch (outException: IndexOutOfBoundsException) {
            return SUCCESS
        }

    }

    override fun getEndpointConfigs(apiName: String): EndpointElement? {
        var endpointElement: EndpointElement? = null
        val content = this.readContent(this.context, this.mockApiConfiguration.endpointConfigPath!!)
        val configs = JsonPathUtil.createGson().fromJson(content, EndpointConfigs::class.java) as EndpointConfigs
        val lstIterator = configs.configElements?.iterator()

        while (lstIterator!!.hasNext()) {
            val element = lstIterator.next() as EndpointElement
            Timber.d(TAG + " " + element.name)
            if (element.name.equals(apiName, true)) {
                endpointElement = element
                break
            }
        }

        return endpointElement
    }

    @NonNull
    @Throws(MalformedURLException::class)
    override fun getFileNameFromInterceptorChain(chain: Interceptor.Chain): String {
        return Utils.getApiFileNameFromRequest(chain.request())
    }

    @Throws(IOException::class)
    protected fun getJsonStringFromInterceptorChain(request: Request): String? {
        return if ("POST".equals(request.method(), ignoreCase = true)) Utils.getRequestBodyStringFromInterceptorChain(
            request
        ) else null
    }

    protected fun getDataResponseFilePath(
        endpointElement: EndpointElement,
        apiFileName: String,
        dataFields: Map<String, String>
    ): String {
        return Utils.getDataResponseFilePathFromEndpointConfig(endpointElement.dataField, dataFields, apiFileName)
    }

    override fun updateApiConfiguration(mockApiConfiguration: MockApiConfiguration) {
        this.mockApiConfiguration = mockApiConfiguration
    }

    companion object {
        private const val TAG = "ContentRepositoryImpl"
        private const val SUCCESS = 200
        private const val JSON_MIMETYPE = "application/json"
        private const val UTF_8 = "UTF-8"
        private const val KEY_EXCEPTION = "#"
        private const val KEY_HEADER = "Header"
        private const val KEY_DETAIL = "Detail"
    }
}