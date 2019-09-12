package vn.kingbee.domain.entity.token

import com.google.gson.annotations.SerializedName

class AccessTokenRequest {
    @SerializedName("scope")
    var scope: String? = null

    @SerializedName("grant_type")
    var grantType: String? = null

    @SerializedName("token")
    var token: String? = null

    @SerializedName("Msisdn")
    var msisdn: String? = null

    @SerializedName("Authorization")
    var authorization: String? = null

    constructor(scope: String?, grantType: String?, msisdn: String?) {
        this.scope = scope
        this.grantType = grantType
        this.msisdn = msisdn
    }

    constructor(token: String?) {
        this.token = token
    }

    constructor(scope: String?, grantType: String?) {
        this.scope = scope
        this.grantType = grantType
    }
}