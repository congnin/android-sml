package vn.kingbee.widget.imageview

import android.animation.ArgbEvaluator
import android.content.Context
import android.graphics.*
import android.support.annotation.FloatRange
import android.support.v7.widget.AppCompatImageView
import android.util.AttributeSet
import android.view.View
import android.os.Parcel
import android.os.Parcelable
import android.os.Build
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.view.animation.DecelerateInterpolator
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import vn.kingbee.widget.R

//https://github.com/zagum/Android-SwitchIcon
@SuppressLint("ObsoleteSdkInt")
class SwitchIconView : AppCompatImageView {
    private val animationDuration: Long
    @FloatRange(from = 0.0, to = 1.0)
    private var disabledStateAlpha: Float = 0.toFloat()
    private var dashXStart: Int = 0
    private var dashYStart: Int = 0
    private var clipPath: Path? = null
    private var iconTintColor: Int = 0
    private var disabledStateColor: Int = 0
    private var noDash: Boolean = false
    private var dashThickness: Int = 0
    private var dashLengthXProjection: Int = 0
    private var dashLengthYProjection: Int = 0
    private var colorFilter: PorterDuffColorFilter? = null
    private val colorEvaluator = ArgbEvaluator()

    @FloatRange(from = 0.0, to = 1.0)
    private var fraction = 0f
    private var enabled: Boolean? = null

    private var dashPaint: Paint? = null
    private val dashStart = Point()
    private val dashEnd = Point()

    constructor(context: Context) : this(context, null)

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        setLayerType(View.LAYER_TYPE_SOFTWARE, null)

        val array = getContext().theme.obtainStyledAttributes(attrs, R.styleable.SwitchIconView, 0, 0)

        try {
            iconTintColor = array.getColor(R.styleable.SwitchIconView_si_tint_color, Color.BLACK)
            animationDuration = array.getInteger(R.styleable.SwitchIconView_si_animation_duration, DEFAULT_ANIMATION_DURATION).toLong()
            disabledStateAlpha = array.getFloat(R.styleable.SwitchIconView_si_disabled_alpha, DEFAULT_DISABLED_ALPHA)
            disabledStateColor = array.getColor(R.styleable.SwitchIconView_si_disabled_color, iconTintColor)
            enabled = array.getBoolean(R.styleable.SwitchIconView_si_enabled, true)
            noDash = array.getBoolean(R.styleable.SwitchIconView_si_no_dash, false)
        } finally {
            array.recycle()
        }

        if (disabledStateAlpha < 0f || disabledStateAlpha > 1f) {
            throw IllegalArgumentException("Wrong value for si_disabled_alpha [" + disabledStateAlpha + "]. "
                    + "Must be value from range [0, 1]")
        }

        colorFilter = PorterDuffColorFilter(iconTintColor, PorterDuff.Mode.SRC_IN)
        setColorFilter(colorFilter)

        dashXStart = paddingLeft
        dashYStart = paddingTop

        dashPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        dashPaint!!.style = Paint.Style.STROKE
        dashPaint!!.color = iconTintColor

        clipPath = Path()

        initDashCoordinates()
        setFraction(if (this.enabled!!) 0f else 1f)
    }

    /**
     * Changes state with animation
     *
     * @param enabled If TRUE - icon is enabled
     */
    fun setIconEnabled(enabled: Boolean) {
        setIconEnabled(enabled, true)
    }

    /**
     * Changes state
     *
     * @param enabled If TRUE - icon is enabled
     */
    fun setIconEnabled(enabled: Boolean, animate: Boolean) {
        if (this.enabled == enabled) return
        switchState(animate)
    }

    /**
     * Check state
     *
     * @return TRUE if icon is enabled, otherwise FALSE
     */
    fun isIconEnabled(): Boolean {
        return if (enabled == null) false
        else enabled!!
    }

    /**
     * Switches icon state with animation
     */
    fun switchState() {
        switchState(true)
    }

    /**
     * Switches icon state
     *
     * @param animate Indicates that state will be changed with or without animation
     */
    fun switchState(animate: Boolean) {
        val newFraction: Float
        if (enabled!= null && enabled!!) {
            newFraction = 1f
        } else {
            newFraction = 0f
        }
        enabled = !enabled!!
        if (animate) {
            animateToFraction(newFraction)
        } else {
            setFraction(newFraction)
            invalidate()
        }
    }

    public override fun onSaveInstanceState(): Parcelable? {
        val superState = super.onSaveInstanceState()
        val savedState = SwitchIconSavedState(superState)
        savedState.iconEnabled = this.enabled!!
        return savedState
    }

    public override fun onRestoreInstanceState(state: Parcelable) {
        if (state !is SwitchIconSavedState) {
            super.onRestoreInstanceState(state)
            return
        }
        super.onRestoreInstanceState(state.superState)
        enabled = state.iconEnabled
        setFraction(if (enabled as Boolean) 0f else 1f)
    }

    override fun onSizeChanged(width: Int, height: Int, oldWidth: Int, oldHeight: Int) {
        super.onSizeChanged(width, height, oldWidth, oldHeight)
        dashLengthXProjection = width - paddingLeft - paddingRight
        dashLengthYProjection = height - paddingTop - paddingBottom
        dashThickness = ((DASH_THICKNESS_PART * (dashLengthXProjection + dashLengthYProjection) / 2f).toInt())
        dashPaint!!.strokeWidth = dashThickness.toFloat()
        initDashCoordinates()
        updateClipPath()
    }

    override fun onDraw(canvas: Canvas) {
        if (!noDash) {
            drawDash(canvas)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                canvas.clipOutPath(clipPath)
            } else {
                canvas.clipPath(clipPath, Region.Op.XOR)
            }
        }
        super.onDraw(canvas)
    }

    private fun animateToFraction(toFraction: Float) {
        val animator = ValueAnimator.ofFloat(fraction, toFraction)
        animator.addUpdateListener { animation -> setFraction(animation.animatedValue as Float) }
        animator.interpolator = DecelerateInterpolator()
        animator.duration = animationDuration
        animator.start()
    }

    private fun setFraction(fraction: Float) {
        this.fraction = fraction
        updateColor(fraction)
        updateAlpha(fraction)
        updateClipPath()
        postInvalidateOnAnimationCompat()
    }

    private fun initDashCoordinates() {
        val delta1 = 1.5f * SIN_45 * dashThickness
        val delta2 = 0.5f * SIN_45 * dashThickness
        dashStart.x = (dashXStart + delta2).toInt()
        dashStart.y = dashYStart + delta1.toInt()
        dashEnd.x = (dashXStart + dashLengthXProjection - delta1).toInt()
        dashEnd.y = (dashYStart + dashLengthYProjection - delta2).toInt()
    }

    private fun updateClipPath() {
        val delta = dashThickness / SIN_45
        clipPath!!.reset()
        clipPath!!.moveTo(dashXStart.toFloat(), dashYStart + delta)
        clipPath!!.lineTo(dashXStart + delta, dashYStart.toFloat())
        clipPath!!.lineTo(dashXStart + dashLengthXProjection * fraction, dashYStart + dashLengthYProjection * fraction - delta)
        clipPath!!.lineTo(dashXStart + dashLengthXProjection * fraction - delta, dashYStart + dashLengthYProjection * fraction)
    }

    private fun drawDash(canvas: Canvas) {
        val x = fraction * (dashEnd.x - dashStart.x) + dashStart.x
        val y = fraction * (dashEnd.y - dashStart.y) + dashStart.y
        canvas.drawLine(dashStart.x.toFloat(), dashStart.y.toFloat(), x, y, dashPaint)
    }

    private fun updateColor(fraction: Float) {
        if (iconTintColor != disabledStateColor) {
            val color = colorEvaluator.evaluate(fraction, iconTintColor, disabledStateColor) as Int
            updateImageColor(color)
            dashPaint!!.color = color
        }
    }

    private fun updateAlpha(fraction: Float) {
        val alpha = ((disabledStateAlpha + (1f - fraction) * (1f - disabledStateAlpha)) * 255).toInt()
        updateImageAlpha(alpha)
        dashPaint!!.alpha = alpha
    }

    private fun updateImageColor(color: Int) {
        colorFilter = PorterDuffColorFilter(color, PorterDuff.Mode.SRC_IN)
        setColorFilter(colorFilter)
    }

    private fun updateImageAlpha(alpha: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            imageAlpha = alpha
        } else {
            setAlpha(alpha)
        }
    }

    private fun postInvalidateOnAnimationCompat() {
        val fakeFrameTime: Long = 10
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1) {
            postInvalidateOnAnimation()
        } else {
            postInvalidateDelayed(fakeFrameTime)
        }
    }

    internal class SwitchIconSavedState : BaseSavedState {
        var iconEnabled: Boolean = false

        constructor(superState: Parcelable) : super(superState)

        private constructor(`in`: Parcel) : super(`in`) {
            val enabled = `in`.readInt()
            iconEnabled = enabled == 1
        }

        override fun writeToParcel(out: Parcel, flags: Int) {
            super.writeToParcel(out, flags)
            out.writeInt(if (iconEnabled) 1 else 0)
        }

        companion object {

            @SuppressLint("ParcelCreator")
            val CREATOR: Parcelable.Creator<SwitchIconSavedState> = object : Parcelable.Creator<SwitchIconSavedState> {
                override fun createFromParcel(`in`: Parcel): SwitchIconSavedState {
                    return SwitchIconSavedState(`in`)
                }

                override fun newArray(size: Int): Array<SwitchIconSavedState?> {
                    return arrayOfNulls(size)
                }
            }
        }
    }

    companion object {
        private const val DEFAULT_ANIMATION_DURATION = 300
        private const val DASH_THICKNESS_PART = 1f / 12f
        private const val DEFAULT_DISABLED_ALPHA = .5f
        private val SIN_45 = Math.sin(Math.toRadians(45.0)).toFloat()
    }
}