package vn.kingbee.widget.base.presenter

interface Presenter<T> {
    fun setView(view: T)

    fun getView(): T

    fun resume()

    fun pause()

    fun destroy(viewHashCode: Long)

    fun updateUI()
}