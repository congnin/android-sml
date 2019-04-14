package vn.kingbee.widget.keyboard

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.inputmethodservice.KeyboardView
import android.util.AttributeSet
import android.graphics.Color
import android.graphics.Typeface
import vn.kingbee.utils.CommonUtils


class NumpadKeyboardView(context: Context, attrs: AttributeSet) : KeyboardView(context, attrs) {
    private var mContext: Context? = context
    private var mPaint: Paint? = null
    private var responderView: NumpadKeyboardEditText? = null

    init {
        mPaint = Paint()
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        val font = Typeface.createFromAsset(mContext?.assets, "fonts/OpenSans-Regular.ttf")
        mPaint?.typeface = font
        mPaint?.textAlign = Paint.Align.CENTER

        val keys = keyboard.keys
        for (key in keys) {
            canvas?.translate(key.x.toFloat(), key.y.toFloat())
            mPaint?.color = Color.BLACK

            if (key.label != null) {
                mPaint!!.textSize = CommonUtils.pxFromDp(22F, mContext!!)
                canvas?.drawText(
                    key.label.toString(),
                    (key.width / 2).toFloat(),
                    key.height / 2 + CommonUtils.pxFromDp(10F, mContext!!),
                    mPaint!!
                )
            }

            canvas?.translate((-key.x).toFloat(), (-key.y).toFloat())
        }
    }

    fun setCurrentActiveResponderView(responderView: NumpadKeyboardEditText) {
        this.responderView = responderView
    }

    fun deselectCurrentActiveResponderViewIfNeed() {
        if (responderView != null) {
            responderView?.isSelected = false
        }
    }
}