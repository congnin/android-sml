package vn.kingbee.achilles.service.callback

interface MockUiServiceCallback {
    fun notifyPrepareApi(apiFileName: String, sessionId: String, listResult: Array<String>)

    fun notifyApiStarted(sessionId: String)
}