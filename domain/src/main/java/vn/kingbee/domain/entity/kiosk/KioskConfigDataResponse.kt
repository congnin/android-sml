package vn.kingbee.domain.entity.kiosk

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class KioskConfigDataResponse {
    @SerializedName("kiosks")
    @Expose
    var kiosks: List<KioskDataResponse>? = null
}