package vn.kingbee.achilles.service.model

import com.google.gson.annotations.SerializedName

class ApiSession {
    companion object {
        const val KEY = "ApiSession"
    }

    @SerializedName("apiName")
    var apiName: String? = null
    @SerializedName("sessionId")
    var sessionId: String? = null

    constructor(apiName: String, sessionId: String) {
        this.apiName = apiName
        this.sessionId = sessionId
    }
}