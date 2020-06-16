package vn.kingbee.achilles.service

import vn.kingbee.achilles.service.callback.MockInterceptorCallback
import vn.kingbee.achilles.service.callback.MockUiServiceCallback
import vn.kingbee.achilles.service.callback.MockUserInteractCallback
import vn.kingbee.achilles.service.callback.UserInteractCallback

interface MockContractor : MockInterceptorCallback, MockUiServiceCallback {
    fun setInterceptorListener(interceptor: MockUserInteractCallback)

    fun registerUserInteractListener(userInteractListener: UserInteractCallback)
}