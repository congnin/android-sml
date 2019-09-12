package vn.kingbee.widget.signature.views

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Resources
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.ViewTreeObserver
import vn.kingbee.widget.R
import vn.kingbee.widget.signature.utils.Bezier
import vn.kingbee.widget.signature.utils.ControlTimedPoints
import vn.kingbee.widget.signature.utils.SvgBuilder
import vn.kingbee.widget.signature.utils.TimedPoint
import vn.kingbee.widget.signature.view.ViewCompat
import vn.kingbee.widget.signature.view.ViewTreeObserverCompat
import java.util.ArrayList

class SignaturePad : View {
    //View state
    private var mPoints: MutableList<TimedPoint>? = null
    private var mIsEmpty: Boolean = false
    private var mLastTouchX: Float = 0.toFloat()
    private var mLastTouchY: Float = 0.toFloat()
    private var mLastVelocity: Float = 0.toFloat()
    private var mLastWidth: Float = 0.toFloat()
    private var mDirtyRect: RectF? = null

    private val mSvgBuilder = SvgBuilder()

    // Cache
    private val mPointsCache = ArrayList<TimedPoint>()
    private val mControlTimedPointsCached = ControlTimedPoints()
    private val mBezierCached = Bezier()

    //Configurable parameters
    private var mMinWidth: Int = 0
    private var mMaxWidth: Int = 0
    private var mVelocityFilterWeight: Float = 0.toFloat()
    private var mOnSignedListener: OnSignedListener? = null
    private var mClearOnDoubleClick: Boolean = false

    //Click values
    private var mFirstClick: Long = 0
    private var mCountClick: Int = 0

    private val mPaint = Paint()
    private var mSignatureBitmap: Bitmap? = null
    private var mSignatureBitmapCanvas: Canvas? = null

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {

        val a = context.theme.obtainStyledAttributes(
            attrs, R.styleable.SignaturePad, 0, 0
        )

        //Configurable parameters
        try {
            mMinWidth = a.getDimensionPixelSize(
                R.styleable.SignaturePad_penMinWidth,
                convertDpToPx(DEFAULT_ATTR_PEN_MIN_WIDTH_PX.toFloat())
            )
            mMaxWidth = a.getDimensionPixelSize(
                R.styleable.SignaturePad_penMaxWidth,
                convertDpToPx(DEFAULT_ATTR_PEN_MAX_WIDTH_PX.toFloat())
            )
            mPaint.color = a.getColor(R.styleable.SignaturePad_penColor, DEFAULT_ATTR_PEN_COLOR)
            mVelocityFilterWeight = a.getFloat(
                R.styleable.SignaturePad_velocityFilterWeight, DEFAULT_ATTR_VELOCITY_FILTER_WEIGHT
            )
            mClearOnDoubleClick = a.getBoolean(
                R.styleable.SignaturePad_clearOnDoubleClick, DEFAULT_ATTR_CLEAR_ON_DOUBLE_CLICK
            )
        } finally {
            a.recycle()
        }

        //Fixed parameters
        mPaint.isAntiAlias = true
        mPaint.style = Paint.Style.STROKE
        mPaint.strokeCap = Paint.Cap.ROUND
        mPaint.strokeJoin = Paint.Join.ROUND

        //Dirty rectangle to update only the changed portion of the view
        mDirtyRect = RectF()

        clear()
    }

    /**
     * Set the pen color from a given resource.
     * If the resource is not found, [Color.BLACK] is assumed.
     *
     * @param colorRes the color resource.
     */
    fun setPenColorRes(colorRes: Int) {
        try {
            setPenColor(resources.getColor(colorRes))
        } catch (ex: Resources.NotFoundException) {
            setPenColor(Color.parseColor("#000000"))
        }

    }

    /**
     * Set the pen color from a given color.
     *
     * @param color the color.
     */
    fun setPenColor(color: Int) {
        mPaint.color = color
    }

    /**
     * Set the minimum width of the stroke in pixel.
     *
     * @param minWidth the width in dp.
     */
    fun setMinWidth(minWidth: Float) {
        mMinWidth = convertDpToPx(minWidth)
    }

    /**
     * Set the maximum width of the stroke in pixel.
     *
     * @param maxWidth the width in dp.
     */
    fun setMaxWidth(maxWidth: Float) {
        mMaxWidth = convertDpToPx(maxWidth)
    }

    /**
     * Set the velocity filter weight.
     *
     * @param velocityFilterWeight the weight.
     */
    fun setVelocityFilterWeight(velocityFilterWeight: Float) {
        mVelocityFilterWeight = velocityFilterWeight
    }

    fun clear() {
        mSvgBuilder.clear()
        mPoints = ArrayList<TimedPoint>()
        mLastVelocity = 0f
        mLastWidth = ((mMinWidth + mMaxWidth) / 2).toFloat()

        if (mSignatureBitmap != null) {
            mSignatureBitmap = null
            ensureSignatureBitmap()
        }
        setIsEmpty(true)

        invalidate()
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (!isEnabled) return false

        val eventX = event.x
        val eventY = event.y

        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                parent.requestDisallowInterceptTouchEvent(true)
                mPoints!!.clear()
                if (!isDoubleClick()){
                    mLastTouchX = eventX
                    mLastTouchY = eventY
                    addPoint(getNewPoint(eventX, eventY))
                    if (mOnSignedListener != null) mOnSignedListener!!.onStartSigning()
                    resetDirtyRect(eventX, eventY)
                    addPoint(getNewPoint(eventX, eventY))
                }
            }

            MotionEvent.ACTION_MOVE -> {
                resetDirtyRect(eventX, eventY)
                addPoint(getNewPoint(eventX, eventY))
            }

            MotionEvent.ACTION_UP -> {
                resetDirtyRect(eventX, eventY)
                addPoint(getNewPoint(eventX, eventY))
                parent.requestDisallowInterceptTouchEvent(true)
                val d = Math.sqrt(
                    Math.pow(
                        (eventX - mLastTouchX).toDouble(), 2.0
                    ) + Math.pow((eventY - mLastTouchY).toDouble(), 2.0)
                )
                if (d > MIN_ELIGIBLE_DISTANCE) {
                    setIsEmpty(false)
                }
            }

            else -> return false
        }

        //invalidate()
        invalidate(
            (mDirtyRect!!.left - mMaxWidth).toInt(),
            (mDirtyRect!!.top - mMaxWidth).toInt(),
            (mDirtyRect!!.right + mMaxWidth).toInt(),
            (mDirtyRect!!.bottom + mMaxWidth).toInt()
        )

        return true
    }

    override fun onDraw(canvas: Canvas) {
        if (mSignatureBitmap != null) {
            canvas.drawBitmap(mSignatureBitmap!!, 0f, 0f, mPaint)
        }
    }

    fun setOnSignedListener(listener: OnSignedListener) {
        mOnSignedListener = listener
    }

    fun isEmpty(): Boolean {
        return mIsEmpty
    }

    fun getSignatureSvg(): String {
        val width = getTransparentSignatureBitmap()!!.width
        val height = getTransparentSignatureBitmap()!!.height
        return mSvgBuilder.build(width, height)
    }

    fun getSignatureBitmap(): Bitmap {
        val originalBitmap = getTransparentSignatureBitmap()
        val whiteBgBitmap = Bitmap.createBitmap(
            originalBitmap!!.width, originalBitmap.height, Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(whiteBgBitmap)
        canvas.drawColor(Color.WHITE)
        canvas.drawBitmap(originalBitmap, 0f, 0f, null)
        return whiteBgBitmap
    }

    /**
     * @param compressPercentage Hint to the compressor, 0-100 percent. 0 meaning compress for
     * small size, 100 meaning compress for max quality. Some
     * formats, like PNG which is lossless, will ignore the
     * quality setting
     */
    fun getCompressedSignatureBitmap(compressPercentage: Int): Bitmap {
        var compressPercentage = compressPercentage

        if (compressPercentage < 0) {
            compressPercentage = 0
        } else if (compressPercentage > 100) {
            compressPercentage = 100
        }
        val originalBitmap = getTransparentSignatureBitmap()
        val originalWidth = originalBitmap!!.width
        val originalHeight = originalBitmap.height

        val targetWidth = originalWidth * compressPercentage / 100 // your arbitrary fixed limit
        val targetHeight = (originalHeight * targetWidth / originalWidth.toDouble()).toInt()

        var whiteBgBitmap =
            Bitmap.createBitmap(originalWidth, originalHeight, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(whiteBgBitmap)
        canvas.drawColor(Color.WHITE)
        canvas.drawBitmap(originalBitmap, 0f, 0f, null)
        whiteBgBitmap = Bitmap.createScaledBitmap(originalBitmap, targetWidth, targetHeight, true)
        return whiteBgBitmap
    }

    /**
     * @param deiredWidth Desired width of the bitmap
     */
    fun getFixedSizeSignatureBitmap(deiredWidth: Int): Bitmap {

        val originalBitmap = getTransparentSignatureBitmap()
        val originalWidth = originalBitmap!!.width
        val originalHeight = originalBitmap.height

        val targetHeight = (originalHeight * deiredWidth / originalWidth.toDouble()).toInt()

        var whiteBgBitmap =
            Bitmap.createBitmap(originalWidth, originalHeight, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(whiteBgBitmap)
        canvas.drawColor(Color.WHITE)
        canvas.drawBitmap(originalBitmap, 0f, 0f, null)
        whiteBgBitmap = Bitmap.createScaledBitmap(originalBitmap, deiredWidth, targetHeight, true)
        return whiteBgBitmap
    }

    /**
     * @param deiredWidth Desired width of the bitmap
     */
    fun getFixedSizeSignatureBitmap(deiredWidth: Int, desiredHeight: Int): Bitmap {

        val originalBitmap = getTransparentSignatureBitmap()
        val originalWidth = originalBitmap!!.width
        val originalHeight = originalBitmap.height

        var whiteBgBitmap =
            Bitmap.createBitmap(originalWidth, originalHeight, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(whiteBgBitmap)
        canvas.drawColor(Color.WHITE)
        canvas.drawBitmap(originalBitmap, 0f, 0f, null)
        whiteBgBitmap = Bitmap.createScaledBitmap(originalBitmap, deiredWidth, desiredHeight, true)
        return whiteBgBitmap
    }

    fun setSignatureBitmap(signature: Bitmap) {
        // View was laid out...
        if (ViewCompat.isLaidOut(this)) {
            clear()
            ensureSignatureBitmap()

            val tempSrc = RectF()
            val tempDst = RectF()

            val dWidth = signature.width
            val dHeight = signature.height
            val vWidth = width
            val vHeight = height

            // Generate the required transform.
            tempSrc.set(0f, 0f, dWidth.toFloat(), dHeight.toFloat())
            tempDst.set(0f, 0f, vWidth.toFloat(), vHeight.toFloat())

            val drawMatrix = Matrix()
            drawMatrix.setRectToRect(tempSrc, tempDst, Matrix.ScaleToFit.CENTER)

            val canvas = Canvas(mSignatureBitmap!!)
            canvas.drawBitmap(signature, drawMatrix, null)
            setIsEmpty(false)
            invalidate()
        } else {
            viewTreeObserver.addOnGlobalLayoutListener(object :
                ViewTreeObserver.OnGlobalLayoutListener {
                override fun onGlobalLayout() {
                    // Remove layout listener...
                    ViewTreeObserverCompat.removeOnGlobalLayoutListener(viewTreeObserver, this)

                    // Signature bitmap...
                    setSignatureBitmap(signature)
                }
            })
        }// View not laid out yet e.g. called from onCreate(), onRestoreInstanceState()...
    }

    fun getTransparentSignatureBitmap(): Bitmap? {
        ensureSignatureBitmap()
        return mSignatureBitmap
    }

    fun getTransparentSignatureBitmap(trimBlankSpace: Boolean): Bitmap? {

        if (!trimBlankSpace) {
            return getTransparentSignatureBitmap()
        }

        ensureSignatureBitmap()

        val imgHeight = mSignatureBitmap!!.height
        val imgWidth = mSignatureBitmap!!.width

        val backgroundColor = Color.TRANSPARENT

        var xMin = Integer.MAX_VALUE
        var xMax = Integer.MIN_VALUE
        var yMin = Integer.MAX_VALUE
        var yMax = Integer.MIN_VALUE

        var foundPixel = false

        // Find xMin
        for (x in 0 until imgWidth) {
            var stop = false
            for (y in 0 until imgHeight) {
                if (mSignatureBitmap!!.getPixel(x, y) != backgroundColor) {
                    xMin = x
                    stop = true
                    foundPixel = true
                    break
                }
            }
            if (stop) break
        }

        // Image is empty...
        if (!foundPixel) return null

        // Find yMin
        for (y in 0 until imgHeight) {
            var stop = false
            for (x in xMin until imgWidth) {
                if (mSignatureBitmap!!.getPixel(x, y) != backgroundColor) {
                    yMin = y
                    stop = true
                    break
                }
            }
            if (stop) break
        }

        // Find xMax
        for (x in imgWidth - 1 downTo xMin) {
            var stop = false
            for (y in yMin until imgHeight) {
                if (mSignatureBitmap!!.getPixel(x, y) != backgroundColor) {
                    xMax = x
                    stop = true
                    break
                }
            }
            if (stop) break
        }

        // Find yMax
        for (y in imgHeight - 1 downTo yMin) {
            var stop = false
            for (x in xMin..xMax) {
                if (mSignatureBitmap!!.getPixel(x, y) != backgroundColor) {
                    yMax = y
                    stop = true
                    break
                }
            }
            if (stop) break
        }

        return Bitmap.createBitmap(mSignatureBitmap!!, xMin, yMin, xMax - xMin, yMax - yMin)
    }

    private fun isDoubleClick(): Boolean {
        if (mClearOnDoubleClick) {
            if (mFirstClick != 0L && System.currentTimeMillis() - mFirstClick > DOUBLE_CLICK_DELAY_MS) {
                mCountClick = 0
            }
            mCountClick++
            if (mCountClick == 1) {
                mFirstClick = System.currentTimeMillis()
            } else if (mCountClick == 2) {
                val lastClick = System.currentTimeMillis()
                if (lastClick - mFirstClick < DOUBLE_CLICK_DELAY_MS) {
                    this.clear()
                    return true
                }
            }
        }
        return false
    }

    private fun getNewPoint(x: Float, y: Float): TimedPoint {
        val mCacheSize = mPointsCache.size
        val timedPoint: TimedPoint
        if (mCacheSize == 0) {
            // Cache is empty, create a new point
            timedPoint = TimedPoint()
        } else {
            // Get point from cache
            timedPoint = mPointsCache.removeAt(mCacheSize - 1)
        }

        return timedPoint.set(x, y)
    }

    private fun recyclePoint(point: TimedPoint) {
        mPointsCache.add(point)
    }

    private fun addPoint(newPoint: TimedPoint) {
        mPoints!!.add(newPoint)

        val pointsCount = mPoints!!.size
        if (pointsCount > 3) {

            var tmp = calculateCurveControlPoints(mPoints!![0], mPoints!![1], mPoints!![2])
            val c2 = tmp.c2
            recyclePoint(tmp.c1!!)

            tmp = calculateCurveControlPoints(mPoints!![1], mPoints!![2], mPoints!![3])
            val c3 = tmp.c1
            recyclePoint(tmp.c2!!)

            val curve = mBezierCached.set(mPoints!![1], c2!!, c3!!, mPoints!![2])

            val startPoint = curve.startPoint
            val endPoint = curve.endPoint

            var velocity = endPoint?.velocityFrom(startPoint!!)
            velocity = if (java.lang.Float.isNaN(velocity!!)) 0.0f else velocity

            velocity =
                mVelocityFilterWeight * velocity + (1 - mVelocityFilterWeight) * mLastVelocity

            // The new width is a function of the velocity. Higher velocities
            // correspond to thinner strokes.
            val newWidth = strokeWidth(velocity)

            // The Bezier's width starts out as last curve's final width, and
            // gradually changes to the stroke width just calculated. The new
            // width calculation is based on the velocity between the Bezier's
            // start and end mPoints.
            addBezier(curve, mLastWidth, newWidth)

            mLastVelocity = velocity
            mLastWidth = newWidth

            // Remove the first element from the list,
            // so that we always have no more than 4 mPoints in mPoints array.
            recyclePoint(mPoints!!.removeAt(0))

            recyclePoint(c2)
            recyclePoint(c3)

        } else if (pointsCount == 1) {
            // To reduce the initial lag make it work with 3 mPoints
            // by duplicating the first point
            val firstPoint = mPoints!![0]
            mPoints!!.add(getNewPoint(firstPoint.x, firstPoint.y))
        }
    }

    private fun addBezier(curve: Bezier, startWidth: Float, endWidth: Float) {
        mSvgBuilder.append(curve, (startWidth + endWidth) / 2)
        ensureSignatureBitmap()
        val originalWidth = mPaint.strokeWidth
        val widthDelta = endWidth - startWidth
        val drawSteps = Math.floor(curve.length().toDouble()).toFloat()

        var i = 0
        while (i < drawSteps) {
            // Calculate the Bezier (x, y) coordinate for this step.
            val t = i.toFloat() / drawSteps
            val tt = t * t
            val ttt = tt * t
            val u = 1 - t
            val uu = u * u
            val uuu = uu * u

            var x = uuu * curve.startPoint!!.x
            x += 3 * uu * t * curve.control1!!.x
            x += 3 * u * tt * curve.control2!!.x
            x += ttt * curve.endPoint!!.x

            var y = uuu * curve.startPoint!!.y
            y += 3 * uu * t * curve.control1!!.y
            y += 3 * u * tt * curve.control2!!.y
            y += ttt * curve.endPoint!!.y

            // Set the incremental stroke width and draw.
            mPaint.strokeWidth = startWidth + ttt * widthDelta
            mSignatureBitmapCanvas!!.drawPoint(x, y, mPaint)
            expandDirtyRect(x, y)
            i++
        }

        mPaint.strokeWidth = originalWidth
    }

    private fun calculateCurveControlPoints(s1: TimedPoint,
                                            s2: TimedPoint,
                                            s3: TimedPoint): ControlTimedPoints {
        val dx1 = s1.x - s2.x
        val dy1 = s1.y - s2.y
        val dx2 = s2.x - s3.x
        val dy2 = s2.y - s3.y

        val m1X = (s1.x + s2.x) / 2.0f
        val m1Y = (s1.y + s2.y) / 2.0f
        val m2X = (s2.x + s3.x) / 2.0f
        val m2Y = (s2.y + s3.y) / 2.0f

        val l1 = Math.sqrt((dx1 * dx1 + dy1 * dy1).toDouble()).toFloat()
        val l2 = Math.sqrt((dx2 * dx2 + dy2 * dy2).toDouble()).toFloat()

        val dxm = m1X - m2X
        val dym = m1Y - m2Y
        var k = l2 / (l1 + l2)
        if (java.lang.Float.isNaN(k)) k = 0.0f
        val cmX = m2X + dxm * k
        val cmY = m2Y + dym * k

        val tx = s2.x - cmX
        val ty = s2.y - cmY

        return mControlTimedPointsCached.set(
            getNewPoint(m1X + tx, m1Y + ty), getNewPoint(m2X + tx, m2Y + ty)
        )
    }

    private fun strokeWidth(velocity: Float): Float {
        return Math.max(mMaxWidth / (velocity + 1), mMinWidth.toFloat())
    }

    /**
     * Called when replaying history to ensure the dirty region includes all
     * mPoints.
     *
     * @param historicalX the previous x coordinate.
     * @param historicalY the previous y coordinate.
     */
    private fun expandDirtyRect(historicalX: Float, historicalY: Float) {
        if (historicalX < mDirtyRect!!.left) {
            mDirtyRect!!.left = historicalX
        } else if (historicalX > mDirtyRect!!.right) {
            mDirtyRect!!.right = historicalX
        }
        if (historicalY < mDirtyRect!!.top) {
            mDirtyRect!!.top = historicalY
        } else if (historicalY > mDirtyRect!!.bottom) {
            mDirtyRect!!.bottom = historicalY
        }
    }

    /**
     * Resets the dirty region when the motion event occurs.
     *
     * @param eventX the event x coordinate.
     * @param eventY the event y coordinate.
     */
    private fun resetDirtyRect(eventX: Float, eventY: Float) {

        // The mLastTouchX and mLastTouchY were set when the ACTION_DOWN motion event occurred.
        mDirtyRect!!.left = Math.min(mLastTouchX, eventX)
        mDirtyRect!!.right = Math.max(mLastTouchX, eventX)
        mDirtyRect!!.top = Math.min(mLastTouchY, eventY)
        mDirtyRect!!.bottom = Math.max(mLastTouchY, eventY)
    }

    private fun setIsEmpty(newValue: Boolean) {
        mIsEmpty = newValue
        if (mOnSignedListener != null) {
            if (mIsEmpty) {
                mOnSignedListener!!.onClear()
            } else {
                mOnSignedListener!!.onSigned()
            }
        }
    }

    private fun ensureSignatureBitmap() {
        if (mSignatureBitmap == null) {
            mSignatureBitmap = Bitmap.createBitmap(
                width, height, Bitmap.Config.ARGB_8888
            )
            mSignatureBitmapCanvas = Canvas(mSignatureBitmap!!)
        }
    }

    private fun convertDpToPx(dp: Float): Int {
        return Math.round(context.resources.displayMetrics.density * dp)
    }

    interface OnSignedListener {
        fun onStartSigning()

        fun onSigned()

        fun onClear()
    }

    companion object {
        private const val MIN_ELIGIBLE_DISTANCE = 10.0
        private const val DOUBLE_CLICK_DELAY_MS = 200

        //Default attribute values
        private const val DEFAULT_ATTR_PEN_MIN_WIDTH_PX = 3
        private const val DEFAULT_ATTR_PEN_MAX_WIDTH_PX = 7
        private const val DEFAULT_ATTR_PEN_COLOR = Color.BLACK
        private const val DEFAULT_ATTR_VELOCITY_FILTER_WEIGHT = 0.9f
        private const val DEFAULT_ATTR_CLEAR_ON_DOUBLE_CLICK = false
    }

}