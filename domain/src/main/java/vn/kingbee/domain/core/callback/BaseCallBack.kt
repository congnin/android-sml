package vn.kingbee.domain.core.callback

import vn.kingbee.domain.core.exception.NetworkException
import vn.kingbee.domain.core.exception.TechnicalErrorException

interface BaseCallBack<T> {
    fun onError(e: Throwable)

    fun onSuccess(info: T)

    fun onNetworkException(e: NetworkException)

    fun onTechnicalException(e: TechnicalErrorException)

    fun onSessionTokenExpiredException()

    fun onFinish(isError: Boolean)

    fun onStart()
}