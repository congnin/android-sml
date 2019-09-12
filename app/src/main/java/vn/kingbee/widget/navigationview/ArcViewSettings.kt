package vn.kingbee.widget.navigationview

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import vn.kingbee.utils.CommonUtils
import vn.kingbee.widget.R

class ArcViewSettings {
    var cropInside = true
    var arcWidth: Float = 0.toFloat()
    var elevation: Float = 0.toFloat()
    var backgroundDrawable: Drawable = ColorDrawable(Color.WHITE)

    constructor(context: Context, attrs: AttributeSet?) {
        val styledAttributes = context.obtainStyledAttributes(attrs, R.styleable.ArcDrawer, 0, 0)
        arcWidth = styledAttributes.getDimension(R.styleable.ArcDrawer_arc_width, CommonUtils.dpToPx(context, 10))

        val cropDirection = styledAttributes.getInt(R.styleable.ArcDrawer_arc_cropDirection, CROP_INSIDE)
        cropInside = (cropDirection == CROP_INSIDE)

        val attrsArray = intArrayOf(android.R.attr.background, android.R.attr.layout_gravity)

        val androidAttrs = context.obtainStyledAttributes(attrs, attrsArray)
        backgroundDrawable = androidAttrs.getDrawable(0)

        androidAttrs.recycle()
        styledAttributes.recycle()
    }

    companion object {
        const val CROP_INSIDE = 0
        const val CROP_OUTSIDE = 1
    }
}