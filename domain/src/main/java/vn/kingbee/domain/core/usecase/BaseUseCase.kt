package vn.kingbee.domain.core.usecase

import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.disposables.Disposable
import io.reactivex.observers.DisposableObserver
import vn.kingbee.domain.core.callback.BaseCallBack
import vn.kingbee.domain.core.exception.BaseException
import vn.kingbee.domain.core.exception.NetworkException
import vn.kingbee.domain.core.exception.SessionTokenExpiredException
import vn.kingbee.domain.core.exception.TechnicalErrorException
import vn.kingbee.domain.core.retry.DefaultRetryCondition
import vn.kingbee.domain.core.retry.RetryCondition
import vn.kingbee.domain.core.retry.RetryWithDelay
import vn.kingbee.domain.core.scheduler.AppScheduler

abstract class BaseUseCase<T, R : BaseCallBack<T>> : UseCase<T, R>, UseCaseCallback<T, R> {

    var observable: Observable<T>? = null
        protected set
    private var mSubscribeOn = AppScheduler.io()
    private var mObserveOn = AppScheduler.mainThread()
    private var mDelay = RetryWithDelay(0, 0, DefaultRetryCondition())

    val isRetryAble: Boolean
        get() = mDelay.isRetryAble

    override fun subscribeOn(scheduler: Scheduler): UseCase<T, R> {
        mSubscribeOn = scheduler
        return this
    }

    override fun observeOn(scheduler: Scheduler): UseCase<T, R> {
        mObserveOn = scheduler
        return this
    }

    override fun buildUseCase(headers: Map<String, String>): UseCase<T, R> {
        return this
    }

    override fun executeBySubscriber(subscriber: DisposableObserver<T>): Disposable {
        return observable!!.retryWhen(mDelay).observeOn(mObserveOn).subscribeOn(mSubscribeOn)
            .subscribeWith(subscriber)
    }

    override fun executeByCallBack(callBack: R): Disposable {
        return observable!!.retryWhen(mDelay).observeOn(mObserveOn).subscribeOn(mSubscribeOn)
            .subscribeWith(BaseUseCaseSubscriber(callBack, this))
    }

    override fun onStart(callBack: R) {
        callBack.onStart()
    }

    override fun onFinish(callback: R, isError: Boolean) {
        callback.onFinish(isError)
    }

    override fun handleOnError(throwable: Throwable, callback: R) {
        try {
            if (!handleResponseException(throwable, callback)) {
                handleGenericException(throwable, callback)
            }
        } catch (exception: Exception) {
            handleGenericException(throwable, callback)
        }

    }

    protected fun handleGenericException(e: Throwable, callBack: R) {
        if (e is NetworkException) {
            callBack.onNetworkException(e)
        } else if (e is TechnicalErrorException) {
            callBack.onTechnicalException(e)
        } else if (e is SessionTokenExpiredException) {
            callBack.onSessionTokenExpiredException()
        } else {
            callBack.onError(e)
        }
    }

    override fun handleOnNext(data: T, callback: R) {
        callback.onSuccess(data)
    }

    override fun retry(condition: RetryCondition, seconds: Int, retry: Int): UseCase<T, R> {
        mDelay = RetryWithDelay(retry, seconds, condition)
        return this
    }

    /**
     * get code from exception.
     *
     * @param e
     * @return
     */
    protected fun getErrorCodeFromException(e: Any): String {
        return if (e is BaseException) {
            e.code
        } else ""
    }

    protected abstract fun handleResponseException(e: Throwable, callBack: R): Boolean
}