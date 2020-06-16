package vn.kingbee.widget.base.fragment

interface CommonView {
    fun showServerGenericError(errorCode: String)

    fun showSessionTokenExpiredError()
}