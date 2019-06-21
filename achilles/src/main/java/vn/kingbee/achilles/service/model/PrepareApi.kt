package vn.kingbee.achilles.service.model

import com.google.gson.annotations.SerializedName

class PrepareApi {
    @SerializedName("apiFileName")
    var apiFileName: String? = null
    @SerializedName("sessionId")
    var sessionId: String? = null
    @SerializedName("listResult")
    var listResult: Array<String>? = null

    constructor(apiFileName: String, sessionId: String, listResult: Array<String>) {
        this.apiFileName = apiFileName
        this.sessionId = sessionId
        this.listResult = listResult
    }

    companion object {
        const val KEY = "PrepareApi"
    }
}