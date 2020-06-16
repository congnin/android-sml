package vn.kingbee.domain.core.retry

import vn.kingbee.domain.core.exception.BaseException

class DefaultRetryCondition : RetryCondition {

    override fun isRetry(exception: BaseException): Boolean {
        return false
    }
}