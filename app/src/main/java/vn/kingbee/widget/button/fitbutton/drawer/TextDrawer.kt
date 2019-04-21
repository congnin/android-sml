package vn.kingbee.widget.button.fitbutton.drawer

import android.graphics.Typeface
import android.support.v4.content.res.ResourcesCompat
import android.text.TextUtils
import android.view.View
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.LinearLayout
import android.widget.TextView
import timber.log.Timber
import vn.kingbee.utils.CommonUtils.Companion.getDensity
import vn.kingbee.widget.button.fitbutton.FitButton
import vn.kingbee.widget.button.fitbutton.model.FButton

internal class TextDrawer(val view: FitButton, val button: FButton)
    : Drawer<FitButton, FButton>(view, button) {

    private lateinit var tv: TextView

    override fun draw() {
        initText()
        view.addView(tv)
    }

    override fun isReady(): Boolean {
        return !TextUtils.isEmpty(button.text) && button.textVisibility != View.GONE
    }

    private fun initText() {
        tv = TextView(view.context)
        tv.layoutParams = LinearLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT)
        tv.text = button.text
        tv.includeFontPadding = false
        tv.textSize = button.textSize / getDensity()
        tv.setTextColor(button.textColor)
        tv.isAllCaps = button.textAllCaps
        tv.visibility = button.textVisibility
        tv.setPadding(button.textPaddingStart.toInt(), button.textPaddingTop.toInt(),
            button.textPaddingEnd.toInt(), button.textPaddingBottom.toInt())
        setTypeface()
    }

    private fun setTypeface() {
        tv.setTypeface(Typeface.DEFAULT, button.textStyle)
        if (button.textFont != null) {
            tv.setTypeface(button.textFont, button.textStyle)
        } else {
            if (button.fontRes != 0) {
                try {
                    val tf = ResourcesCompat.getFont(view.context, button.fontRes)
                    tv.setTypeface(tf, button.textStyle)
                    button.textFont = tf
                } catch (e: Exception) {
                    Timber.e(TextDrawer::class.java.simpleName, "Incorrect font resource")
                }
            }
        }
    }

}