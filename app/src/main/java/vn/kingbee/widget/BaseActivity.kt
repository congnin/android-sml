package vn.kingbee.widget

import android.content.Context
import android.os.Bundle
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import android.view.MotionEvent
import android.widget.EditText
import vn.kingbee.utils.CommonUtils
import vn.kingbee.utils.FontHelper
import vn.kingbee.widget.dialog.loading.LoadingDialogMaterial

abstract class BaseActivity : AppCompatActivity() {

    protected var loadingDialog: LoadingDialogMaterial? = null

    override fun dispatchTouchEvent(event: MotionEvent): Boolean {
        if (event.action == 0 && this.currentFocus != null) {
            val v = this.currentFocus
            val beforeDispatch = CommonUtils.isKeyboardShown(v!!.rootView)
            val ret = super.dispatchTouchEvent(event)
            if (v is EditText) {
                val isAfterDispatch = CommonUtils.isKeyboardShown(v.rootView)
                val isTouchInsideEditTextField = CommonUtils.isTouchInsideEditText(event, v)
                if (event.action == 0 && isAfterDispatch && beforeDispatch && !isTouchInsideEditTextField) {
                    CommonUtils.hideKeyboard(this, this.window.currentFocus!!)
                    v.clearFocus()
                } else if (event.action == 0 && !isTouchInsideEditTextField) {
                    hideCustomKeyboard(event)
                }
            }

            return ret
        } else {
            return super.dispatchTouchEvent(event)
        }
    }

    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(FontHelper.attachBaseContext(newBase!!))
    }

    open fun hideCustomKeyboard(event: MotionEvent) {

    }

    fun showProgressDialog() {
        showProgressDialog("")
    }

    fun showProgressDialog(@StringRes stringResId: Int) {
        showProgressDialog(getString(stringResId))
    }

    fun showProgressDialog(message: String) {
        if (loadingDialog != null && loadingDialog?.getDialog() != null && loadingDialog?.getDialog()!!.isShowing) {
            return
        }
        loadingDialog = LoadingDialogMaterial(this, null)
        loadingDialog?.setMessage(message)
        loadingDialog?.getDialog()?.show()
    }

    fun hideProgressDialog() {
        if (this.loadingDialog == null) {
            return
        }
        this.loadingDialog?.getDialog()?.dismiss()
    }
}