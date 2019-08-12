package vn.kingbee.feature.base.presenter

import vn.kingbee.feature.base.fragment.BaseFragmentBehavior

interface IBasePresenter<V> {
    fun setView(view: V)

    fun getView(): V?

    fun resume()

    fun pause()

    fun destroy()

    fun updateUI()

    fun destroy(viewHashCode: Int)
}