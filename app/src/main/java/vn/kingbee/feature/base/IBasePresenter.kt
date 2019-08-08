package vn.kingbee.feature.base

interface IBasePresenter<ViewType> {
    fun setView(view: ViewType)

    fun getView(): ViewType?

    fun resume()

    fun pause()

    fun destroy()

    fun updateUI()

    fun destroy(viewHashCode: Long)
}