package vn.kingbee.domain.core.usecase

import android.os.Handler

import io.reactivex.observers.DisposableObserver
import vn.kingbee.domain.core.callback.BaseCallBack
import vn.kingbee.domain.core.exception.NetworkException

class BaseUseCaseSubscriber<T, R : BaseCallBack<T>>(private val callBack: R,
                                                    private val useCaseCallback: UseCaseCallback<T, R>) :
    DisposableObserver<T>() {

    override fun onError(e: Throwable) {
        if (e is NetworkException && e.isInternetConnectionError) {
            Handler().postDelayed({
                useCaseCallback.handleOnError(e, callBack)
                useCaseCallback.onFinish(callBack, true)
            }, DELAY_TIME_FOR_NO_CONNECTION)
        } else {
            useCaseCallback.handleOnError(e, callBack)
            useCaseCallback.onFinish(callBack, true)
        }
    }

    override fun onComplete() {
        useCaseCallback.onFinish(callBack, false)
    }

    override fun onNext(t: T) {
        useCaseCallback.handleOnNext(t, callBack)
    }

    override fun onStart() {
        useCaseCallback.onStart(callBack)
    }

    companion object {
        // Use later
        private const val DELAY_TIME_FOR_NO_CONNECTION = 1000L
    }
}
