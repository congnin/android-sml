package vn.kingbee.domain.core.usecase

import io.reactivex.disposables.Disposable

class NonSubscription : Disposable {
    override fun dispose() {
    }

    override fun isDisposed(): Boolean {
        return false
    }
}