package vn.kingbee.widget.recyclerview.alphabet

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.graphics.Typeface

import androidx.annotation.ColorInt
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.util.AttributeSet
import android.view.MotionEvent
import android.widget.SectionIndexer
import timber.log.Timber

class IndexFastScrollRecyclerSection(context: Context, rv: IndexFastScrollRecyclerView) : RecyclerView.AdapterDataObserver() {

    private var mIndexbarWidth: Float = 0.toFloat()
    private var mIndexbarMargin: Float = 0.toFloat()
    private val mPreviewPadding: Float
    private val mDensity: Float
    private val mScaledDensity: Float
    private var mListViewWidth: Int = 0
    private var mListViewHeight: Int = 0
    private var mCurrentSection = -1
    private var mIsIndexing = false
    private var mRecyclerView: RecyclerView? = null
    private var mIndexer: SectionIndexer? = null
    private var mSections: Array<String>? = null
    private var mIndexbarRect: RectF? = null

    private var setIndexTextSize: Int = 0
    private val setIndexbarWidth: Float
    private val setIndexbarMargin: Float
    private var setPreviewPadding: Int = 0
    private var previewVisibility = true
    private var setIndexBarCornerRadius: Int = 0
    private var setTypeface: Typeface? = null
    private var setIndexBarVisibility: Boolean? = true
    private var setSetIndexBarHighLateTextVisibility: Boolean? = false
    @ColorInt
    private var indexbarBackgroudColor: Int = 0
    @ColorInt
    private var indexbarTextColor: Int = 0
    @ColorInt
    private var indexbarHighLateTextColor: Int = 0

    private var setPreviewTextSize: Int = 0
    @ColorInt
    private var previewBackgroundColor: Int = 0
    @ColorInt
    private var previewTextColor: Int = 0
    private var previewBackgroudAlpha: Int = 0
    private var indexbarBackgroudAlpha: Int = 0

    private val indexPaintPaintColor = Color.WHITE
    internal var attrs: AttributeSet? = null

    private var mLastFadeRunnable: Runnable? = null

    init {

        setIndexTextSize = rv.setIndexTextSize
        setIndexbarWidth = rv.mIndexbarWidth
        setIndexbarMargin = rv.mIndexbarMargin
        setPreviewPadding = rv.mPreviewPadding
        setPreviewTextSize = rv.mPreviewTextSize
        previewBackgroundColor = rv.mPreviewBackgroudColor
        previewTextColor = rv.mPreviewTextColor
        previewBackgroudAlpha = convertTransparentValueToBackgroundAlpha(rv.mPreviewTransparentValue)

        setIndexBarCornerRadius = rv.mIndexBarCornerRadius
        indexbarBackgroudColor = rv.mIndexbarBackgroudColor
        indexbarTextColor = rv.mIndexbarTextColor
        indexbarHighLateTextColor = rv.mIndexbarHighLateTextColor

        indexbarBackgroudAlpha = convertTransparentValueToBackgroundAlpha(rv.mIndexBarTransparentValue)

        mDensity = context.resources.displayMetrics.density
        mScaledDensity = context.resources.displayMetrics.scaledDensity
        mRecyclerView = rv
        setAdapter(mRecyclerView!!.adapter)

        mIndexbarWidth = setIndexbarWidth * mDensity
        mIndexbarMargin = setIndexbarMargin * mDensity
        mPreviewPadding = setPreviewPadding * mDensity
    }

    fun draw(canvas: Canvas) {

        if (setIndexBarVisibility!!) {

            val indexbarPaint = Paint()
            indexbarPaint.color = indexbarBackgroudColor
            indexbarPaint.alpha = indexbarBackgroudAlpha
            indexbarPaint.isAntiAlias = true
            canvas.drawRoundRect(mIndexbarRect!!, setIndexBarCornerRadius * mDensity, setIndexBarCornerRadius * mDensity, indexbarPaint)

            if (mSections != null && mSections!!.size > 0) {
                // Preview is shown when mCurrentSection is set
                if (previewVisibility && mCurrentSection >= 0 && mSections!![mCurrentSection] !== "") {
                    val previewPaint = Paint()
                    previewPaint.color = previewBackgroundColor
                    previewPaint.alpha = previewBackgroudAlpha
                    previewPaint.isAntiAlias = true
                    previewPaint.setShadowLayer(3f, 0f, 0f, Color.argb(64, 0, 0, 0))

                    val previewTextPaint = Paint()
                    previewTextPaint.color = previewTextColor
                    previewTextPaint.isAntiAlias = true
                    previewTextPaint.textSize = setPreviewTextSize * mScaledDensity
                    previewTextPaint.typeface = setTypeface

                    val previewTextWidth = previewTextPaint.measureText(mSections!![mCurrentSection])
                    var previewSize = 2 * mPreviewPadding + previewTextPaint.descent() - previewTextPaint.ascent()
                    previewSize = Math.max(previewSize, previewTextWidth + 2 * mPreviewPadding)
                    val previewRect = RectF((mListViewWidth - previewSize) / 2, (mListViewHeight - previewSize) / 2, (mListViewWidth - previewSize) / 2 + previewSize, (mListViewHeight - previewSize) / 2 + previewSize)

                    canvas.drawRoundRect(previewRect, 5 * mDensity, 5 * mDensity, previewPaint)
                    canvas.drawText(mSections!![mCurrentSection], previewRect.left + (previewSize - previewTextWidth) / 2 - 1, previewRect.top + (previewSize - (previewTextPaint.descent() - previewTextPaint.ascent())) / 2 - previewTextPaint.ascent(), previewTextPaint)
                    fade(300)
                }

                val indexPaint = Paint()
                indexPaint.color = indexbarTextColor
                indexPaint.isAntiAlias = true
                indexPaint.textSize = setIndexTextSize * mScaledDensity
                indexPaint.typeface = setTypeface

                val sectionHeight = (mIndexbarRect!!.height() - 2 * mIndexbarMargin) / mSections!!.size
                val paddingTop = (sectionHeight - (indexPaint.descent() - indexPaint.ascent())) / 2
                for (i in mSections!!.indices) {

                    if (setSetIndexBarHighLateTextVisibility!!) {

                        if (mCurrentSection > -1 && i == mCurrentSection) {
                            indexPaint.typeface = Typeface.create(setTypeface, Typeface.BOLD)
                            indexPaint.textSize = (setIndexTextSize + 3) * mScaledDensity
                            indexPaint.color = indexbarHighLateTextColor
                        } else {
                            indexPaint.typeface = setTypeface
                            indexPaint.textSize = setIndexTextSize * mScaledDensity
                            indexPaint.color = indexbarTextColor
                        }
                        val paddingLeft = (mIndexbarWidth - indexPaint.measureText(mSections!![i])) / 2
                        canvas.drawText(mSections!![i], mIndexbarRect!!.left + paddingLeft, mIndexbarRect!!.top + mIndexbarMargin + sectionHeight * i + paddingTop - indexPaint.ascent(), indexPaint)


                    } else {
                        val paddingLeft = (mIndexbarWidth - indexPaint.measureText(mSections!![i])) / 2
                        canvas.drawText(mSections!![i], mIndexbarRect!!.left + paddingLeft, mIndexbarRect!!.top + mIndexbarMargin + sectionHeight * i + paddingTop - indexPaint.ascent(), indexPaint)
                    }

                }
            }
        }

    }

    fun onTouchEvent(ev: MotionEvent): Boolean {
        when (ev.action) {
            MotionEvent.ACTION_DOWN ->
                // If down event occurs inside index bar region, start indexing
                if (contains(ev.x, ev.y)) {

                    // It demonstrates that the motion event started from index bar
                    mIsIndexing = true
                    // Determine which section the point is in, and move the list to that section
                    mCurrentSection = getSectionByPoint(ev.y)
                    scrollToPosition()
                    return true
                }
            MotionEvent.ACTION_MOVE -> if (mIsIndexing) {
                // If this event moves inside index bar
                if (contains(ev.x, ev.y)) {
                    // Determine which section the point is in, and move the list to that section
                    mCurrentSection = getSectionByPoint(ev.y)
                    scrollToPosition()
                }
                return true
            }
            MotionEvent.ACTION_UP -> if (mIsIndexing) {
                mIsIndexing = false
                mCurrentSection = -1
            }
        }
        return false
    }

    private fun scrollToPosition() {
        try {
            val position = mIndexer!!.getPositionForSection(mCurrentSection)
            val layoutManager = mRecyclerView!!.layoutManager
            if (layoutManager is LinearLayoutManager) {
                layoutManager.scrollToPositionWithOffset(position, 0)
            } else {
                layoutManager!!.scrollToPosition(position)
            }
        } catch (e: Exception) {
            Timber.d("Data size returns null")
        }

    }

    fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        mListViewWidth = w
        mListViewHeight = h
        mIndexbarRect = RectF(w.toFloat() - mIndexbarMargin - mIndexbarWidth, mIndexbarMargin, w - mIndexbarMargin, h - mIndexbarMargin)
    }

    fun setAdapter(adapter: RecyclerView.Adapter<*>?) {
        if (adapter is SectionIndexer) {
            adapter.registerAdapterDataObserver(this)
            mIndexer = adapter
            mSections = mIndexer!!.sections.map { it as String }.toTypedArray()
        }
    }

    override fun onChanged() {
        super.onChanged()
        updateSections()
    }

    fun updateSections() {
        mSections = mIndexer!!.sections.map { it as String }.toTypedArray()
    }

    fun contains(x: Float, y: Float): Boolean {
        // Determine if the point is in index bar region, which includes the right margin of the bar
        return x >= mIndexbarRect!!.left && y >= mIndexbarRect!!.top && y <= mIndexbarRect!!.top + mIndexbarRect!!.height()
    }

    private fun getSectionByPoint(y: Float): Int {
        if (mSections == null || mSections!!.size == 0)
            return 0
        if (y < mIndexbarRect!!.top + mIndexbarMargin)
            return 0
        return if (y >= mIndexbarRect!!.top + mIndexbarRect!!.height() - mIndexbarMargin) mSections!!.size - 1 else ((y - mIndexbarRect!!.top - mIndexbarMargin) / ((mIndexbarRect!!.height() - 2 * mIndexbarMargin) / mSections!!.size)).toInt()
    }

    private fun fade(delay: Long) {
        if (mRecyclerView != null) {
            if (mLastFadeRunnable != null) {
                mRecyclerView!!.removeCallbacks(mLastFadeRunnable)
            }
            mLastFadeRunnable = Runnable { mRecyclerView!!.invalidate() }
            mRecyclerView!!.postDelayed(mLastFadeRunnable, delay)
        }
    }

    private fun convertTransparentValueToBackgroundAlpha(value: Float): Int {
        return (255 * value).toInt()
    }

    /**
     * @param value int to set the text size of the index bar
     */
    fun setIndexTextSize(value: Int) {
        setIndexTextSize = value
    }

    /**
     * @param value float to set the width of the index bar
     */
    fun setIndexbarWidth(value: Float) {
        mIndexbarWidth = value
    }

    /**
     * @param value float to set the margin of the index bar
     */
    fun setIndexbarMargin(value: Float) {
        mIndexbarMargin = value
    }

    /**
     * @param value int to set preview padding
     */
    fun setPreviewPadding(value: Int) {
        setPreviewPadding = value
    }

    /**
     * @param value int to set the radius of the index bar
     */
    fun setIndexBarCornerRadius(value: Int) {
        setIndexBarCornerRadius = value
    }

    /**
     * @param value float to set the transparency of the color for index bar
     */
    fun setIndexBarTransparentValue(value: Float) {
        indexbarBackgroudAlpha = convertTransparentValueToBackgroundAlpha(value)
    }

    /**
     * @param typeface Typeface to set the typeface of the preview & the index bar
     */
    fun setTypeface(typeface: Typeface) {
        setTypeface = typeface
    }

    /**
     * @param shown boolean to show or hide the index bar
     */
    fun setIndexBarVisibility(shown: Boolean) {
        setIndexBarVisibility = shown
    }

    /**
     * @param shown boolean to show or hide the preview box
     */
    fun setPreviewVisibility(shown: Boolean) {
        previewVisibility = shown
    }

    /**
     * @param value int to set the text size of the preview box
     */
    fun setPreviewTextSize(value: Int) {
        setPreviewTextSize = value
    }

    /**
     * @param color The color for the preview box
     */
    fun setPreviewColor(@ColorInt color: Int) {
        previewBackgroundColor = color
    }

    /**
     * @param color The text color for the preview box
     */
    fun setPreviewTextColor(@ColorInt color: Int) {
        previewTextColor = color
    }

    /**
     * @param value float to set the transparency value of the preview box
     */
    fun setPreviewTransparentValue(value: Float) {
        previewBackgroudAlpha = convertTransparentValueToBackgroundAlpha(value)
    }

    /**
     * @param color The color for the scroll track
     */
    fun setIndexBarColor(@ColorInt color: Int) {
        indexbarBackgroudColor = color
    }

    /**
     * @param color The text color for the index bar
     */
    fun setIndexBarTextColor(@ColorInt color: Int) {
        indexbarTextColor = color
    }

    /**
     * @param color The text color for the index bar
     */
    fun setIndexBarHighLateTextColor(@ColorInt color: Int) {
        indexbarHighLateTextColor = color
    }

    /**
     * @param shown boolean to show or hide the index bar
     */
    fun setIndexBarHighLateTextVisibility(shown: Boolean) {
        setSetIndexBarHighLateTextVisibility = shown
    }

    inline fun <reified T> toArray(list: List<*>): Array<T> {
        return (list as List<T>).toTypedArray()
    }

}