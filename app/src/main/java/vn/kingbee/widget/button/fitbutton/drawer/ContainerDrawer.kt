package vn.kingbee.widget.button.fitbutton.drawer

import android.graphics.drawable.GradientDrawable
import android.view.View
import android.widget.LinearLayout
import vn.kingbee.utils.CommonUtils.Companion.pxToDp
import vn.kingbee.widget.button.fitbutton.FitButton
import vn.kingbee.widget.button.fitbutton.model.FButton
import vn.kingbee.widget.button.fitbutton.model.IconPosition
import vn.kingbee.widget.button.fitbutton.model.Shape
import vn.kingbee.widget.button.fitbutton.util.RippleEffect

internal class ContainerDrawer(val view: FitButton, val button: FButton) :
    Drawer<FitButton, FButton>(view, button) {
    private lateinit var container: GradientDrawable

    override fun draw() {
        initContainer()
        setOrientation()
        drawShape()
    }

    override fun isReady(): Boolean {
        return view.visibility != View.GONE
    }

    private fun initContainer() {
        container = GradientDrawable()
        container.cornerRadius = pxToDp(button.cornerRadius)
        container.setColor(button.btnColor)
        container.setStroke(button.borderWidth.toInt(), button.borderColor)
        addRipple()
    }

    // Add a ripple effect for the button if it was enabled
    private fun addRipple() {
        view.isEnabled = button.enable
        view.isClickable = button.enable
        view.isFocusable = button.enable
        RippleEffect.createRipple(
            view,
            button.enableRipple && button.enable,
            button.btnColor,
            button.rippleColor,
            button.cornerRadius,
            button.btnShape,
            container
        )
    }

    // Set the layout orientation dependent on icon position
    private fun setOrientation() {
        view.orientation = when (button.iconPosition) {
            IconPosition.LEFT, IconPosition.RIGHT -> LinearLayout.HORIZONTAL
            else -> LinearLayout.VERTICAL
        }
    }

    // Draw button shape
    private fun drawShape() {
        container.shape = when (button.btnShape) {
            Shape.RECTANGLE -> GradientDrawable.RECTANGLE
            Shape.OVAL -> GradientDrawable.OVAL
            Shape.SQUARE -> alignSides(GradientDrawable.RECTANGLE)
            Shape.CIRCLE -> alignSides(GradientDrawable.OVAL)
        }
    }

    // Align shape sides
    private fun alignSides(shape: Int): Int {
        val dimension = if (view.layoutParams != null) {
            defineFitSide(view.layoutParams.width, view.layoutParams.height)
        } else {
            defineFitSide(view.measuredWidth, view.measuredHeight)
        }
        if (view.layoutParams != null) {
            view.layoutParams.width = dimension
            view.layoutParams.height = dimension
        }
        return shape
    }

    // Get a min side or a max side if anyone side equal zero or less
    private fun defineFitSide(w: Int, h: Int): Int {
        return if (w <= 0 || h <= 0) {
            Math.max(w, h)
        } else {
            Math.min(w, h)
        }
    }
}