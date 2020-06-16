package vn.kingbee.domain.entity.lov

import com.google.gson.annotations.SerializedName

class LOVItem {
    @SerializedName("code")
    var code: String? = null

    @SerializedName("name")
    var name: String? = null
}