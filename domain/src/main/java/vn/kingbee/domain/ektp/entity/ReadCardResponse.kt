package vn.kingbee.domain.ektp.entity

class ReadCardResponse {

    var rawCode: String? = null
    var primaryFp: Int? = null
    var secondaryFp: Int? = null
    var statusCode: StatusCode? = null

    enum class StatusCode private constructor(val message: String) {
        SUCCESS("Read card successfully"),
        ERROR_INVALID_DOCUMENT("01C101 - CARD NOT PRESENT. Cannot read fingerprint index data."),
        ERROR_DEVICE_BROKEN("01C105 - SAM INIT FAIL. Cannot read fingerprint index data."),
        FAIL_TO_READ_CARD("016F00 - FAIL TO READ CARD. Cannot read fingerprint index data."),
        ERROR_TIME_OUT("Timeout error"),
        ERROR_UNKNOWN("Unknown error"),
        FINGER_PRINT_INDEX_INVALID("Finger print index is out of range [1->10]")
    }

    companion object {
        const val CODE_SUCCESS = "9000"
    }
}