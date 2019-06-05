package vn.kingbee.widget.recyclerview.alphabet

import android.content.Context
import android.graphics.Canvas
import androidx.annotation.ColorRes
import android.graphics.Typeface
import android.view.MotionEvent
import android.view.GestureDetector
import android.graphics.Color
import android.util.AttributeSet
import androidx.annotation.ColorInt
import androidx.recyclerview.widget.RecyclerView
import vn.kingbee.widget.R


class IndexFastScrollRecyclerView : RecyclerView {

    private var mScroller: IndexFastScrollRecyclerSection? = null
    private var mGestureDetector: GestureDetector? = null

    private var mEnabled = true

    var setIndexTextSize = 12
    var mIndexbarWidth = 20f
    var mIndexbarMargin = 5f
    var mPreviewPadding = 5
    var mIndexBarCornerRadius = 5
    var mIndexBarTransparentValue = 0.6.toFloat()
    @ColorInt
    var mIndexbarBackgroudColor = Color.BLACK
    @ColorInt
    var mIndexbarTextColor = Color.WHITE
    @ColorInt
    var mIndexbarHighLateTextColor = Color.BLACK

    var mPreviewTextSize = 50
    @ColorInt
    var mPreviewBackgroudColor = Color.BLACK
    @ColorInt
    var mPreviewTextColor = Color.WHITE
    var mPreviewTransparentValue = 0.4.toFloat()

    constructor(context: Context) : super(context) {}

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle) {
        init(context, attrs)
    }


    private fun init(context: Context, attrs: AttributeSet?) {
        mScroller = IndexFastScrollRecyclerSection(context, this)

        if (attrs != null) {
            val typedArray = context.obtainStyledAttributes(attrs, R.styleable.IndexFastScrollRecyclerView, 0, 0)

            if (typedArray != null) {
                try {
                    setIndexTextSize = typedArray.getInt(R.styleable.IndexFastScrollRecyclerView_setIndexTextSize, setIndexTextSize)
                    mIndexbarWidth = typedArray.getFloat(R.styleable.IndexFastScrollRecyclerView_setIndexbarWidth, mIndexbarWidth)
                    mIndexbarMargin = typedArray.getFloat(R.styleable.IndexFastScrollRecyclerView_setIndexbarMargin, mIndexbarMargin)
                    mPreviewPadding = typedArray.getInt(R.styleable.IndexFastScrollRecyclerView_setPreviewPadding, mPreviewPadding)
                    mIndexBarCornerRadius = typedArray.getInt(R.styleable.IndexFastScrollRecyclerView_setIndexBarCornerRadius, mIndexBarCornerRadius)
                    mIndexBarTransparentValue = typedArray.getFloat(R.styleable.IndexFastScrollRecyclerView_setIndexBarTransparentValue, mIndexBarTransparentValue)

                    if (typedArray.hasValue(R.styleable.IndexFastScrollRecyclerView_setIndexBarColor)) {
                        mIndexbarBackgroudColor = Color.parseColor(typedArray.getString(R.styleable.IndexFastScrollRecyclerView_setIndexBarColor))
                    }

                    if (typedArray.hasValue(R.styleable.IndexFastScrollRecyclerView_setIndexBarTextColor)) {
                        mIndexbarTextColor = Color.parseColor(typedArray.getString(R.styleable.IndexFastScrollRecyclerView_setIndexBarTextColor))
                    }

                    if (typedArray.hasValue(R.styleable.IndexFastScrollRecyclerView_setIndexBarHighlightTextColor)) {
                        mIndexbarHighLateTextColor = Color.parseColor(typedArray.getString(R.styleable.IndexFastScrollRecyclerView_setIndexBarHighlightTextColor))
                    }

                    if (typedArray.hasValue(R.styleable.IndexFastScrollRecyclerView_setIndexBarColorRes)) {
                        mIndexbarBackgroudColor = typedArray.getColor(R.styleable.IndexFastScrollRecyclerView_setIndexBarColorRes, mIndexbarBackgroudColor)
                    }

                    if (typedArray.hasValue(R.styleable.IndexFastScrollRecyclerView_setIndexBarTextColorRes)) {
                        mIndexbarTextColor = typedArray.getColor(R.styleable.IndexFastScrollRecyclerView_setIndexBarTextColorRes, mIndexbarTextColor)
                    }

                    if (typedArray.hasValue(R.styleable.IndexFastScrollRecyclerView_setIndexBarHighlightTextColorRes)) {
                        mIndexbarHighLateTextColor = typedArray.getColor(R.styleable.IndexFastScrollRecyclerView_setIndexBarHighlightTextColor, mIndexbarHighLateTextColor)
                    }

                    mPreviewTextSize = typedArray.getInt(R.styleable.IndexFastScrollRecyclerView_setPreviewTextSize, mPreviewTextSize)
                    mPreviewTransparentValue = typedArray.getFloat(R.styleable.IndexFastScrollRecyclerView_setPreviewTransparentValue, mPreviewTransparentValue)

                    if (typedArray.hasValue(R.styleable.IndexFastScrollRecyclerView_setPreviewColor)) {
                        mPreviewBackgroudColor = Color.parseColor(typedArray.getString(R.styleable.IndexFastScrollRecyclerView_setPreviewColor))
                    }

                    if (typedArray.hasValue(R.styleable.IndexFastScrollRecyclerView_setPreviewTextColor)) {
                        mPreviewTextColor = Color.parseColor(typedArray.getString(R.styleable.IndexFastScrollRecyclerView_setPreviewTextColor))
                    }

                } finally {
                    typedArray.recycle()
                }
            }
        }
    }

    override fun draw(canvas: Canvas) {
        super.draw(canvas)

        // Overlay index bar
        if (mScroller != null)
            mScroller!!.draw(canvas)
    }

    override fun onTouchEvent(ev: MotionEvent): Boolean {
        if (mEnabled) {
            // Intercept ListView's touch event
            if (mScroller != null && mScroller!!.onTouchEvent(ev))
                return true

            if (mGestureDetector == null) {
                mGestureDetector = GestureDetector(context, object : GestureDetector.SimpleOnGestureListener() {

                    override fun onFling(e1: MotionEvent, e2: MotionEvent,
                                         velocityX: Float, velocityY: Float): Boolean {
                        return super.onFling(e1, e2, velocityX, velocityY)
                    }

                })
            }
            mGestureDetector!!.onTouchEvent(ev)
        }

        return super.onTouchEvent(ev)
    }

    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        return if (mEnabled && mScroller != null && mScroller!!.contains(ev.x, ev.y)) true else super.onInterceptTouchEvent(ev)

    }

    override fun setAdapter(adapter: RecyclerView.Adapter<*>?) {
        super.setAdapter(adapter)
        if (mScroller != null)
            mScroller!!.setAdapter(adapter)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        if (mScroller != null)
            mScroller!!.onSizeChanged(w, h, oldw, oldh)
    }

    /**
     * @param value int to set the text size of the index bar
     */
    fun setIndexTextSize(value: Int) {
        mScroller!!.setIndexTextSize(value)
    }

    /**
     * @param value float to set the width of the index bar
     */
    fun setIndexbarWidth(value: Float) {
        mScroller!!.setIndexbarWidth(value)
    }

    /**
     * @param value float to set the margin of the index bar
     */
    fun setIndexbarMargin(value: Float) {
        mScroller!!.setIndexbarMargin(value)
    }

    /**
     * @param value int to set the preview padding
     */
    fun setPreviewPadding(value: Int) {
        mScroller!!.setPreviewPadding(value)
    }

    /**
     * @param value int to set the corner radius of the index bar
     */
    fun setIndexBarCornerRadius(value: Int) {
        mScroller!!.setIndexBarCornerRadius(value)
    }

    /**
     * @param value float to set the transparency value of the index bar
     */
    fun setIndexBarTransparentValue(value: Float) {
        mScroller!!.setIndexBarTransparentValue(value)
    }

    /**
     * @param typeface Typeface to set the typeface of the preview & the index bar
     */
    fun setTypeface(typeface: Typeface) {
        mScroller!!.setTypeface(typeface)
    }

    /**
     * @param shown boolean to show or hide the index bar
     */
    fun setIndexBarVisibility(shown: Boolean) {
        mScroller!!.setIndexBarVisibility(shown)
        mEnabled = shown
    }

    /**
     * @param shown boolean to show or hide the preview
     */
    fun setPreviewVisibility(shown: Boolean) {
        mScroller!!.setPreviewVisibility(shown)
    }

    /**
     * @param value int to set the text size of the preview box
     */
    fun setPreviewTextSize(value: Int) {
        mScroller!!.setPreviewTextSize(value)
    }

    /**
     * @param color The color for the preview box
     */
    fun setPreviewColor(@ColorRes color: Int) {
        val colorValue = context.resources.getColor(color)
        mScroller!!.setPreviewColor(colorValue)
    }

    /**
     * @param color The color for the preview box
     */
    fun setPreviewColor(color: String) {
        mScroller!!.setPreviewColor(Color.parseColor(color))
    }

    /**
     * @param color The text color for the preview box
     */
    fun setPreviewTextColor(@ColorRes color: Int) {
        val colorValue = context.resources.getColor(color)
        mScroller!!.setPreviewTextColor(colorValue)
    }

    /**
     * @param value float to set the transparency value of the preview box
     */
    fun setPreviewTransparentValue(value: Float) {
        mScroller!!.setPreviewTransparentValue(value)
    }

    /**
     * @param color The text color for the preview box
     */
    fun setPreviewTextColor(color: String) {
        mScroller!!.setPreviewTextColor(Color.parseColor(color))
    }

    /**
     * @param color The color for the index bar
     */
    fun setIndexBarColor(color: String) {
        mScroller!!.setIndexBarColor(Color.parseColor(color))
    }

    /**
     * @param color The color for the index bar
     */
    fun setIndexBarColor(@ColorRes color: Int) {
        val colorValue = context.resources.getColor(color)
        mScroller!!.setIndexBarColor(colorValue)
    }


    /**
     * @param color The text color for the index bar
     */
    fun setIndexBarTextColor(color: String) {
        mScroller!!.setIndexBarTextColor(Color.parseColor(color))
    }

    /**
     * @param color The text color for the index bar
     */
    fun setIndexBarTextColor(@ColorRes color: Int) {
        val colorValue = context.resources.getColor(color)
        mScroller!!.setIndexBarTextColor(colorValue)
    }

    /**
     * @param color The text color for the index bar
     */
    fun setIndexbarHighLateTextColor(color: String) {
        mScroller!!.setIndexBarHighLateTextColor(Color.parseColor(color))
    }

    /**
     * @param color The text color for the index bar
     */
    fun setIndexbarHighLateTextColor(@ColorRes color: Int) {
        val colorValue = context.resources.getColor(color)
        mScroller!!.setIndexBarHighLateTextColor(colorValue)
    }

    /**
     * @param shown boolean to show or hide the index bar
     */
    fun setIndexBarHighLateTextVisibility(shown: Boolean) {
        mScroller!!.setIndexBarHighLateTextVisibility(shown)
    }

    fun updateSections() {
        mScroller!!.updateSections()
    }
}