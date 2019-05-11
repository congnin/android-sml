package vn.kingbee.domain.core.usecase

import vn.kingbee.domain.core.callback.BaseCallBack

interface UseCaseCallback<T, R : BaseCallBack<T>> {
    fun handleOnNext(data: T, callback: R)

    fun handleOnError(throwable: Throwable, callback: R)

    fun onFinish(callback: R, isError: Boolean)

    fun onStart(callBack: R)
}