package vn.kingbee.widget.edittext.prefixed

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Canvas
import android.graphics.ColorFilter
import android.graphics.drawable.Drawable
import androidx.appcompat.widget.AppCompatEditText
import android.util.AttributeSet

/**
 * Custom EditText that displays a fixed prefix in line with the text.
 * The trick here is to draw the prefix as a drawable and attach it via
 * setCompoundDrawables().
 */
open class PrefixedEditText : AppCompatEditText {
    private var mPrefixTextColor: ColorStateList? = null

    constructor(context: Context) : this(context, null)

    constructor(context: Context, attrs: AttributeSet?)
            : this(context, attrs, android.R.attr.editTextStyle)

    constructor(context: Context, attrs: AttributeSet?, defStyle: Int) : super(context, attrs, defStyle) {
        mPrefixTextColor = textColors
    }

    fun setPrefix(prefix: String) {
        setCompoundDrawables(TextDrawable(prefix), null, null, null)
    }

    fun setPrefixTextColor(color: Int) {
        mPrefixTextColor = ColorStateList.valueOf(color)
    }

    fun setPrefixTextColor(color: ColorStateList) {
        mPrefixTextColor = color
    }

    private inner class TextDrawable(text: String) : Drawable() {
        private var mText = ""
        private var mBaseLine = -1

        init {
            mText = text
            setBounds(0, 0, paint.measureText(mText).toInt() + 2, textSize.toInt())
        }

        override fun draw(canvas: Canvas) {
            val paint = paint
            paint.color = mPrefixTextColor!!.getColorForState(drawableState, 0)
            if (mBaseLine < 0) {
                mBaseLine = canvas.clipBounds.top + getLineBounds(0, null)
            }
            canvas.drawText(mText, 0f, mBaseLine.toFloat(), paint)
        }

        override fun setAlpha(alpha: Int) {
            /* Not supported */
        }

        override fun setColorFilter(colorFilter: ColorFilter?) {
            /* Not supported */
        }

        @SuppressLint("WrongConstant")
        override fun getOpacity(): Int {
            return 1
        }
    }
}