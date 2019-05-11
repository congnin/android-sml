package vn.kingbee.domain.core.retry

import vn.kingbee.domain.core.exception.BaseException

interface RetryCondition {
    fun isRetry(exception: BaseException): Boolean
}