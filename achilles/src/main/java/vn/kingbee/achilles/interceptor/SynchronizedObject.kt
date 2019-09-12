package vn.kingbee.achilles.interceptor

import java.util.*

class SynchronizedObject {
    var sessionId: String? = null
    var responseApiName: String? = null
    var isUserInteract: Boolean = false

    fun resetSession() {
        this.sessionId = UUID.randomUUID().toString()
        this.isUserInteract = false
        this.responseApiName = null
    }
}