package vn.kingbee.utils

object StringUtils {
    fun emptyIfNull(s: String?): String {
        return s ?: ""
    }

    fun match(value: String?, keyword: String?): Boolean {
        if (value == null || keyword == null)
            return false
        if (keyword.length > value.length)
            return false

        var i = 0
        var j = 0
        do {
            if (keyword[j] == value[i]) {
                i++
                j++
            } else if (j > 0)
                break
            else
                i++
        } while (i < value.length && j < keyword.length)

        return j == keyword.length
    }
}