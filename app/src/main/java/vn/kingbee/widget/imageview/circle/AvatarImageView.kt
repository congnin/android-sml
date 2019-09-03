package vn.kingbee.widget.imageview.circle

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.graphics.drawable.ColorDrawable
import android.text.TextPaint
import android.util.AttributeSet
import vn.kingbee.widget.R

class AvatarImageView : CircleImageView {
    private val textPaint = TextPaint()
    var text: String? = ""
        set(text) {
            field = AvatarUtils.formatDefaultAvatarName(text)
            if (text != null && !text.isEmpty()) {
                this.setImageDrawable(ColorDrawable(this.backgroundColor))
            }

            this.invalidate()
        }
    private var textSize = UNUSED_FLOAT_VALUE
    private var backgroundColor = Color.LTGRAY

    constructor(context: Context) : super(context) {
        this.init(context, null)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        this.init(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle) {
        this.init(context, attrs)
    }

    private fun init(context: Context, attrs: AttributeSet?) {
        this.initResource(context, attrs)
        this.initPaintText()
    }

    private fun initResource(context: Context, attrs: AttributeSet?) {
        val a = context.obtainStyledAttributes(attrs, R.styleable.AvatarImageView, 0, 0)
        this.textSize = a.getDimension(R.styleable.AvatarImageView_text_size, -1.0f)
        a.recycle()
    }

    private fun initPaintText() {
        this.textPaint.color = Color.WHITE
        this.textPaint.textSize = this.textSize
        this.textPaint.textAlign = Paint.Align.CENTER
        this.textPaint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
    }

    override fun setBackgroundColor(color: Int) {
        this.backgroundColor = color
        this.setImageDrawable(ColorDrawable(color))
        this.invalidate()
    }

    fun setTextColor(color: Int) {
        this.textPaint.color = color
        this.invalidate()
    }

    protected override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        this.drawText(canvas)
    }

    override fun setImageBitmap(bm: Bitmap) {
        super.setImageBitmap(bm)
        this.text = ""
    }

    @SuppressLint("DefaultLocale")
    private fun drawText(canvas: Canvas) {
        canvas.drawText(this.text!!.toUpperCase(), (canvas.width / 2).toFloat(), (canvas.height / 2).toFloat() - (this.textPaint.descent() + this.textPaint.ascent()) / 2.0f, this.textPaint)
    }

    companion object {
        private const val UNUSED_FLOAT_VALUE = -1.0f
    }
}