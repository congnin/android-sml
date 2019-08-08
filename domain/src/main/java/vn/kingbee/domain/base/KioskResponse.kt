package vn.kingbee.domain.base

import com.google.gson.annotations.SerializedName

class KioskResponse<D> {
    @SerializedName("success")
    var isSuccessful: Boolean? = null

    @SerializedName("data")
    var data: D? = null

    @SerializedName("errors")
    var errors: List<ErrorResponse>? = null

    @SerializedName("warnings")
    var warnings: List<ErrorResponse>? = null
}