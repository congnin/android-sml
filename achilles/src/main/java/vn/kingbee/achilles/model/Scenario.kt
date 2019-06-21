package vn.kingbee.achilles.model

import com.google.gson.annotations.SerializedName

class Scenario {
    @SerializedName("name")
    var name: String? = null
    @SerializedName("version")
    var version: String? = null
    @SerializedName("configs")
    var configs: List<ApiConfig>? = null
}