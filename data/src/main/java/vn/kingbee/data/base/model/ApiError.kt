package vn.kingbee.data.base.model

import com.google.gson.annotations.SerializedName

class ApiError {
    @SerializedName("errorCode")
    var errorCode: String? = null

    @SerializedName("errorMessage")
    var errorMessage: String? = null
}