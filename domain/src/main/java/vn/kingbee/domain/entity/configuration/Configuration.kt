package vn.kingbee.domain.entity.configuration

import com.google.gson.annotations.SerializedName

class Configuration {
    @SerializedName("key")
    var key: String? = null

    @SerializedName("value")
    var value: String? = null
}