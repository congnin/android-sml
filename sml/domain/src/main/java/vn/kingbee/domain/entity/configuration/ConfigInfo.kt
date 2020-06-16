package vn.kingbee.domain.entity.configuration

import com.google.gson.annotations.SerializedName

class ConfigInfo {
    @SerializedName("configurations")
    var configurations: List<Configuration>? = null
}