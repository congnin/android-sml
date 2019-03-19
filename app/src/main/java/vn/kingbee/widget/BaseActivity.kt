package vn.kingbee.widget

import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.view.MotionEvent
import android.widget.EditText
import vn.kingbee.widget.utils.CommonUtils
import vn.kingbee.widget.utils.FontHelper

abstract class BaseActivity : AppCompatActivity() {

    override fun dispatchTouchEvent(event: MotionEvent): Boolean {
        if (event.action == 0 && this.currentFocus != null) {
            val v = this.currentFocus
            val beforeDispatch = CommonUtils.isKeyboardShown(v!!.rootView)
            val ret = super.dispatchTouchEvent(event)
            if (v is EditText) {
                val isAfterDispatch = CommonUtils.isKeyboardShown(v.rootView)
                val isTouchInsideEditTextField = CommonUtils.isTouchInsideEditText(event, v)
                if (event.action == 0 && isAfterDispatch && beforeDispatch && !isTouchInsideEditTextField) {
                    CommonUtils.hideKeyboard(this, this.window.currentFocus)
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
}