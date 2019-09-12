package vn.kingbee.model

import com.google.gson.annotations.SerializedName

class Province {
    @SerializedName("town")
    var mTown: List<Town>? = null

    @SerializedName("code")
    var mCode: String? = null

    @SerializedName("name")
    var mName: String? = null

    @SerializedName("ektp_name")
    val ektpName: String? = null
}