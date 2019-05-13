package vn.kingbee.mock

import android.net.Uri
import okhttp3.Headers
import vn.kingbee.mock.MockAssetManager.JSON_EXT

private const val PATH_REGEX = "\\{.*?\\}"

object FullMockConfigData {

    private const val LOCATION_CODE_HEADER = "http://localhost/callback?" +
            "code=29e00777-248e-3076-9db0-2a6294d77992"
    private val fullMockData = HashMap<HttpMethod, ArrayList<MockConfig>>()

    private var isFullMockSupport = true

    fun setFullMockSupport(value: Boolean) {
        val fullMockConfigs = mutableListOf<MockConfig>()
        fullMockData.values.forEach { fullMockConfigs.addAll(it) }
        MockAssetManager.applyFullMockDefault(fullMockConfigs)
        isFullMockSupport = value
    }

    fun isFullMockSupport() = isFullMockSupport

    fun applySelection(name: String, mockSelection: String) {
        fullMockData.values.forEach {
            it.filter { config -> config.responseBodyPath == name }
                .forEach { it.responseBodyFile = mockSelection }
        }
    }

    fun mockConfig(request: okhttp3.Request): MockConfig? {
        if (isFullMockSupport) {
            try {
                val url = request.url().encodedPath()
                val headers = request.headers()
                val queries = Uri.parse(url).queryParameterNames
                return fullMockData[HttpMethod.valueOf(request.method().toUpperCase())]?.firstOrNull {
                    isMatchUrl(url, it) && isMatchParamOrQuery(it, headers, queries)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        return null
    }

    private fun isMatchParamOrQuery(it: MockConfig, headers: Headers, queries: MutableSet<String>) =
        it.filterKey == null || headers[it.filterKey] != null || queries.contains(it.filterKey)

    private fun isMatchUrl(url: String, it: MockConfig): Boolean {
        val pattern = it.url.replace(Regex(PATH_REGEX), ".+") + "$"
        return url.contains(Regex(pattern))
    }

    private fun addConfig(method: HttpMethod,
                          url: String,
                          mockName: String,
                          mockFile: String? = null,
                          headerResponse: List<Pair<String, String>>? = null,
                          filterKey: String? = null) {
        if (!fullMockData.containsKey(method)) {
            fullMockData[method] = ArrayList()
        }

        fullMockData[method]!!.add(0,
            MockConfig(url,
                mockName,
                if (mockFile.isNullOrBlank()) (mockName + JSON_EXT) else mockFile,
                headerResponse,
                filterKey))
    }

    // add mock config for service in init
    init {

    }
}