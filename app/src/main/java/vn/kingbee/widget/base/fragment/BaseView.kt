package vn.kingbee.widget.base.fragment

import androidx.annotation.StringRes

interface BaseView : CommonView {
    fun viewLoaded()

    fun showProgressDialog()

    fun showProgressDialog(@StringRes stringResId: Int)

    fun showProgressDialog(message: String)

    fun hideProgressDialog()

    fun showToastMessageUnderHandle(message: String)

    fun showToastUnderConstruction()

    fun showToastNoInternetError()

    fun showFullScreenInBlackNoInternetError()

    fun showFullScreenInWhiteNoInternetError()

    fun vibrate()

    fun goToNextScreen()

    fun goToPreviousScreen()

    fun showToastConnectionTimeout()

    fun showFullScreenInBlackConnectionTimeoutError()

    fun showFullScreenInWhiteConnectionTimeoutError()
}