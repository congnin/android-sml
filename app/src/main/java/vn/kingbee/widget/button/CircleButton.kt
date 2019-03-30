package vn.kingbee.widget.button

import android.view.View
import android.animation.ObjectAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.widget.ImageView
import android.util.TypedValue
import vn.kingbee.widget.R
import vn.kingbee.widget.utils.CommonUtils

class CircleButton : ImageView, View.OnClickListener {
    private val PRESSED_COLOR_LIGHTUP = 255 / 25
    private val PRESSED_RING_ALPHA = 75
    private val DEFAULT_PRESSED_RING_WIDTH_DIP = 4
    private val ANIMATION_TIME_ID = android.R.integer.config_shortAnimTime
    protected var animationProgress: Float = 0.toFloat()
    private var onClickListener: View.OnClickListener? = null
    private var mCenterY: Int = 0
    private var mCenterX: Int = 0
    private var mOuterRadius: Int = 0
    private var mPressedRingRadius: Int = 0
    private var mCirclePaint: Paint? = null
    private var mFocusPaint: Paint? = null
    private var mPressedRingWidth: Int = 0
    private var mDefaultColor = Color.BLACK
    private var mPressedColor: Int = 0
    private var mIsEnabled = true

    private var mEnableColor: Int = 0
    private var mDisableColor: Int = 0
    private var mPressedAnimator: ObjectAnimator? = null

    constructor(context: Context) : super(context) {
        init(context, null)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyle: Int) : super(
        context,
        attrs,
        defStyle
    ) {
        init(context, attrs)
    }

    override fun setPressed(pressed: Boolean) {
        super.setPressed(pressed)

        if (mCirclePaint != null) {
            mCirclePaint?.color = if (pressed) mPressedColor else mDefaultColor
        }

        if (pressed) {
            showPressedRing()
        } else {
            hidePressedRing()
        }
    }

    override fun onDraw(canvas: Canvas?) {
        canvas?.drawCircle(
            mCenterX.toFloat(),
            mCenterY.toFloat(), mPressedRingRadius + animationProgress, mFocusPaint!!
        )
        canvas?.drawCircle(
            mCenterX.toFloat(),
            mCenterY.toFloat(), (mOuterRadius - mPressedRingWidth).toFloat(), mCirclePaint!!
        )
        super.onDraw(canvas)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        mCenterX = w / 2
        mCenterY = h / 2
        mOuterRadius = Math.min(w, h) / 2
        mPressedRingRadius = mOuterRadius - mPressedRingWidth - mPressedRingWidth / 2
    }

    fun setColor(color: Int) {
        this.mDefaultColor = color
        this.mPressedColor = getHighlightColor(color, PRESSED_COLOR_LIGHTUP)

        mCirclePaint?.color = mDefaultColor
        mFocusPaint?.color = mDefaultColor
        mFocusPaint?.alpha = PRESSED_RING_ALPHA

        this.invalidate()
    }

    private fun hidePressedRing() {
        mPressedAnimator?.setFloatValues(mPressedRingWidth.toFloat(), 0f)
        mPressedAnimator?.start()
    }

    private fun showPressedRing() {
        mPressedAnimator?.setFloatValues(animationProgress, mPressedRingWidth.toFloat())
        mPressedAnimator?.start()
    }

    fun init(context: Context, attrs: AttributeSet?) {
        this.isFocusable = true
        this.scaleType = ScaleType.CENTER_INSIDE
        super.setOnClickListener(this)
        isClickable = true

        mCirclePaint = Paint(Paint.ANTI_ALIAS_FLAG)
        mCirclePaint?.style = Paint.Style.FILL

        mFocusPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        mFocusPaint?.style = Paint.Style.STROKE

        mPressedRingWidth = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP, DEFAULT_PRESSED_RING_WIDTH_DIP.toFloat(), resources
                .displayMetrics
        ).toInt()

        val color = Color.BLACK
        if (attrs != null) {
            val array = context.obtainStyledAttributes(attrs, R.styleable.CircleButton)
            mEnableColor = array.getColor(R.styleable.CircleButton_cb_color, color)
            mDisableColor = array.getColor(R.styleable.CircleButton_cb_disable_color, Color.GRAY)
            mPressedRingWidth =
                array.getDimension(
                    R.styleable.CircleButton_cb_pressedRingWidth,
                    mPressedRingWidth.toFloat()
                ).toInt()
            array.recycle()
        }

        setColor(mEnableColor)
        mIsEnabled = true

        mFocusPaint?.strokeWidth = mPressedRingWidth.toFloat()
        val pressedAnimationTime = resources.getInteger(ANIMATION_TIME_ID)
        mPressedAnimator = ObjectAnimator.ofFloat(this, "animationProgress", 0f, 0f)
        mPressedAnimator?.duration = pressedAnimationTime.toLong()
    }

    fun getHighlightColor(color: Int, amount: Int): Int {
        return Color.argb(
            Math.min(255, Color.alpha(color)), Math.min(255, Color.red(color) + amount),
            Math.min(255, Color.green(color) + amount), Math.min(255, Color.blue(color) + amount)
        )
    }

    override fun setEnabled(enabled: Boolean) {
        super.setEnabled(true)
        mIsEnabled = enabled
        isClickable = enabled
        setColor(if (enabled) mEnableColor else mDisableColor)
    }

    fun isEnableState(): Boolean {
        return mIsEnabled
    }

    override fun onClick(v: View?) {
        if (CommonUtils.isClickAvailable() && onClickListener != null) {
            onClickListener?.onClick(v)
        }
    }

    override fun setOnClickListener(onClickListener: View.OnClickListener?) {
        this.onClickListener = onClickListener
    }
}