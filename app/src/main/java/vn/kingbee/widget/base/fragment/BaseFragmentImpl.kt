package vn.kingbee.widget.base.fragment

import android.app.Dialog
import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.View
import butterknife.ButterKnife
import butterknife.Unbinder
import vn.kingbee.widget.dialog.ButtonType
import vn.kingbee.widget.dialog.DialogClickedListener
import vn.kingbee.widget.dialog.loading.LoadingDialogMaterial
import android.view.ViewGroup
import android.view.LayoutInflater
import android.widget.Toast
import vn.kingbee.widget.R
import androidx.annotation.LayoutRes
import vn.kingbee.utils.CommonUtils
import vn.kingbee.widget.base.presenter.BasePresenter

abstract class BaseFragmentImpl : androidx.fragment.app.Fragment(), BaseView, DialogClickedListener {

    protected lateinit var handler: Handler
    protected var loadingDialog: LoadingDialogMaterial? = null
    private lateinit var mUnbinder: Unbinder

    protected abstract fun getPresenter(): BasePresenter<*>?

    fun getTagName(): String? = this.tag

    fun getTagFromClassName(): String? = this.javaClass.simpleName

    fun getInstance(): androidx.fragment.app.Fragment = this

    fun needIdleTimeout(): Boolean = true

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        this.handler = Handler()
        this.mUnbinder = ButterKnife.bind(this, view)
        this.setUpActionBar()
    }


    @Deprecated("use {@link #getLayoutContentView()} instead")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val layoutResId = getLayoutContentView()
        return if (layoutResId != NO_LAYOUT) {
            inflater.inflate(layoutResId, container, false)
        } else super.onCreateView(inflater, container, savedInstanceState)
    }

    @LayoutRes
    protected abstract fun getLayoutContentView(): Int

    override fun viewLoaded() {
        //nothing
    }

    protected fun setUpActionBar() {
        //nothing
    }

    override fun onResume() {
        super.onResume()
        if (getPresenter() != null) {
            getPresenter()?.resume()
        }
    }

    override fun onPause() {
        super.onPause()
        if (getPresenter() != null) {
            getPresenter()?.pause()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (getPresenter() != null) {
            getPresenter()?.pause()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mUnbinder.unbind()
        handler.removeCallbacksAndMessages(null)
        this.loadingDialog = null
    }

    override fun showToastMessageUnderHandle(message: String) {
        Toast.makeText(activity, String.format(getString(R.string.TEXT_UNDER_HANDLE), message),
            Toast.LENGTH_SHORT).show()
    }

    override fun showToastUnderConstruction() {
        Toast.makeText(activity, R.string.SAMPLE_TEXT, Toast.LENGTH_SHORT).show()
    }

    override fun showProgressDialog() {
        showProgressDialog("")
    }

    override fun showProgressDialog(stringResId: Int) {
        showProgressDialog(getString(stringResId))
    }

    override fun showProgressDialog(message: String) {
        if (loadingDialog != null && loadingDialog?.getDialog() != null && loadingDialog?.getDialog()?.isShowing!!) {
            return
        }
        if (activity != null) {
            loadingDialog = LoadingDialogMaterial(activity!!, null)
            loadingDialog?.setMessage(message)
            loadingDialog?.getDialog()?.show()
        }
    }

    override fun hideProgressDialog() {
        if (this.loadingDialog == null) {
            return
        }
        this.loadingDialog?.getDialog()?.dismiss()
    }

    override fun showToastNoInternetError() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun showFullScreenInBlackNoInternetError() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun showFullScreenInWhiteNoInternetError() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun vibrate() {
        if (activity != null) {
            CommonUtils.vibrate(activity!!)
        }
    }

    override fun goToNextScreen() {

    }

    override fun goToPreviousScreen() {

    }

    override fun showToastConnectionTimeout() {

    }

    override fun showFullScreenInBlackConnectionTimeoutError() {

    }

    override fun showFullScreenInWhiteConnectionTimeoutError() {

    }

    override fun showServerGenericError(errorCode: String) {

    }

    override fun showSessionTokenExpiredError() {

    }

    override fun onDialogClicked(dialog: Dialog, buttonType: ButtonType): Boolean {
        return false
    }

    companion object {
        private const val NO_LAYOUT = -1
    }
}