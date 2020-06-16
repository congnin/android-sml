package vn.kingbee.achilles.service.callback

import vn.kingbee.achilles.service.model.ApiSession

interface MockUserInteractCallback {
    fun onUserStartInteract(sessionId: String)

    @Deprecated("")
    fun onUserEndInteract(responseApiName: String, sessionId: String)

    fun onUserEndInteract(apiSession: ApiSession)
}