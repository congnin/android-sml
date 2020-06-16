package vn.kingbee.widget.calendar.ggdatepicker

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import androidx.core.content.ContextCompat
import android.util.AttributeSet
import android.widget.TextView
import vn.kingbee.widget.R

class TextViewWithCircularIndicator(context: Context, attributeSet: AttributeSet) :
    TextView(context, attributeSet) {
    private var mCircleColor: Int
    private var mItemIsSelectedText: String
    private var mRadius: Int
    private val mCirclePaint = Paint()
    private var mDrawCircle: Boolean = false

    init {
        val res = context.resources
        mCircleColor = ContextCompat.getColor(context, R.color.blue)
        mRadius = res.getDimensionPixelOffset(R.dimen.month_select_circle_radius)
        mItemIsSelectedText = context.resources.getString(R.string.item_is_selected)
        init()
    }

    private fun init() {
        mCirclePaint.isFakeBoldText = true
        mCirclePaint.isAntiAlias = true
        mCirclePaint.color = mCircleColor
        mCirclePaint.textAlign = Paint.Align.CENTER
        mCirclePaint.style = Paint.Style.FILL
        mCirclePaint.alpha = 60
    }

    fun drawIndicator(drawIndicator: Boolean) {
        mDrawCircle = drawIndicator
    }

    @SuppressLint("GetContentDescriptionOverride")
    override fun getContentDescription(): CharSequence {
        var text = text
        if (mDrawCircle) {
            text = String.format(mItemIsSelectedText, text)
        }
        return text
    }

    public override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if (mDrawCircle) {
            val width = width
            val height = height
            val radius = Math.min(width, height) / 2
            canvas.drawCircle(
                (width / 2).toFloat(), (height / 2).toFloat(), radius.toFloat(), mCirclePaint
            )
        }
    }
}