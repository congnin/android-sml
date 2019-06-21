package vn.kingbee.achilles.service.callback

interface MockInterceptorCallback {
    val isUserInteractEnable: Boolean

    fun notifyUserStartInteract(sessionId: String)

    fun notifyUserEndInteract(selectedApiName: String, sessionId: String)
}