package vn.kingbee.widget.calendar.ggdatepicker

import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.content.Context
import android.os.Build
import android.os.Handler
import android.view.View
import android.view.ViewConfiguration
import android.widget.AbsListView
import android.widget.ListView

class DayPickerView(context: Context, datePickerController: DatePickerController) :
    ListView(context), AbsListView.OnScrollListener, DatePickerDialog.OnDateChangedListener {

    companion object {
        protected const val GOTO_SCROLL_DURATION = 250
        protected const val SCROLL_CHANGE_DELAY = 40
        var LIST_TOP_OFFSET = -1
    }

    private var mController: DatePickerController = datePickerController
    protected lateinit var mContext: Context
    protected var mHandler = Handler()
    protected var mAdapter: SimpleMonthAdapter? = null
    protected var mCurrentMonthDisplayed: Int = 0
    protected var mCurrentScrollState = 0
    protected var mPreviousScrollPosition: Long = 0
    protected var mPreviousScrollState = 0
    protected var mScrollStateChangedRunnable = ScrollStateRunnable()
    protected var mSelectedDay = SimpleMonthAdapter.CalendarDay()
    protected var mTempDay = SimpleMonthAdapter.CalendarDay()
    protected var mNumWeeks = 6
    protected var mShowWeekNumber = false
    protected var mDaysPerWeek = 7
    protected var mFriction = 1.0f
    private var mPerformingScroll: Boolean = false

    init {
        mController.registerOnDateChangedListener(this)
        layoutParams = LayoutParams(
            LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT
        )
        setDrawSelectorOnTop(false)
        init(context)
        onDateChanged()
    }

    fun getMostVisiblePosition(): Int {
        val firstPosition = firstVisiblePosition
        val height = height

        var maxDisplayedHeight = 0
        var mostVisibleIndex = 0
        var i = 0
        var bottom = 0
        while (bottom < height) {
            val child = getChildAt(i) ?: break
            bottom = child.bottom
            val displayedHeight = Math.min(bottom, height) - Math.max(0, child.top)
            if (displayedHeight > maxDisplayedHeight) {
                mostVisibleIndex = i
                maxDisplayedHeight = displayedHeight
            }
            i++
        }
        return firstPosition + mostVisibleIndex
    }

    @SuppressLint("ObsoleteSdkInt")
    fun goTo(day: SimpleMonthAdapter.CalendarDay,
             animate: Boolean,
             setSelected: Boolean,
             forceScroll: Boolean): Boolean {
        // Set the selected day
        if (setSelected) {
            mSelectedDay.set(day)
        }

        mTempDay.set(day)
        val position =
            (day.year - mController.getMinYear()) * SimpleMonthAdapter.MONTHS_IN_YEAR + day.month

        var child: View?
        var i = 0
        var top = 0
        // Find a child that's completely in the view
        do {
            child = getChildAt(i++)
            if (child == null) {
                break
            }
            top = child.top
        } while (top < 0)

        // Compute the first and last position visible
        val selectedPosition: Int
        if (child != null) {
            selectedPosition = getPositionForView(child)
        } else {
            selectedPosition = 0
        }

        if (setSelected) {
            mAdapter!!.setSelectedDay(mSelectedDay)
        }

        // Check if the selected day is now outside of our visible range
        // and if so scroll to the month that contains it
        if (position != selectedPosition || forceScroll) {
            setMonthDisplayed(mTempDay)
            mPreviousScrollState = AbsListView.OnScrollListener.SCROLL_STATE_FLING
            if (animate && Build.VERSION.SDK_INT >= 11) {
                smoothScrollToPositionFromTop(position, LIST_TOP_OFFSET, GOTO_SCROLL_DURATION)
                return true
            } else {
                postSetSelection(position)
            }
        } else if (setSelected) {
            setMonthDisplayed(mSelectedDay)
        }
        return false
    }

    fun init(paramContext: Context) {
        mContext = paramContext
        setUpListView()
        setUpAdapter()
        adapter = mAdapter
    }

    override fun layoutChildren() {
        super.layoutChildren()
        if (mPerformingScroll) {
            mPerformingScroll = false
        }
    }

    fun onChange() {
        setUpAdapter()
        adapter = mAdapter
    }

    override fun onDateChanged() {
        goTo(mController.getSelectedDay(), false, true, true)
    }

    override fun onScroll(view: AbsListView,
                          firstVisibleItem: Int,
                          visibleItemCount: Int,
                          totalItemCount: Int) {
        val child = view.getChildAt(0) as SimpleMonthView

        // Figure out where we are
        val currScroll = view.firstVisiblePosition * child.height - child.bottom
        mPreviousScrollPosition = currScroll.toLong()
        mPreviousScrollState = mCurrentScrollState
    }

    override fun onScrollStateChanged(absListView: AbsListView, scroll: Int) {
        mScrollStateChangedRunnable.doScrollStateChange(absListView, scroll)
    }

    fun postSetSelection(position: Int) {
        clearFocus()
        post { this@DayPickerView.setSelection(position) }
        onScrollStateChanged(this, 0)
    }

    protected fun setMonthDisplayed(calendarDay: SimpleMonthAdapter.CalendarDay) {
        this.mCurrentMonthDisplayed = calendarDay.month
        invalidateViews()
    }

    protected fun setUpAdapter() {
        if (mAdapter == null) {
            mAdapter = SimpleMonthAdapter(context, mController)
        }
        mAdapter!!.setSelectedDay(this.mSelectedDay)
        mAdapter!!.notifyDataSetChanged()
    }

    protected fun setUpListView() {
        cacheColorHint = 0
        divider = null
        itemsCanFocus = true
        isFastScrollEnabled = false
        isVerticalScrollBarEnabled = false
        setOnScrollListener(this)
        setFadingEdgeLength(0)
        setFrictionIfSupported(ViewConfiguration.getScrollFriction() * mFriction)
    }

    @SuppressLint("ObsoleteSdkInt")
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    internal fun setFrictionIfSupported(friction: Float) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            setFriction(friction)
        }
    }

    protected inner class ScrollStateRunnable : Runnable {
        private var mNewState: Int = 0

        /**
         * Sets up the runnable with a short delay in case the scroll state
         * immediately changes again.
         *
         * @param view        The list view that changed state
         * @param scrollState The new state it changed to
         */
        fun doScrollStateChange(view: AbsListView, scrollState: Int) {
            mHandler.removeCallbacks(this)
            mNewState = scrollState
            mHandler.postDelayed(this, SCROLL_CHANGE_DELAY.toLong())
        }

        override fun run() {
            mCurrentScrollState = mNewState
            // Fix the position after a scroll or a fling ends
            if (mNewState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE && mPreviousScrollState != AbsListView.OnScrollListener.SCROLL_STATE_IDLE && mPreviousScrollState != AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                mPreviousScrollState = mNewState
                var i = 0
                var child: View? = getChildAt(i)
                while (child != null && child.bottom <= 0) {
                    child = getChildAt(++i)
                }
                if (child == null) {
                    // The view is no longer visible, just return
                    return
                }
                val firstPosition = firstVisiblePosition
                val lastPosition = lastVisiblePosition
                val scroll = firstPosition != 0 && lastPosition != count - 1
                val top = child.top
                val bottom = child.bottom
                val midpoint = height / 2
                if (scroll && top < LIST_TOP_OFFSET) {
                    if (bottom > midpoint) {
                        smoothScrollBy(top, GOTO_SCROLL_DURATION)
                    } else {
                        smoothScrollBy(bottom, GOTO_SCROLL_DURATION)
                    }
                }
            } else {
                mPreviousScrollState = mNewState
            }
        }
    }
}