package vn.kingbee.widget.dialog.big.timeout

import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import android.view.animation.LinearInterpolator
import androidx.core.content.ContextCompat
import vn.kingbee.widget.R

class LoadingCircular : View {
    lateinit var mPaint: Paint
    private var mWidth: Float
    private var mPadding: Float
    private var startAngle: Float
    private var rectF: RectF
    private var valueAnimator: ValueAnimator? = null

    constructor(context: Context) : this(context, null)

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        this.mWidth = 0.0F
        this.mPadding = 0.0F
        this.startAngle = 0.0F
        this.rectF = RectF()
        this.initPaint()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        if (measuredWidth > height) {
            mWidth = measuredHeight.toFloat()
        } else {
            mWidth = measuredWidth.toFloat()
        }

        mPadding = 5.0F
    }

    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        mPaint.color = ContextCompat.getColor(context, R.color.cbsa_color_yellow_30)
        canvas?.drawCircle(mWidth / 2.0F, mWidth / 2.0F, mWidth / 2.0F - mPadding, mPaint)
        mPaint.color = ContextCompat.getColor(context, R.color.cbsa_color_yellow)
        rectF = RectF(mPadding, mPadding, mWidth - mPadding, mWidth - mPadding)
        canvas?.drawArc(rectF, startAngle, 100.0F, false, mPaint)
    }

    private fun initPaint() {
        mPaint = Paint()
        mPaint.isAntiAlias = true
        mPaint.style = Paint.Style.STROKE
        mPaint.strokeWidth = 5.0F
    }

    fun startAnim() {
        stopAnim()
        startViewAnim(0.0F, 1.0F, 1000L)
    }

    fun stopAnim() {
        if (valueAnimator != null) {
            clearAnimation()
            valueAnimator?.repeatCount = 0
            valueAnimator?.cancel()
            valueAnimator?.end()
        }
    }

    private fun startViewAnim(startF: Float, endF: Float, time: Long): ValueAnimator {
        this.valueAnimator = ValueAnimator.ofFloat(startF, endF)
        this.valueAnimator?.duration = time
        this.valueAnimator?.interpolator = LinearInterpolator()
        this.valueAnimator?.repeatCount = -1
        this.valueAnimator?.repeatMode = ValueAnimator.RESTART
        this.valueAnimator?.addUpdateListener { valueAnimator ->
            val value = valueAnimator.animatedValue as Float
            this@LoadingCircular.startAngle = 360.0f * value
            this@LoadingCircular.invalidate()
        }
        this.valueAnimator?.addListener(object : AnimatorListenerAdapter() {
        })
        if (valueAnimator != null && !this.valueAnimator?.isRunning!!) {
            this.valueAnimator?.start()
        }

        return this.valueAnimator!!
    }
}