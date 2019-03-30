package vn.kingbee.widget.utils

class StringUtils {
    companion object {
        fun emptyIfNull(s: String?): String {
            return s ?: ""
        }
    }
}