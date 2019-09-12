package vn.kingbee.model

import com.google.gson.annotations.SerializedName

class ProvinceResponse {
    @SerializedName("province")
    var provinces: List<Province>? = null
}