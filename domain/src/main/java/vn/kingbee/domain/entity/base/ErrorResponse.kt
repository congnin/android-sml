package vn.kingbee.domain.entity.base

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ErrorResponse {

    @SerializedName("code")
    @Expose
    var code: String? = null
    @SerializedName("message")
    @Expose
    var message: String? = null
    @SerializedName("channel")
    @Expose
    var channel: String? = null

    constructor(builder: Builder) {
        code = builder.code
        message = builder.message
        channel = builder.channel
    }

    inner class Builder {
        var code: String? = null
        var message: String? = null
        var channel: String? = null

        fun code(value: String): Builder {
            code = value
            return this
        }

        fun message(value: String): Builder {
            message = value
            return this
        }

        fun channel(value: String): Builder {
            channel = value
            return this
        }

        fun build(): ErrorResponse {
            return ErrorResponse(this)
        }
    }

    companion object {
        const val ERROR_CODE_SEC00401 = "SEC00401"
        const val ERROR_CODE_SEC00403 = "SEC00403"
        const val ERROR_CODE_SEC00404 = "SEC00404"
    }
}