package vn.kingbee.widget.spinner

enum class PaymentNotificationType(var value: String) {
    CELLPHONE("cellphone"),
    EMAIL("email"),
    CELLPHONE_EMAIL("cellphone email"),
    UNKNOWN("UNKNOWN");

    fun getFromValue(value: String?): PaymentNotificationType {
        if (value == null) {
            return UNKNOWN
        }
        for (h in PaymentNotificationType.values()) {
            if (h.value == value) {
                return h
            }
        }
        return UNKNOWN
    }
}