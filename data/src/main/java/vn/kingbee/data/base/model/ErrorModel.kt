package vn.kingbee.data.base.model

import com.google.gson.annotations.SerializedName

class ErrorModel {

    var httpCode: Int = 0

    @SerializedName("errors")
    var errorList: List<ApiError>? = null

    @SerializedName("errorCode")
    var errorCode: String? = null

    @SerializedName("errorMessage")
    var errorMessage: String? = null
}
