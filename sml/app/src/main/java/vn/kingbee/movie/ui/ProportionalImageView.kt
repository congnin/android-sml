package vn.kingbee.movie.ui

import android.content.Context
import android.util.AttributeSet
import android.widget.ImageView
import kotlin.math.roundToInt

class ProportionalImageView : ImageView {

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle)

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val width = MeasureSpec.getSize(widthMeasureSpec)
        val height = (width * ASPECT_RATIO).roundToInt()
        setMeasuredDimension(width, height)
    }

    companion object {
        private const val ASPECT_RATIO = 1.5f
    }
}