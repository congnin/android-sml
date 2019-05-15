package vn.kingbee.widget.fragment

import android.app.Dialog
import android.os.Bundle
import android.os.Handler
import android.support.v4.app.Fragment
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
import android.support.annotation.LayoutRes


abstract class BaseFragmentImpl : Fragment(), BaseView, DialogClickedListener {

    protected lateinit var handler: Handler
    protected var loadingDialogMaterial: LoadingDialogMaterial? = null
    private lateinit var mUnbinder: Unbinder

    fun getTagName(): String? = this.tag

    fun getTagFromClassName(): String? = this.javaClass.simpleName

    fun getInstance(): Fragment = this

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

    override fun onDestroyView() {
        super.onDestroyView()
        mUnbinder.unbind()
        handler.removeCallbacksAndMessages(null)
        this.loadingDialogMaterial = null
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

    }

    override fun showProgressDialog(message: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun hideProgressDialog() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
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
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun goToNextScreen() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun goToPreviousScreen() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun showToastConnectionTimeout() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun showFullScreenInBlackConnectionTimeoutError() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun showFullScreenInWhiteConnectionTimeoutError() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun showServerGenericError(errorCode: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun showSessionTokenExpiredError() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onDialogClicked(dialog: Dialog, buttonType: ButtonType): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    companion object {
        private const val NO_LAYOUT = -1
    }
}