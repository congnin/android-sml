package vn.kingbee.widget.recyclerview.help

import com.google.gson.annotations.SerializedName

class HelpVideo {
    @SerializedName("title")
    var title: String? = null
    @SerializedName("description")
    var description: String? = null
    @SerializedName("path")
    var path: String? = null
    @SerializedName("thumb")
    var thumb: String? = null
}