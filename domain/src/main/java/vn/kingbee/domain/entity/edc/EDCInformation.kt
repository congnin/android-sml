package vn.kingbee.domain.entity.edc

import org.apache.commons.lang3.StringUtils

class EDCInformation {
    var edcIP: String? = null
    var edcPort: Int = 0
    var edcTimeout: Int = 0
    var iv: ByteArray? = null
    var sk: ByteArray? = null
    var securitySuffix: String? = null
    var location: String? = null
    var terminalId: String? = null

    companion object {
        fun convertToBytes(byteString: String): ByteArray {
            val strings = StringUtils.split(byteString, ",")
            val results = ByteArray(strings.size)
            for (index in strings.indices) {
                results[index] = java.lang.Byte.decode(strings[index])
            }
            return results
        }
    }
}