package vn.kingbee.widget.progress.circle

import android.graphics.Color
import android.graphics.Paint
import android.view.View
import android.graphics.RectF
import android.content.Context
import android.content.res.TypedArray
import android.graphics.Canvas
import android.util.AttributeSet
import android.os.Bundle
import android.os.Parcelable
import vn.kingbee.widget.R

class CircleProgress : View {
    private var finishedPaint: Paint? = null
    private var unfinishedPaint: Paint? = null

    private val finishedOuterRect = RectF()
    private val unfinishedOuterRect = RectF()

    private var progress = 0f
    private var max = 100
    private var finishedStrokeColor: Int = 0
    private var unfinishedStrokeColor: Int = 0
    private var startingDegree: Int = 0
    private var stokeWidth: Float = 0.toFloat()
    private val defaultFinishedColor = Color.rgb(66, 145, 241)
    private val defaultUnfinishedColor = Color.rgb(204, 204, 204)

    private val INSTANCE_STATE = "saved_instance"
    private val INSTANCE_FINISHED_STROKE_COLOR = "finished_stroke_color"
    private val INSTANCE_UNFINISHED_STROKE_COLOR = "unfinished_stroke_color"
    private val INSTANCE_MAX = "max"
    private val INSTANCE_PROGRESS = "progress"
    private val INSTANCE_STARTING_DEGREE = "starting_degree"

    constructor(context: Context) : this(context, null)

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        val attributes = context.getTheme().obtainStyledAttributes(
            attrs,
            R.styleable.CircleProgress,
            defStyleAttr,
            0
        )
        initByAttributes(attributes)
        attributes.recycle()
        initPainters()
    }

    protected fun initPainters() {
        finishedPaint = Paint()
        finishedPaint?.color = finishedStrokeColor
        finishedPaint?.style = Paint.Style.STROKE
        finishedPaint?.isAntiAlias = true
        finishedPaint?.strokeWidth = stokeWidth

        unfinishedPaint = Paint()
        unfinishedPaint?.color = unfinishedStrokeColor
        unfinishedPaint?.style = Paint.Style.STROKE
        unfinishedPaint?.isAntiAlias = true
        unfinishedPaint?.strokeWidth = stokeWidth
    }

    protected fun initByAttributes(attributes: TypedArray) {
        finishedStrokeColor = attributes.getColor(
            R.styleable.CircleProgress_finished_color,
            defaultFinishedColor
        )
        unfinishedStrokeColor = attributes.getColor(
            R.styleable.CircleProgress_unfinished_color,
            defaultUnfinishedColor
        )

        setMax(attributes.getInt(R.styleable.CircleProgress_max, 100))
        setProgress(attributes.getFloat(R.styleable.CircleProgress_progress, 0f))
        stokeWidth = attributes.getDimension(R.styleable.CircleProgress_stoke_width, 1f)

        startingDegree = attributes.getInt(
            R.styleable.CircleProgress_circle_starting_degree,
            0
        )
    }

    override fun invalidate() {
        initPainters()
        super.invalidate()
    }

    private fun getProgressAngle(): Float {
        return getProgress() / max.toFloat() * 360f
    }

    fun getProgress(): Float {
        return progress
    }

    fun setProgress(progress: Float) {
        this.progress = progress
        if (this.progress > getMax()) {
            this.progress %= getMax()
        }
        invalidate()
    }

    fun getMax(): Int {
        return max
    }

    fun setMax(max: Int) {
        if (max > 0) {
            this.max = max
            invalidate()
        }
    }

    fun getStartingDegree(): Int {
        return startingDegree
    }

    fun setStartingDegree(startingDegree: Int) {
        this.startingDegree = startingDegree
        this.invalidate()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        setMeasuredDimension(measure(widthMeasureSpec), measure(heightMeasureSpec))
    }

    private fun measure(measureSpec: Int): Int {
        var result: Int
        val mode = View.MeasureSpec.getMode(measureSpec)
        val size = View.MeasureSpec.getSize(measureSpec)
        if (mode == View.MeasureSpec.EXACTLY) {
            result = size
        } else {
            result = 100
            if (mode == View.MeasureSpec.AT_MOST) {
                result = Math.min(result, size)
            }
        }
        return result
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        finishedOuterRect.set(
            stokeWidth,
            stokeWidth,
            width - stokeWidth,
            height - stokeWidth
        )
        unfinishedOuterRect.set(
            stokeWidth,
            stokeWidth,
            width - stokeWidth,
            height - stokeWidth
        )

        canvas.drawArc(
            finishedOuterRect,
            getStartingDegree().toFloat(),
            getProgressAngle(),
            false,
            finishedPaint!!
        )
        canvas.drawArc(
            unfinishedOuterRect,
            getStartingDegree() + getProgressAngle(),
            360 - getProgressAngle(),
            false,
            unfinishedPaint!!
        )
    }

    override fun onSaveInstanceState(): Parcelable? {
        val bundle = Bundle()
        bundle.putParcelable(INSTANCE_STATE, super.onSaveInstanceState())
        bundle.putInt(INSTANCE_FINISHED_STROKE_COLOR, finishedStrokeColor)
        bundle.putInt(INSTANCE_UNFINISHED_STROKE_COLOR, unfinishedStrokeColor)
        bundle.putInt(INSTANCE_MAX, getMax())
        bundle.putInt(INSTANCE_STARTING_DEGREE, getStartingDegree())
        bundle.putFloat(INSTANCE_PROGRESS, getProgress())
        return bundle
    }

    override fun onRestoreInstanceState(state: Parcelable) {
        if (state is Bundle) {
            finishedStrokeColor = state.getInt(INSTANCE_FINISHED_STROKE_COLOR)
            unfinishedStrokeColor = state.getInt(INSTANCE_UNFINISHED_STROKE_COLOR)
            initPainters()
            setMax(state.getInt(INSTANCE_MAX))
            setStartingDegree(state.getInt(INSTANCE_STARTING_DEGREE))
            setProgress(state.getFloat(INSTANCE_PROGRESS))
            super.onRestoreInstanceState(state.getParcelable(INSTANCE_STATE))
            return
        }
        super.onRestoreInstanceState(state)
    }
}