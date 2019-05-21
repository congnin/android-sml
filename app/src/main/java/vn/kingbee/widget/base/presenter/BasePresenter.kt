package vn.kingbee.widget.base.presenter

import io.reactivex.disposables.Disposable
import vn.kingbee.widget.base.fragment.BaseView

interface BasePresenter<T : BaseView> {

    fun updateUI()

    fun onScreenVisible()

    fun onScreenInvisible()

    fun resume()

    fun pause()

    fun destroy()

    fun onDestroyAllSubscriptions()

    fun onAddOneSubscription(s: Disposable)
}