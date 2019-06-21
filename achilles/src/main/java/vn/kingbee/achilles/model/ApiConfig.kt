package vn.kingbee.achilles.model

import com.google.gson.annotations.SerializedName

class ApiConfig {
    @SerializedName("api")
    var api: String? = null
    @SerializedName("response")
    var response: String? = null
    @SerializedName("seq")
    var sequence: Int = 0

    constructor(api: String?, response: String?, sequence: Int) {
        this.api = api
        this.response = response
        this.sequence = sequence
    }
}