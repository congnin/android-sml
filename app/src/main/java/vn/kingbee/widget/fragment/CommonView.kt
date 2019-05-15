package vn.kingbee.widget.fragment

interface CommonView {
    fun showServerGenericError(errorCode: String)

    fun showSessionTokenExpiredError()
}