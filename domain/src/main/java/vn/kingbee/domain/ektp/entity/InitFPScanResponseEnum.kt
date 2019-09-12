package vn.kingbee.domain.ektp.entity

enum class InitFPScanResponseEnum private constructor(val code: String, val message: String) {
    SUCCESS("009000", "Fingerprint matched"),
    FAIL("!000000", "Fingerprint miss match"),

    TRIAL_REACH_MAX("0x63C0", "Trial Reach Max"),
    INCORRECT_TRIAL_NUMBER("0x6A8y", "Incorrect trial number, correct value is y"),
    INCORRECT_FINGERPRINT_INDEX("0x6A9y", "Incorrect fingerprint index, correct value is y")
}
