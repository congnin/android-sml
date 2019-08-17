package vn.kingbee.feature.base.presenter

interface IBasePresenter<V> {
    fun setView(view: V)

    fun getView(): V?

    fun resume()

    fun pause()

    fun destroy()

    fun updateUI()

    fun destroy(viewHashCode: Int)
}