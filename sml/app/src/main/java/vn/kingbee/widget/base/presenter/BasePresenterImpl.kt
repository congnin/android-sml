package vn.kingbee.widget.base.presenter

import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

class BasePresenterImpl {
    protected var subscriptions: CompositeDisposable? = null

    constructor()

    fun onAddOneSubscription(s: Disposable) {
        if (this.subscriptions == null) {
            this.subscriptions = CompositeDisposable()
        }

        this.subscriptions?.add(s)
    }

    fun onDestroyAllSubscriptions() {
        if (this.subscriptions != null) {
            subscriptions?.clear()
        }
    }

    fun resume() {
        //nothing
    }

    fun pause() {
        //nothing
    }

    fun destroy() {
        //nothing
    }

    fun onScreenVisible() {
        //nothing
    }

    fun onScreenInvisible() {
        //nothing
    }
}