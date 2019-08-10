package vn.kingbee.domain.entity.token

import com.google.gson.annotations.SerializedName

class AccessTokenResponse {
    @SerializedName("token_type")
    var tokenType: String? = null

    @SerializedName("expires_in")
    var expiresIn: Long? = null

    @SerializedName("access_token")
    var accessToken: String? = null

    @SerializedName("success")
    var success: Boolean = false

    @SerializedName("data")
    var data: CustomerToken? = null
}