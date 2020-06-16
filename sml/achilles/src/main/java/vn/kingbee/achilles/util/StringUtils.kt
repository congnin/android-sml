package vn.kingbee.achilles.util

object StringUtils {

    fun isEmpty(str: String?): Boolean {
        return str == null || str.isEmpty()
    }

    fun isEmpty(str: StringBuilder?): Boolean {
        return str == null || str.isEmpty()
    }
}