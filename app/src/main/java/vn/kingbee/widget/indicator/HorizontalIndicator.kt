package vn.kingbee.widget.indicator

import android.content.Context
import android.util.AttributeSet
import android.widget.LinearLayout
import android.widget.TextView
import vn.kingbee.widget.R
import android.graphics.Typeface
import android.view.ViewGroup
import android.view.LayoutInflater
import androidx.core.content.ContextCompat
import timber.log.Timber

class HorizontalIndicator : LinearLayout {
    private var mItemWidth = 100
    private var mItemHeight = 5
    private var mProgressDrawable: Int = 0
    private var mAnimationDuration: Int = 0
    private var mTitleMarginBottom: Int = 0
    private var mTitleTextColor: Int = 0
    private var mTitleContentList: Array<CharSequence>? = null
    private var mTitleTextSize: Int = 0
    private var mTitleFontStyleNormal: String? = null
    private var mTitleFontStyleBold: String? = null
    private var mCurrentItem: Int = 0
    private var mLastPosition: Int = 0
    private var mProgressBar: AnimateHorizontalProgressBar? = null

    constructor(context: Context) : super(context) {
        this.mProgressDrawable = R.drawable.bg_step_indicator_default
        this.mAnimationDuration = 1000
        this.mTitleMarginBottom = 10
        this.mTitleTextColor = -1
        this.mTitleTextSize = 14
        this.mCurrentItem = 0
        this.mLastPosition = 0
        this.init(context, null)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        this.mProgressDrawable = R.drawable.bg_step_indicator_default
        this.mAnimationDuration = 1000
        this.mTitleMarginBottom = 10
        this.mTitleTextColor = -1
        this.mTitleTextSize = 14
        this.mCurrentItem = 0
        this.mLastPosition = 0
        this.init(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyle: Int) : super(context, attrs, defStyle) {
        this.mProgressDrawable = R.drawable.bg_step_indicator_default
        this.mAnimationDuration = 1000
        this.mTitleMarginBottom = 10
        this.mTitleTextColor = -1
        this.mTitleTextSize = 14
        this.mCurrentItem = 0
        this.mLastPosition = 0
        this.init(context, attrs)
    }

    private fun init(context: Context, attrs: AttributeSet?) {
        this.orientation = VERTICAL
        this.gravity = 1
        if (attrs != null) {
            val typedArray = context.theme.obtainStyledAttributes(attrs, R.styleable.HorizontalIndicator, 0, 0)

            try {
                this.mItemWidth = typedArray.getDimensionPixelSize(R.styleable.HorizontalIndicator_hi_itemWidth, this.mItemWidth)
                this.mItemHeight = typedArray.getDimensionPixelSize(R.styleable.HorizontalIndicator_hi_itemHeight, this.mItemHeight)
                this.mTitleMarginBottom = typedArray.getDimensionPixelSize(R.styleable.HorizontalIndicator_hi_titleMarginBottom, this.mTitleMarginBottom)
                this.mTitleTextColor = typedArray.getColor(R.styleable.HorizontalIndicator_hi_titleTextColor, ContextCompat.getColor(context, R.color.c_black))
                this.mTitleFontStyleNormal = typedArray.getString(R.styleable.HorizontalIndicator_hi_titleFontStyleNormal)
                this.mTitleFontStyleBold = typedArray.getString(R.styleable.HorizontalIndicator_hi_titleFontStyleBold)
                this.mTitleTextSize = typedArray.getDimensionPixelSize(R.styleable.HorizontalIndicator_hi_titleTextSize, this.mTitleTextSize)
                this.mTitleContentList = typedArray.getTextArray(R.styleable.HorizontalIndicator_hi_titleContentList)
                this.mProgressDrawable = typedArray.getResourceId(R.styleable.HorizontalIndicator_hi_progressDrawable, this.mProgressDrawable)
                this.mAnimationDuration = typedArray.getInteger(R.styleable.HorizontalIndicator_hi_progressAnimateDuration, this.mAnimationDuration)
            } catch (ex: Exception) {
                Timber.d(HorizontalIndicator::class.java.name, ex.message)
            } finally {
                typedArray.recycle()
            }
        }

        this.addStepIndicators()
        this.addHorizontalProgressBar()
    }

    fun setCurrentItem(currentItemPosition: Int) {
        if (this.mCurrentItem != currentItemPosition) {
            this.mCurrentItem = currentItemPosition
            this.onCurrentItemChanged(currentItemPosition)
        }
    }

    fun setSuccessAllItems() {
        val lastStep = (this.getChildAt(0) as LinearLayout).getChildAt((this.getChildAt(0) as LinearLayout).childCount - 1) as TextView
        this.setTypeFaceTextNormal(lastStep)
        this.mProgressBar?.setProgressWithAnim(this.mItemWidth * this.getNumberCount())
    }

    private fun onCurrentItemChanged(position: Int) {
        if (this.getNumberCount() > 0) {
            var from = 0
            var to = 0
            if (this.mLastPosition < position) {
                from = 0
                to = position
            } else if (this.mLastPosition > position) {
                from = position + 1
                to = this.getNumberCount()
            }

            if (from < to) {
                for (i in from until to) {
                    val step = (this.getChildAt(0) as LinearLayout).getChildAt(i) as TextView
                    this.setTypeFaceTextNormal(step)
                }
            }

            val currentStep = (this.getChildAt(0) as LinearLayout).getChildAt(position) as TextView
            this.setTypeFaceTextSelected(currentStep)
            this.mProgressBar?.setProgressWithAnim(this.mItemWidth * position)
            this.mProgressBar?.setSecondaryProgressWithAnim(this.mItemWidth * (position + 1))
            this.mLastPosition = position
        }
    }

    private fun addHorizontalProgressBar() {
        this.mProgressBar = LayoutInflater.from(this.context).inflate(R.layout.progress_bar_view, null as ViewGroup?) as AnimateHorizontalProgressBar
        this.mProgressBar?.progressDrawable = ContextCompat.getDrawable(context, this.mProgressDrawable)
        this.mProgressBar?.setDuration(this.mAnimationDuration)
        this.mProgressBar?.layoutParams = LayoutParams(-1, this.mItemHeight)
        this.mProgressBar?.max = this.mItemWidth * this.getNumberCount()
        this.mProgressBar?.progress = 0
        this.mProgressBar?.secondaryProgress = this.mItemWidth
        this.addView(this.mProgressBar)
    }

    private fun addStepIndicators() {
        if (this.childCount > 0) {
            this.removeViewAt(0)
        }

        val count = this.getNumberCount()
        if (count > 0) {
            val stepContainer = LinearLayout(this.context)
            stepContainer.layoutParams = LayoutParams(-2, -2)
            stepContainer.gravity = 48
            stepContainer.orientation = LinearLayout.HORIZONTAL
            stepContainer.setPadding(0, 0, 0, this.mTitleMarginBottom)
            this.addView(stepContainer, 0)
            val currentItem = this.getCurrentItem()

            for (i in 0 until count) {
                if (currentItem == i) {
                    this.addIndicator(stepContainer, true, i)
                } else {
                    this.addIndicator(stepContainer, false, i)
                }
            }
        }
    }

    private fun addIndicator(stepContainer: LinearLayout, currentPosition: Boolean, pos: Int) {
        val tv = TextView(this.context)
        tv.layoutParams = LayoutParams(this.mItemWidth, -1)
        tv.gravity = 81
        tv.text = this.mTitleContentList!![pos]
        tv.textSize = this.mTitleTextSize.toFloat()
        tv.setTextColor(this.mTitleTextColor)
        if (currentPosition) {
            this.setTypeFaceTextSelected(tv)
        } else {
            this.setTypeFaceTextNormal(tv)
        }

        stepContainer.addView(tv)
    }

    private fun setTypeFaceTextSelected(tv: TextView) {
        val typeface = TypeFacesUtils.get(this.context, this.mTitleFontStyleBold!!)
        if (typeface != null) {
            tv.typeface = typeface
        } else {
            tv.setTypeface(null as Typeface?, Typeface.BOLD)
        }
    }

    private fun setTypeFaceTextNormal(step: TextView) {
        val typeface = TypeFacesUtils.get(context, this.mTitleFontStyleNormal!!)
        if (typeface != null) {
            step.typeface = typeface
        } else {
            step.setTypeface(null as Typeface?, Typeface.NORMAL)
        }
    }

    fun setContentList(contentList: Array<CharSequence>, currentPosition: Int) {
        if (!this.mTitleContentList?.equals(contentList)!!) {
            this.mTitleContentList = contentList
            this.mLastPosition = 0
            this.mCurrentItem = currentPosition
            this.mProgressBar?.progress = this.mItemWidth * currentPosition
            this.mProgressBar?.secondaryProgress = this.mItemWidth * (currentPosition + 1)
            this.mProgressBar?.max = this.mItemWidth * this.getNumberCount()
            this.addStepIndicators()
        }
    }

    fun getNumberCount(): Int =
        if (this.mTitleContentList != null) this.mTitleContentList!!.size else 0

    fun getCurrentItem(): Int = this.mCurrentItem
}