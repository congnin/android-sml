package vn.kingbee.domain.entity.lov

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class LOV {
    @SerializedName("country")
    @Expose
    var country: List<LOVItem>? = null
}