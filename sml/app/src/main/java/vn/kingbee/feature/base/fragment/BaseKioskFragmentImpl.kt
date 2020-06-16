package vn.kingbee.feature.base.fragment

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.view.View
import androidx.fragment.app.Fragment
import butterknife.ButterKnife
import butterknife.Unbinder
import com.afollestad.materialdialogs.MaterialDialog
import vn.kingbee.feature.service.TimeoutProcessingService
import vn.kingbee.utils.UIUtils
import vn.kingbee.widget.R
import vn.kingbee.widget.dialog.big.timeout.ProgressCircleView
import javax.inject.Inject

abstract class BaseKioskFragmentImpl : Fragment(), BaseKioskFragment, BaseFragmentBehavior {
    lateinit var mHandler: Handler
    lateinit var unbinder: Unbinder
    protected var isVisibleToUser = false
    protected var mProgressDialog: Dialog? = null

    override fun getTagName(): String = if (tag != null) tag!! else ""

    override fun getTagFromClassName(): String = javaClass.simpleName

    override fun getInstance(): Fragment = this

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupFragmentComponent()
        setHasOptionsMenu(true)
        mHandler = Handler()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        unbinder = ButterKnife.bind(this, view)
        if (mProgressDialog == null) {
            mProgressDialog = getProgressDialog()
        }

        onScreenVisible()
    }

    override fun onResume() {
        super.onResume()
        mHandler.postDelayed({ UIUtils.hideKeyboard(this@BaseKioskFragmentImpl.activity!!) }, 100L)
    }

    override fun onPause() {
        super.onPause()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        unbinder.unbind()
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    override fun viewLoaded() {
        //do nothing
    }

    override fun showNetworkErrorDialog() {

    }

    override fun showTimeoutDialog() {

    }

    override fun showLoggedInTimeoutDialog() {

    }

    override fun getProgressDialog(): Dialog {
        val builder = MaterialDialog.Builder(context!!)
            .customView(R.layout.view_circle_loading, false)
        val materialDialog = builder.build()

        ProgressCircleView(materialDialog)
        materialDialog.setCancelable(false)
        return materialDialog
    }

    override fun showProgressDialog() {
        if (mProgressDialog != null) {
            mProgressDialog?.show()
        }
    }

    override fun hideProgressDialog() {
        if (mProgressDialog != null) {
            mProgressDialog?.hide()
        }
    }

    protected fun onScreenVisible() {
        //do nothing
    }

    abstract fun getTimeoutProcessingService(): TimeoutProcessingService

    protected fun startTimeoutService() {
        getTimeoutProcessingService().start()
    }

    protected fun stopTimeoutService() {
        getTimeoutProcessingService().stop()
    }
}