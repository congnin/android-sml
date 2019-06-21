package vn.kingbee.achilles.model

import com.google.gson.annotations.SerializedName

class EndpointElement {
    @SerializedName("name")
    var name: String? = null
    @SerializedName("delay")
    var delay: Int? = null
    @SerializedName("data")
    var dataField: List<Data>?= null

    constructor(name: String?, delay: Int?, dataField: List<Data>?) {
        this.name = name
        this.delay = delay
        this.dataField = dataField
    }

    inner class Data {
        @SerializedName("dataField")
        var dataField: String? = null
        @SerializedName("code")
        var code: Int = 0

        constructor(dataField: String?, code: Int) {
            this.dataField = dataField
            this.code = code
        }
    }
}