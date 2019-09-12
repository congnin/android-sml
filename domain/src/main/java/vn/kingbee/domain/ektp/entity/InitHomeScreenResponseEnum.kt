package vn.kingbee.domain.ektp.entity

enum class InitHomeScreenResponseEnum private constructor(val code: String, val message: String) {
    SUCCESS("9000", "Init HomeScreen Action Success"),
    FAIL("!9000", "Init HomeScreen Action Fail")
}
