package vn.kingbee.achilles.model

import com.google.gson.annotations.SerializedName
import kotlin.jvm.internal.Intrinsics

class MockApiConfiguration {
    @SerializedName("scenarioName")
    var scenarioName: String? = null
    @SerializedName("apiPath")
    var apiPath: String? = null
    @SerializedName("scenarioPath")
    var scenarioPath: String? = null
    @SerializedName("scenario")
    var scenario: Scenario? = null
    @SerializedName("endpointConfigPath")
    var endpointConfigPath: String? = null

    constructor(scenarioName: String?,
                apiPath: String?,
                scenarioPath: String?,
                scenario: Scenario?,
                endpointConfigPath: String?) {
        this.scenarioName = scenarioName
        this.apiPath = apiPath
        this.scenarioPath = scenarioPath
        this.scenario = scenario
        this.endpointConfigPath = endpointConfigPath
    }

    class Builder {
        private var scenarioName: String? = null
        private var apiPath: String? = null
        private var scenarioPath: String? = null
        private var scenario: Scenario? = null
        private var endpointConfigPath: String? = null

        fun scenarioName(scenarioName: String): Builder {
            this.scenarioName = scenarioName
            return this
        }

        fun apiPath(apiPath: String): Builder {
            this.apiPath = apiPath
            return this
        }

        fun scenarioPath(scenarioPath: String): Builder {
            this.scenarioPath = scenarioPath
            return this
        }

        fun scenario(scenario: Scenario): Builder {
            this.scenario = scenario
            return this
        }

        fun endpointConfigPath(endpointConfigPath: String): Builder {
            this.endpointConfigPath = endpointConfigPath
            return this
        }

        fun build(): MockApiConfiguration {
            return MockApiConfiguration(this.scenarioName,
                this.apiPath, this.scenarioPath, this.scenario, this.endpointConfigPath)
        }
    }
}