package vn.kingbee.widget.progress.circularprogressindicator

import android.animation.Animator
import android.animation.PropertyValuesHolder
import android.animation.TypeEvaluator
import android.animation.ValueAnimator
import android.annotation.TargetApi
import android.content.Context
import android.content.res.TypedArray
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.LinearGradient
import android.graphics.Matrix
import android.graphics.Paint
import android.graphics.RadialGradient
import android.graphics.Rect
import android.graphics.RectF
import android.graphics.Shader
import android.graphics.SweepGradient
import android.os.Build
import androidx.annotation.ColorInt
import androidx.annotation.Dimension
import androidx.annotation.IntDef
import androidx.annotation.IntRange
import android.text.TextPaint
import android.util.AttributeSet
import android.util.DisplayMetrics
import android.util.TypedValue
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.Interpolator

import java.lang.annotation.RetentionPolicy

import vn.kingbee.widget.R
import kotlin.annotation.Retention

//https://github.com/antonKozyriatskyi/CircularProgressIndicator
class CircularProgressIndicator : View {


    private var progressPaint: Paint? = null
    private var progressBackgroundPaint: Paint? = null
    private var dotPaint: Paint? = null
    private var textPaint: Paint? = null

    private var startAngle = DEFAULT_PROGRESS_START_ANGLE
    private var sweepAngle = 0

    private var circleBounds: RectF? = null

    private var progressText: String? = null
    private var textX: Float = 0.toFloat()
    private var textY: Float = 0.toFloat()

    private var radius: Float = 0.toFloat()

    var isDotEnabled: Boolean = false
        private set

    private var maxProgressValue = 100.0
    var progress = 0.0
        private set

    private var isAnimationEnabled: Boolean = false

    private var isFillBackgroundEnabled: Boolean = false

    @Direction
    private var direction = DIRECTION_COUNTERCLOCKWISE

    private var progressAnimator: ValueAnimator? = null

    private lateinit var progressTextAdapter: ProgressTextAdapter

    var onProgressChangeListener: OnProgressChangeListener? = null

    var interpolator: Interpolator = AccelerateDecelerateInterpolator()

    var progressColor: Int
        @ColorInt get() = progressPaint!!.color
        set(@ColorInt color) {
            progressPaint!!.color = color
            invalidate()
        }

    var progressBackgroundColor: Int
        @ColorInt get() = progressBackgroundPaint!!.color
        set(@ColorInt color) {
            progressBackgroundPaint!!.color = color
            invalidate()
        }

    val progressStrokeWidth: Float
        get() = progressPaint!!.strokeWidth

    val progressBackgroundStrokeWidth: Float
        get() = progressBackgroundPaint!!.strokeWidth

    var textColor: Int
        @ColorInt get() = textPaint!!.color
        set(@ColorInt color) {
            textPaint!!.color = color

            val textRect = Rect()
            textPaint!!.getTextBounds(progressText, 0, progressText!!.length, textRect)

            invalidate(textRect)
        }

    val textSize: Float
        get() = textPaint!!.textSize

    var dotColor: Int
        @ColorInt get() = dotPaint!!.color
        set(@ColorInt color) {
            dotPaint!!.color = color

            invalidate()
        }

    val dotWidth: Float
        get() = dotPaint!!.strokeWidth

    var maxProgress: Double
        get() = maxProgressValue
        set(maxProgress) {
            maxProgressValue = maxProgress
            if (maxProgressValue < progress) {
                setCurrentProgress(maxProgress)
            }
            invalidate()
        }

    var progressStrokeCap: Int
        @Cap get() = if (progressPaint!!.strokeCap == Paint.Cap.ROUND) CAP_ROUND else CAP_BUTT
        set(@Cap cap) {
            val paintCap = if (cap == CAP_ROUND) Paint.Cap.ROUND else Paint.Cap.BUTT
            if (progressPaint!!.strokeCap != paintCap) {
                progressPaint!!.strokeCap = paintCap
                invalidate()
            }
        }

    val gradientType: Int
        @GradientType get() {
            val shader = progressPaint!!.shader

            var type = NO_GRADIENT

            if (shader is LinearGradient) {
                type = LINEAR_GRADIENT
            } else if (shader is RadialGradient) {
                type = RADIAL_GRADIENT
            } else if (shader is SweepGradient) {
                type = SWEEP_GRADIENT
            }

            return type
        }

    constructor(context: Context) : super(context) {
        init(context, null)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init(context, attrs)
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    constructor(context: Context,
                attrs: AttributeSet?,
                defStyleAttr: Int,
                defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes) {
        init(context, attrs)
    }

    private fun init(context: Context, attrs: AttributeSet?) {

        var progressColor = Color.parseColor(DEFAULT_PROGRESS_COLOR)
        var progressBackgroundColor = Color.parseColor(DEFAULT_PROGRESS_BACKGROUND_COLOR)
        var progressStrokeWidth = dp2px(DEFAULT_STROKE_WIDTH_DP.toFloat())
        var progressBackgroundStrokeWidth = progressStrokeWidth
        var textColor = progressColor
        var textSize = sp2px(DEFAULT_TEXT_SIZE_SP.toFloat())

        isDotEnabled = true
        var dotColor = progressColor
        var dotWidth = progressStrokeWidth

        var progressStrokeCap: Paint.Cap = Paint.Cap.ROUND

        if (attrs != null) {
            val a = context.obtainStyledAttributes(attrs, R.styleable.CircularProgressIndicator)

            progressColor =
                a.getColor(R.styleable.CircularProgressIndicator_progressColor, progressColor)
            progressBackgroundColor = a.getColor(
                R.styleable.CircularProgressIndicator_progressBackgroundColor,
                progressBackgroundColor
            )
            progressStrokeWidth = a.getDimensionPixelSize(
                R.styleable.CircularProgressIndicator_progressStrokeWidth,
                progressStrokeWidth
            )
            progressBackgroundStrokeWidth = a.getDimensionPixelSize(
                R.styleable.CircularProgressIndicator_progressBackgroundStrokeWidth,
                progressStrokeWidth
            )
            textColor = a.getColor(R.styleable.MaterialEditText_textColor, progressColor)
            textSize =
                a.getDimensionPixelSize(R.styleable.CircularProgressIndicator_textSize, textSize)

            isDotEnabled = a.getBoolean(R.styleable.CircularProgressIndicator_drawDot, true)
            dotColor = a.getColor(R.styleable.CircularProgressIndicator_dotColor, progressColor)
            dotWidth = a.getDimensionPixelSize(
                R.styleable.CircularProgressIndicator_dotWidth,
                progressStrokeWidth
            )

            startAngle = a.getInt(
                R.styleable.CircularProgressIndicator_startAngle,
                DEFAULT_PROGRESS_START_ANGLE
            )
            if (startAngle < 0 || startAngle > 360) {
                startAngle = DEFAULT_PROGRESS_START_ANGLE
            }

            isAnimationEnabled =
                a.getBoolean(R.styleable.CircularProgressIndicator_enableProgressAnimation, true)
            isFillBackgroundEnabled =
                a.getBoolean(R.styleable.CircularProgressIndicator_fillBackground, false)

            direction = a.getInt(
                R.styleable.CircularProgressIndicator_direction,
                DIRECTION_COUNTERCLOCKWISE
            )

            val cap = a.getInt(R.styleable.CircularProgressIndicator_progressCap, CAP_ROUND)
            progressStrokeCap = if (cap == CAP_ROUND) Paint.Cap.ROUND else Paint.Cap.BUTT

            val formattingPattern =
                a.getString(R.styleable.CircularProgressIndicator_formattingPattern)
            if (formattingPattern != null) {
                progressTextAdapter = PatternProgressTextAdapter(formattingPattern)
            } else {
                progressTextAdapter = DefaultProgressTextAdapter()
            }

            reformatProgressText()

            val gradientType = a.getColor(R.styleable.CircularProgressIndicator_gradientType, 0)
            if (gradientType != NO_GRADIENT) {
                val gradientColorEnd =
                    a.getColor(R.styleable.CircularProgressIndicator_gradientEndColor, -1)

                if (gradientColorEnd == -1) {
                    throw IllegalArgumentException("did you forget to specify gradientColorEnd?")
                }

                post { setGradient(gradientType, gradientColorEnd) }
            }

            a.recycle()
        }

        progressPaint = Paint()
        progressPaint!!.strokeCap = progressStrokeCap
        progressPaint!!.strokeWidth = progressStrokeWidth.toFloat()
        progressPaint!!.style = Paint.Style.STROKE
        progressPaint!!.color = progressColor
        progressPaint!!.isAntiAlias = true

        val progressBackgroundStyle =
            if (isFillBackgroundEnabled) Paint.Style.FILL_AND_STROKE else Paint.Style.STROKE
        progressBackgroundPaint = Paint()
        progressBackgroundPaint!!.style = progressBackgroundStyle
        progressBackgroundPaint!!.strokeWidth = progressBackgroundStrokeWidth.toFloat()
        progressBackgroundPaint!!.color = progressBackgroundColor
        progressBackgroundPaint!!.isAntiAlias = true

        dotPaint = Paint()
        dotPaint!!.strokeCap = Paint.Cap.ROUND
        dotPaint!!.strokeWidth = dotWidth.toFloat()
        dotPaint!!.style = Paint.Style.FILL_AND_STROKE
        dotPaint!!.color = dotColor
        dotPaint!!.isAntiAlias = true

        textPaint = TextPaint()
        textPaint!!.strokeCap = Paint.Cap.ROUND
        textPaint!!.color = textColor
        textPaint!!.isAntiAlias = true
        textPaint!!.textSize = textSize.toFloat()

        circleBounds = RectF()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        val paddingLeft = paddingLeft
        val paddingRight = paddingRight
        val paddingTop = paddingTop
        val paddingBottom = paddingBottom

        val measuredWidth = View.MeasureSpec.getSize(widthMeasureSpec)
        val measuredHeight = View.MeasureSpec.getSize(heightMeasureSpec)

        val widthMode = View.MeasureSpec.getMode(widthMeasureSpec)
        val heightMode = View.MeasureSpec.getMode(heightMeasureSpec)

        val textBoundsRect = Rect()
        textPaint!!.getTextBounds(progressText, 0, progressText!!.length, textBoundsRect)


        val dotWidth = dotPaint!!.strokeWidth
        val progressWidth = progressPaint!!.strokeWidth
        val progressBackgroundWidth = progressBackgroundPaint!!.strokeWidth
        val strokeSizeOffset = if (isDotEnabled) Math.max(
            dotWidth,
            Math.max(progressWidth, progressBackgroundWidth)
        ) else Math.max(progressWidth, progressBackgroundWidth)

        var desiredSize = strokeSizeOffset.toInt() + dp2px(DESIRED_WIDTH_DP.toFloat()) + Math.max(
            paddingBottom + paddingTop,
            paddingLeft + paddingRight
        )

        // multiply by .1f to have an extra space for small padding between text and circle
        desiredSize += (Math.max(
            textBoundsRect.width(),
            textBoundsRect.height()
        ) + desiredSize * .1f).toInt()

        val finalWidth: Int
        when (widthMode) {
            View.MeasureSpec.EXACTLY -> finalWidth = measuredWidth
            View.MeasureSpec.AT_MOST -> finalWidth = Math.min(desiredSize, measuredWidth)
            else -> finalWidth = desiredSize
        }

        val finalHeight: Int
        when (heightMode) {
            View.MeasureSpec.EXACTLY -> finalHeight = measuredHeight
            View.MeasureSpec.AT_MOST -> finalHeight = Math.min(desiredSize, measuredHeight)
            else -> finalHeight = desiredSize
        }

        val widthWithoutPadding = finalWidth - paddingLeft - paddingRight
        val heightWithoutPadding = finalHeight - paddingTop - paddingBottom

        val smallestSide = Math.min(heightWithoutPadding, widthWithoutPadding)
        setMeasuredDimension(smallestSide, smallestSide)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        calculateBounds(w, h)

        val shader = progressPaint!!.shader
        if (shader is RadialGradient) {
            val gradient = shader
        }
    }

    private fun calculateBounds(w: Int, h: Int) {
        radius = w / 2f

        val dotWidth = dotPaint!!.strokeWidth
        val progressWidth = progressPaint!!.strokeWidth
        val progressBackgroundWidth = progressBackgroundPaint!!.strokeWidth
        val strokeSizeOffset = if (isDotEnabled) Math.max(
            dotWidth,
            Math.max(progressWidth, progressBackgroundWidth)
        ) else Math.max(
            progressWidth,
            progressBackgroundWidth
        ) // to prevent progress or dot from drawing over the bounds
        val halfOffset = strokeSizeOffset / 2f

        circleBounds!!.left = halfOffset
        circleBounds!!.top = halfOffset
        circleBounds!!.right = w - halfOffset
        circleBounds!!.bottom = h - halfOffset

        radius = circleBounds!!.width() / 2f

        calculateTextBounds()
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()

        if (progressAnimator != null) {
            progressAnimator!!.cancel()
        }
    }

    override fun onDraw(canvas: Canvas) {
        drawProgressBackground(canvas)
        drawProgress(canvas)
        if (isDotEnabled) drawDot(canvas)
        drawText(canvas)
    }

    private fun drawProgressBackground(canvas: Canvas) {
        canvas.drawArc(
            circleBounds!!,
            ANGLE_START_PROGRESS_BACKGROUND.toFloat(),
            ANGLE_END_PROGRESS_BACKGROUND.toFloat(),
            false,
            progressBackgroundPaint!!
        )
    }

    private fun drawProgress(canvas: Canvas) {
        canvas.drawArc(
            circleBounds!!,
            startAngle.toFloat(),
            sweepAngle.toFloat(),
            false,
            progressPaint!!
        )
    }

    private fun drawDot(canvas: Canvas) {
        val angleRadians = Math.toRadians((startAngle + sweepAngle + 180).toDouble())
        val cos = Math.cos(angleRadians).toFloat()
        val sin = Math.sin(angleRadians).toFloat()
        val x = circleBounds!!.centerX() - radius * cos
        val y = circleBounds!!.centerY() - radius * sin

        canvas.drawPoint(x, y, dotPaint!!)
    }

    private fun drawText(canvas: Canvas) {
        canvas.drawText(progressText!!, textX, textY, textPaint!!)
    }

    fun setCurrentProgress(currentProgress: Double) {
        if (currentProgress > maxProgressValue) {
            maxProgressValue = currentProgress
        }

        setProgress(currentProgress, maxProgressValue)
    }

    fun setProgress(current: Double, max: Double) {
        val finalAngle: Double

        if (direction == DIRECTION_COUNTERCLOCKWISE) {
            finalAngle = -(current / max * 360)
        } else {
            finalAngle = current / max * 360
        }

        val oldCurrentProgress = progress

        maxProgressValue = max
        progress = Math.min(current, max)

        if (onProgressChangeListener != null) {
            onProgressChangeListener!!.onProgressChanged(progress, maxProgressValue)
        }

        reformatProgressText()
        calculateTextBounds()

        stopProgressAnimation()

        if (isAnimationEnabled) {
            startProgressAnimation(oldCurrentProgress, finalAngle)
        } else {
            sweepAngle = finalAngle.toInt()
            invalidate()
        }
    }

    private fun startProgressAnimation(oldCurrentProgress: Double, finalAngle: Double) {
        val angleProperty =
            PropertyValuesHolder.ofInt(PROPERTY_ANGLE, sweepAngle, finalAngle.toInt())

        progressAnimator = ValueAnimator.ofObject(
            TypeEvaluator<Double> { fraction, startValue, endValue -> startValue!! + (endValue!! - startValue) * fraction },
            oldCurrentProgress,
            progress
        )
        progressAnimator!!.duration = DEFAULT_ANIMATION_DURATION.toLong()
        progressAnimator!!.setValues(angleProperty)
        progressAnimator!!.interpolator = interpolator
        progressAnimator!!.addUpdateListener { animation ->
            sweepAngle = animation.getAnimatedValue(PROPERTY_ANGLE) as Int
            invalidate()
        }
        progressAnimator!!.addListener(object : DefaultAnimatorListener() {
            override fun onAnimationCancel(animation: Animator) {
                sweepAngle = finalAngle.toInt()
                invalidate()
                progressAnimator = null
            }
        })
        progressAnimator!!.start()
    }

    private fun stopProgressAnimation() {
        if (progressAnimator != null) {
            progressAnimator!!.cancel()
        }
    }

    private fun reformatProgressText() {
        progressText = progressTextAdapter.formatText(progress)
    }

    private fun calculateTextBounds(): Rect {
        val textRect = Rect()
        textPaint!!.getTextBounds(progressText, 0, progressText!!.length, textRect)
        textX = circleBounds!!.centerX() - textRect.width() / 2f
        textY = circleBounds!!.centerY() + textRect.height() / 2f

        return textRect
    }

    private fun dp2px(dp: Float): Int {
        val metrics = resources.displayMetrics
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, metrics).toInt()
    }

    private fun sp2px(sp: Float): Int {
        val metrics = resources.displayMetrics
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, metrics).toInt()
    }

    // calculates circle bounds, view size and requests invalidation
    private fun invalidateEverything() {
        calculateBounds(width, height)
        requestLayout()
        invalidate()
    }

    fun setProgressStrokeWidthDp(@Dimension strokeWidth: Int) {
        setProgressStrokeWidthPx(dp2px(strokeWidth.toFloat()))
    }

    fun setProgressStrokeWidthPx(@Dimension strokeWidth: Int) {
        progressPaint!!.strokeWidth = strokeWidth.toFloat()

        invalidateEverything()
    }

    fun setProgressBackgroundStrokeWidthDp(@Dimension strokeWidth: Int) {
        setProgressBackgroundStrokeWidthPx(dp2px(strokeWidth.toFloat()))
    }

    fun setProgressBackgroundStrokeWidthPx(@Dimension strokeWidth: Int) {
        progressBackgroundPaint!!.strokeWidth = strokeWidth.toFloat()

        invalidateEverything()
    }

    fun setTextSizeSp(@Dimension size: Int) {
        setTextSizePx(sp2px(size.toFloat()))
    }

    fun setTextSizePx(@Dimension size: Int) {
        var size = size
        val currentSize = textPaint!!.textSize

        val factor = textPaint!!.measureText(progressText) / currentSize

        val offset = if (isDotEnabled) Math.max(
            dotPaint!!.strokeWidth,
            progressPaint!!.strokeWidth
        ) else progressPaint!!.strokeWidth
        val maximumAvailableTextWidth = circleBounds!!.width() - offset

        if (size * factor >= maximumAvailableTextWidth) {
            size = (maximumAvailableTextWidth / factor).toInt()
        }

        textPaint!!.textSize = size.toFloat()

        val textBounds = calculateTextBounds()
        invalidate(textBounds)
    }

    fun setShouldDrawDot(shouldDrawDot: Boolean) {
        this.isDotEnabled = shouldDrawDot

        if (dotPaint!!.strokeWidth > progressPaint!!.strokeWidth) {
            requestLayout()
            return
        }

        invalidate()
    }

    fun setDotWidthDp(@Dimension width: Int) {
        setDotWidthPx(dp2px(width.toFloat()))
    }

    fun setDotWidthPx(@Dimension width: Int) {
        dotPaint!!.strokeWidth = width.toFloat()

        invalidateEverything()
    }

    fun setProgressTextAdapter(progressTextAdapter: ProgressTextAdapter?) {

        if (progressTextAdapter != null) {
            this.progressTextAdapter = progressTextAdapter
        } else {
            this.progressTextAdapter = DefaultProgressTextAdapter()
        }

        reformatProgressText()

        invalidateEverything()
    }

    fun getProgressTextAdapter(): ProgressTextAdapter {
        return progressTextAdapter
    }

    fun getStartAngle(): Int {
        return startAngle
    }

    fun setStartAngle(@IntRange(from = 0, to = 360) startAngle: Int) {
        this.startAngle = startAngle
        invalidate()
    }

    @Direction
    fun getDirection(): Int {
        return direction
    }

    fun setDirection(@Direction direction: Int) {
        this.direction = direction
        invalidate()
    }

    fun setAnimationEnabled(enableAnimation: Boolean) {
        isAnimationEnabled = enableAnimation

        if (!enableAnimation) stopProgressAnimation()
    }

    fun isAnimationEnabled(): Boolean {
        return isAnimationEnabled
    }

    fun setFillBackgroundEnabled(fillBackgroundEnabled: Boolean) {
        if (fillBackgroundEnabled == isFillBackgroundEnabled) return

        isFillBackgroundEnabled = fillBackgroundEnabled

        val style = if (fillBackgroundEnabled) Paint.Style.FILL_AND_STROKE else Paint.Style.STROKE
        progressBackgroundPaint!!.style = style

        invalidate()
    }

    fun isFillBackgroundEnabled(): Boolean {
        return isFillBackgroundEnabled
    }

    fun setGradient(@GradientType type: Int, @ColorInt endColor: Int) {
        var gradient: Shader? = null

        val cx = width / 2f
        val cy = height / 2f

        val startColor = progressPaint!!.color

        when (type) {
            LINEAR_GRADIENT -> gradient = LinearGradient(
                0f,
                0f,
                width.toFloat(),
                height.toFloat(),
                startColor,
                endColor,
                Shader.TileMode.CLAMP
            )
            RADIAL_GRADIENT -> gradient =
                RadialGradient(cx, cy, cx, startColor, endColor, Shader.TileMode.MIRROR)
            SWEEP_GRADIENT -> gradient =
                SweepGradient(cx, cy, intArrayOf(startColor, endColor), null)
        }

        if (gradient != null) {
            val matrix = Matrix()
            matrix.postRotate(startAngle.toFloat(), cx, cy)
            gradient.setLocalMatrix(matrix)
        }

        progressPaint!!.shader = gradient

        invalidate()
    }

    @Retention(AnnotationRetention.SOURCE)
    @IntDef(DIRECTION_CLOCKWISE, DIRECTION_COUNTERCLOCKWISE)
    annotation class Direction

    @Retention(AnnotationRetention.SOURCE)
    @IntDef(CAP_ROUND, CAP_BUTT)
    annotation class Cap

    @Retention(AnnotationRetention.SOURCE)
    @IntDef(NO_GRADIENT, LINEAR_GRADIENT, RADIAL_GRADIENT, SWEEP_GRADIENT)
    annotation class GradientType

    interface ProgressTextAdapter {

        fun formatText(currentProgress: Double): String
    }

    interface OnProgressChangeListener {
        fun onProgressChanged(progress: Double, maxProgress: Double)
    }

    companion object {

        const val DIRECTION_CLOCKWISE = 0
        const val DIRECTION_COUNTERCLOCKWISE = 1

        const val CAP_ROUND = 0
        const val CAP_BUTT = 1

        const val NO_GRADIENT = 0
        const val LINEAR_GRADIENT = 1
        const val RADIAL_GRADIENT = 2
        const val SWEEP_GRADIENT = 3

        private const val DEFAULT_PROGRESS_START_ANGLE = 270
        private const val ANGLE_START_PROGRESS_BACKGROUND = 0
        private const val ANGLE_END_PROGRESS_BACKGROUND = 360

        private const val DESIRED_WIDTH_DP = 150

        private const val DEFAULT_PROGRESS_COLOR = "#3F51B5"
        private const val DEFAULT_TEXT_SIZE_SP = 24
        private const val DEFAULT_STROKE_WIDTH_DP = 8
        private const val DEFAULT_PROGRESS_BACKGROUND_COLOR = "#e0e0e0"

        private const val DEFAULT_ANIMATION_DURATION = 1000

        private const val PROPERTY_ANGLE = "angle"
    }
}
