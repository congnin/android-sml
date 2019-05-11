package vn.kingbee.domain.core.retry

import io.reactivex.Observable
import io.reactivex.functions.Function
import vn.kingbee.domain.core.exception.BaseException
import java.util.concurrent.TimeUnit

class RetryWithDelay(private val maxRetries: Int,
                     private val mRetryDelaySeconds: Int,
                     private val mCondition: RetryCondition) :
    Function<Observable<out Throwable>, Observable<*>> {
    private var mRetryCount: Int = 0

    val isRetryAble: Boolean
        get() = maxRetries > 0 && mRetryCount < maxRetries - 1

    init {
        this.mRetryCount = 0
    }

    override fun apply(attempts: Observable<out Throwable>): Observable<*> {
        return attempts.flatMap { throwable ->
            if (throwable is BaseException && mCondition.isRetry(throwable) && ++mRetryCount < maxRetries) {
                Observable.timer(mRetryDelaySeconds.toLong(), TimeUnit.SECONDS)
            } else Observable.error<Any>(throwable)
        }
    }
}