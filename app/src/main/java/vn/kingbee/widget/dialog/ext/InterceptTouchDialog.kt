package vn.kingbee.widget.dialog.ext

import android.app.Activity
import android.app.Dialog
import android.graphics.Rect
import android.view.MotionEvent
import android.view.View
import android.view.Window
import android.widget.EditText
import androidx.annotation.NonNull
import vn.kingbee.utils.UIUtils

class InterceptTouchDialog(activity: Activity, theme: Int) : Dialog(activity, theme) {

    private val mScreenSize: IntArray = UIUtils.getScreenSize(activity)

    override fun dispatchTouchEvent(@NonNull ev: MotionEvent): Boolean {
        return if (handleTouchOnDialog(ev, window)) {
            true
        } else {
            super.dispatchTouchEvent(ev)
        }
    }

    private fun handleTouchOnDialog(event: MotionEvent, window: Window?): Boolean {
        if (event.action == MotionEvent.ACTION_DOWN && window != null && window.currentFocus != null) {
            val v = window.currentFocus
            // check keyboard is shown before dispatch touch event
            val beforeDispatch = isKeyboardShownOnDialog(v!!.rootView)
            if (v is EditText) {
                // check keyboard is shown after dispatch touch event
                val isAfterDispatch = isKeyboardShownOnDialog(v.rootView)
                val isTouchInsideEditTextField = UIUtils.isTouchInsideEditText(event, v)
                if (event.action == MotionEvent.ACTION_DOWN && isAfterDispatch &&
                    beforeDispatch && !isTouchInsideEditTextField) {
                    UIUtils.hideKeyboard(ownerActivity!!, v)
                }
            }
            return false
        }
        return false
    }

    private fun isKeyboardShownOnDialog(v: View): Boolean {
        if (window != null) {
            val viewHeight = window!!.decorView.height
            //for case view content height < screen height.
            val diff = mScreenSize[1] - viewHeight
            if (diff > 0) {
                val r = Rect()
                v.getWindowVisibleDisplayFrame(r)
                val dm = v.resources.displayMetrics
                val heightDiff = v.bottom - r.bottom + diff
                return heightDiff.toFloat() > KEYBOARD_HEIGHT * dm.density
            }
        }
        return UIUtils.isKeyboardShown(v)
    }

    companion object {

        private const val KEYBOARD_HEIGHT = 128.0f
    }
}