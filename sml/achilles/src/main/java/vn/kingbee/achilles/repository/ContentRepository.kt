package vn.kingbee.achilles.repository

import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import org.json.JSONException
import vn.kingbee.achilles.exceptions.InvalidScenarioException
import vn.kingbee.achilles.model.EndpointElement
import vn.kingbee.achilles.model.MockApiConfiguration
import vn.kingbee.achilles.model.Scenario
import java.io.IOException
import java.net.MalformedURLException

interface ContentRepository {

    val scenarioList: Array<String>
    @Throws(IOException::class, JSONException::class)
    fun getScenarioResponse(request: Request, apiFileName: String): Response

    @Throws(IOException::class, JSONException::class)
    fun getDataResponse(request: Request, fileName: String, customerResponseFilePath: String): Response

    @Throws(InvalidScenarioException::class)
    fun getScenario(scenarioPath: String, scenarioName: String): Scenario

    @Throws(MalformedURLException::class)
    fun getFileNameFromInterceptorChain(chain: Interceptor.Chain): String

    fun getEndpointConfigs(apiName: String): EndpointElement?

    fun updateApiConfiguration(mockApiConfiguration: MockApiConfiguration)

    fun getAllListResultFilePath(fileName: String): Array<String>?
}