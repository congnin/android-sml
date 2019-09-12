package vn.kingbee.widget.pin.square

import android.graphics.drawable.Drawable
import android.view.View
import android.widget.LinearLayout

interface PinItem {
    fun getPinItem(): View

    fun setLayout(layoutParams: LinearLayout.LayoutParams)

    fun setBackgroundDefault(color: Drawable)

    fun setBackground(color: Drawable)

    fun setFocusBackground(color: Drawable)

    fun setErrorColor(color: Drawable)

    fun setCircleSize(circleSize: Int)

    fun setTextSize(size: Int)

    fun setTextColor(color: Int)

    fun setTextBackgroundColor(textBackgroundColor: Drawable)

    fun setElevation(elevation: Int)

    fun setText(text: String)

    fun activeEmptyFocus()

    fun setErrorStatus()

    fun inActiveFillFocus()

    fun setDefaultStatus()
}
