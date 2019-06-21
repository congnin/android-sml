package vn.kingbee.achilles.model

import com.google.gson.annotations.SerializedName

class EndpointConfigs {
    @SerializedName("version")
    var version: String? = null
    @SerializedName("config")
    var configElements: List<EndpointElement>? = null

    constructor(version: String?, configElements: List<EndpointElement>?) {
        this.version = version
        this.configElements = configElements
    }
}