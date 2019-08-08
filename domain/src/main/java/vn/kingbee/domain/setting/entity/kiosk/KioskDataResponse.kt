package vn.kingbee.domain.setting.entity.kiosk

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class KioskDataResponse {
    @SerializedName("imei")
    @Expose
    var imei: String? = null

    @SerializedName("type")
    @Expose
    var type: String? = null

    @SerializedName("edcIpAddress")
    @Expose
    var edcIpAddress: String? = null

    @SerializedName("edcPort")
    @Expose
    var edcPort: Int = 0

    @SerializedName("minimumCardAmount")
    @Expose
    var minimumCardAmount: Int = 0

    @SerializedName("maximumCardAmount")
    @Expose
    var maximumCardAmount: Int = 0

    @SerializedName("remainingCardAmount")
    @Expose
    var remainingCardAmount: Int = 0

    @SerializedName("terminalID")
    @Expose
    var terminalID: String? = null

    @SerializedName("commissionDate")
    @Expose
    var commissionDate: Long = 0

    @SerializedName("decommissionDate")
    @Expose
    var decommissionDate: Long = 0

    @SerializedName("location")
    @Expose
    var location: String? = null

    @SerializedName("branchCode")
    @Expose
    var branchCode: String? = null

    @SerializedName("trackingStatusEnabled")
    @Expose
    var trackingStatusEnabled: Boolean = false
}