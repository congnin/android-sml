package vn.kingbee.widget.utils

import android.app.Activity
import android.content.Context
import android.content.res.Resources
import android.graphics.Rect
import android.os.SystemClock
import android.util.TypedValue
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText

const val EXPECT_DELAY_TIME_BETWEEN_CLICKS_DEFAULT: Long = 650
class CommonUtils {

    companion object {
        private var lastClickTime: Long = 0
        fun isClickAvailable(): Boolean {
            return isClickAvailable(EXPECT_DELAY_TIME_BETWEEN_CLICKS_DEFAULT)
        }

        fun isClickAvailable(expectDelayTimeBetweenClicks: Long): Boolean {
            val currentTime = SystemClock.elapsedRealtime()
            if (currentTime - lastClickTime > expectDelayTimeBetweenClicks) {
                lastClickTime = currentTime
                return true
            } else {
                return false
            }
        }

        fun hideKeyboard(activity: Activity?, view: View) {
            if (activity == null) {
                return
            }
            val inputMethodManager = activity.getSystemService(
                    Activity.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
        }

        fun showKeyboard(activity: Activity?, view: View) {
            if (activity == null) {
                return
            }
            view.requestFocus()
            val imm = activity.getSystemService(
                    Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
        }

        fun isKeyboardShown(rootView: View): Boolean {
            val r = Rect()
            rootView.getWindowVisibleDisplayFrame(r)
            val dm = rootView.resources.displayMetrics
            val heightDiff = rootView.bottom - r.bottom
            return heightDiff.toFloat() > 128.0f * dm.density
        }

        fun isTouchInsideEditText(event: MotionEvent, editText: EditText): Boolean {
            val xPoint = event.rawX
            val yPoint = event.rawY
            val l = IntArray(2)
            editText.getLocationOnScreen(l)
            val x = l[0]
            val y = l[1]
            val w = editText.width
            val h = editText.height
            return xPoint >= x.toFloat() && xPoint <= (x + w).toFloat() && yPoint >= y.toFloat() && yPoint <= (y + h).toFloat()
        }

        /**
         * Display Utils
         */
        fun dpFromPx(px: Float, context: Context): Float {
            return px / context.resources.displayMetrics.density
        }

        fun pxFromDp(dp: Float, context: Context): Float {
            return dp * context.resources.displayMetrics.density
        }

        fun dpToPx(res: Resources, dp: Int): Int {
            return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp.toFloat(), res.displayMetrics).toInt()
        }
    }
}
