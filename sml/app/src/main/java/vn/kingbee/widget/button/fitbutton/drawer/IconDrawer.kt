package vn.kingbee.widget.button.fitbutton.drawer

import android.view.View
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.ImageView
import android.widget.LinearLayout
import vn.kingbee.widget.button.fitbutton.FitButton
import vn.kingbee.widget.button.fitbutton.model.FButton

internal class IconDrawer(val view: FitButton, val button: FButton) :
    Drawer<FitButton, FButton>(view, button) {

    private lateinit var iv: ImageView

    override fun draw() {
        initIcon()
        view.addView(iv)
    }

    override fun isReady(): Boolean {
        return button.icon != null && button.iconVisibility != View.GONE
    }

    // Prepare icon to the drawing
    private fun initIcon(): ImageView {
        iv = ImageView(view.context)
        iv.setImageDrawable(button.icon)
        iv.setColorFilter(button.iconColor)
        iv.visibility = button.iconVisibility
        val iconParams = LinearLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT)
        iconParams.width = button.iconWidth.toInt()
        iconParams.height = button.iconHeight.toInt()
        iconParams.marginStart = button.iconMarginStart.toInt()
        iconParams.topMargin = button.iconMarginTop.toInt()
        iconParams.marginEnd = button.iconMarginEnd.toInt()
        iconParams.bottomMargin = button.iconMarginBottom.toInt()
        iv.layoutParams = iconParams
        return iv
    }

}