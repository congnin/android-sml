package vn.kingbee.domain.core.usecase

import io.reactivex.Scheduler
import io.reactivex.disposables.Disposable
import io.reactivex.observers.DisposableObserver
import vn.kingbee.domain.core.callback.BaseCallBack
import vn.kingbee.domain.core.retry.RetryCondition

interface UseCase<T, R : BaseCallBack<T>> {

    fun buildUseCase(headers: Map<String, String>): UseCase<T, R>

    fun subscribeOn(scheduler: Scheduler): UseCase<T, R>

    fun observeOn(scheduler: Scheduler): UseCase<T, R>

    fun executeBySubscriber(subscriber: DisposableObserver<T>): Disposable

    fun executeByCallBack(callBack: R): Disposable

    fun retry(condition: RetryCondition, seconds: Int, retry: Int): UseCase<T, R>
}