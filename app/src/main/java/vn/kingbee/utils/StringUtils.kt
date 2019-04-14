package vn.kingbee.utils

class StringUtils {
    companion object {
        fun emptyIfNull(s: String?): String {
            return s ?: ""
        }
    }
}