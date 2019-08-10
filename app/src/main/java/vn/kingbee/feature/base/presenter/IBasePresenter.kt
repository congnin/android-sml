package vn.kingbee.feature.base.presenter

interface IBasePresenter<ViewType> {
    fun setView(view: ViewType)

    fun getView(): ViewType?

    fun resume()

    fun pause()

    fun destroy()

    fun updateUI()

    fun destroy(viewHashCode: Int)
}