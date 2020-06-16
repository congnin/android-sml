package vn.kingbee.domain.ektp.entity

class EKTPDataAndPhotoResponse {

    var rawCode: String? = null
    var payload: Array<String>? = null
    var statusCode: StatusCode? = null

    fun getProvince(): String? = payload?.get(0)

    fun getTown(): String? = payload?.get(1)

    fun getEKtpNumber(): String? = payload?.get(2)

    fun getName(): String? = payload?.get(3)

    fun getPlaceOfBirth(): String? = payload?.get(4)

    fun getDateOfBirth(): String? = payload?.get(5)

    fun getGender(): String? = payload?.get(6)

    fun getBloodType(): String? = payload?.get(7)

    fun getAddress(): String? = payload?.get(8)

    fun getRtrw(): String? = payload?.get(9)

    fun getKelDesa(): String? = payload?.get(10)

    fun getKecamatan(): String? = payload?.get(11)

    fun getReligion(): String? = payload?.get(12)

    fun getMaritalStatus(): String? = payload?.get(13)

    fun getOccupation(): String? = payload?.get(14)

    fun getNationality(): String? = payload?.get(15)

    fun getExpiryDate(): String? = payload?.get(16)

    fun getPhoto(): String? = payload?.get(17)

    companion object {
        const val CODE_SUCCESS = "9000"
        const val CODE_CARD_NOT_VERIFIED = "0xC106"
    }

    enum class StatusCode private constructor(val message: String) {
        SUCCESS("Read bio data successfully"),
        CARD_NOT_VERIFIED("C106 - Card not verified"),
        ERROR_INVALID_DOCUMENT("01C101 - CARD NOT PRESENT. Cannot read fingerprint index data."),
        ERROR_DEVICE_BROKEN("01C105 - SAM INIT FAIL. Cannot read fingerprint index data."),
        FAIL_TO_READ_CARD("016F00 - FAIL TO READ CARD. Cannot read fingerprint index data."),
        ERROR_TIME_OUT("Timeout error"),
        ERROR_UNKNOWN("Unknown error")
    }
}