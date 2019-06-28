package vn.kingbee.widget.button

import android.widget.ImageButton
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Paint
import android.util.AttributeSet
import androidx.core.content.ContextCompat
import vn.kingbee.widget.R
import android.graphics.Canvas
import android.util.TypedValue
import kotlin.math.min

class CircleButtonV2 : ImageButton {

    private var mCenterY: Int = 0
    private var mCenterX: Int = 0
    private var mOuterRadius: Float = 0.toFloat()
    private var mPressedRingRadius: Float = 0.toFloat()
    private var mFocusPaint: Paint? = null
    protected var mAnimationProgress: Float = 0.toFloat()
    private var mPressedRingWidth: Float = 0.toFloat()
    private var mDefaultColor: Int = 0
    private var mEnableColor: Int = 0
    private var mDisableColor: Int = 0
    private var mPressedAnimator: ObjectAnimator? = null

    constructor(context: Context) : super(context) {
        this.mDefaultColor = ContextCompat.getColor(context, R.color.cbsa_color_transparent)
        init(context, null)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        this.mDefaultColor = ContextCompat.getColor(context, R.color.cbsa_color_transparent)
        init(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyle: Int) : super(context, attrs, defStyle) {
        this.mDefaultColor = ContextCompat.getColor(context, R.color.cbsa_color_transparent)
        init(context, attrs)
    }

    override fun setPressed(pressed: Boolean) {
        super.setPressed(pressed)
        if (pressed) {
            this.showPressedRing()
        } else {
            this.hidePressedRing()
        }
    }

    override fun onDraw(canvas: Canvas) {
        if (this.mAnimationProgress > 0.0f) {
            this.mFocusPaint?.strokeWidth = this.mAnimationProgress * PRESSED_INACTIVE_INCREASE_STROKE_WIDTH
            if (!this.isActivated) {
                if (this.mAnimationProgress <= PRESSED_INACTIVE_LIMITED) {
                    this.mFocusPaint?.alpha = PRESSED_RING_ALPHA_INACTIVE_MIN
                } else {
                    this.mFocusPaint?.alpha = PRESSED_RING_ALPHA_INACTIVE_MAX
                }
            } else {
                this.mFocusPaint?.alpha = PRESSED_RING_ALPHA
            }

            canvas.drawCircle(this.mCenterX.toFloat(), this.mCenterY.toFloat(),
                this.mPressedRingRadius + this.mAnimationProgress, this.mFocusPaint)
        }

        super.onDraw(canvas)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        this.mCenterX = w / 2
        this.mCenterY = h / 2
        this.mOuterRadius = (min(w, h) / 2).toFloat()
        this.mPressedRingRadius = this.mOuterRadius - this.mPressedRingWidth - this.mPressedRingWidth
    }

    fun getAnimationProgress(): Float {
        return this.mAnimationProgress
    }

    fun setAnimationProgress(animationProgress: Float) {
        this.mAnimationProgress = animationProgress
        this.invalidate()
    }

    fun setColor(color: Int) {
        this.mDefaultColor = color
        this.mFocusPaint?.color = this.mDefaultColor
        this.mFocusPaint?.alpha = PRESSED_RING_ALPHA
        this.invalidate()
    }

    private fun hidePressedRing() {
        this.mPressedAnimator?.setFloatValues(this.mPressedRingWidth, 0.0f)
        this.mPressedAnimator?.start()
    }

    private fun showPressedRing() {
        this.mPressedAnimator?.setFloatValues(this.mAnimationProgress, this.mPressedRingWidth)
        this.mPressedAnimator?.start()
    }

    @SuppressLint("CustomViewStyleable")
    private fun init(context: Context, attrs: AttributeSet?) {
        isFocusable = true
        scaleType = ScaleType.CENTER_INSIDE
        isClickable = true
        mFocusPaint = Paint(1)
        mFocusPaint?.style = Paint.Style.STROKE
        mPressedRingWidth = TypedValue.applyDimension(1, DEFAULT_PRESSED_RING_WIDTH_DIP,
            resources.displayMetrics)
        val color = ContextCompat.getColor(context, R.color.cbsa_color_transparent)
        if (attrs != null) {
            val typedArray = context.obtainStyledAttributes(attrs, R.styleable.CircleButton)
            this.mEnableColor = typedArray.getColor(R.styleable.CircleButton_cb_color, color)
            this.mDisableColor = typedArray.getColor(R.styleable.CircleButton_cb_disable_color, color)
            this.mPressedRingWidth = typedArray.getDimension(R.styleable.CircleButton_cb_pressedRingWidth,
                this.mPressedRingWidth)
            typedArray.recycle()
        }

        this.mFocusPaint?.strokeWidth = this.mPressedRingWidth
        val pressedAnimationTime = this.resources.getInteger(ANIMATION_TIME_ID)
        this.mPressedAnimator = ObjectAnimator.ofFloat(this, "animationProgress", *floatArrayOf(0.0f, 0.0f))
        this.mPressedAnimator?.duration = pressedAnimationTime.toLong()
        this.isActivated = true
    }

    override fun setActivated(activated: Boolean) {
        super.setActivated(activated)
        this.setColor(if (activated) this.mEnableColor else this.mDisableColor)
    }

    companion object {
        private const val PRESSED_RING_ALPHA_INACTIVE_MIN = 10
        private const val PRESSED_RING_ALPHA_INACTIVE_MAX = 30
        private const val PRESSED_INACTIVE_LIMITED = 5.0f
        private const val PRESSED_INACTIVE_INCREASE_STROKE_WIDTH = 1.2f
        private const val PRESSED_RING_ALPHA = 75
        private const val DEFAULT_PRESSED_RING_WIDTH_DIP = 4F
        private const val ANIMATION_TIME_ID = 17694720
    }
}