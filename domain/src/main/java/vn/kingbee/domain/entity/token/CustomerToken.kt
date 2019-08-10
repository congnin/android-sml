package vn.kingbee.domain.entity.token

import com.google.gson.annotations.SerializedName

class CustomerToken {
    @SerializedName("accessToken")
    var accessToken: String? = null

    @SerializedName("onboardingNumber")
    var onboardingNumber: String? = null

    @SerializedName("expiresIn")
    var expiresIn: Long? = null
}