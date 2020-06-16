package vn.kingbee.widget.calendar.ggdatepicker

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Typeface
import androidx.core.content.ContextCompat
import android.text.format.Time
import android.view.MotionEvent
import android.view.View
import vn.kingbee.widget.R
import vn.kingbee.widget.calendar.Utils
import java.security.InvalidParameterException
import java.text.DateFormatSymbols
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap

class SimpleMonthView : View {
    companion object {
        const val VIEW_PARAMS_HEIGHT = "height"
        const val VIEW_PARAMS_MONTH = "month"
        const val VIEW_PARAMS_YEAR = "year"
        const val VIEW_PARAMS_SELECTED_DAY = "selected_day"
        const val VIEW_PARAMS_WEEK_START = "week_start"
        const val VIEW_PARAMS_NUM_DAYS = "num_days"
        const val VIEW_PARAMS_FOCUS_MONTH = "focus_month"
        const val VIEW_PARAMS_SHOW_WK_NUM = "show_wk_num"
        protected const val DEFAULT_NUM_ROWS = 6
        private const val SELECTED_CIRCLE_ALPHA = 60
        protected var DEFAULT_HEIGHT = 32

        protected var DAY_SELECTED_CIRCLE_SIZE: Int = 0
        protected var DAY_SEPARATOR_WIDTH = 1
        protected var MINI_DAY_NUMBER_TEXT_SIZE: Int = 0
        protected var MIN_HEIGHT = 10
        protected var MONTH_DAY_LABEL_TEXT_SIZE: Int = 0
        protected var MONTH_HEADER_SIZE: Int = 0
        protected var MONTH_LABEL_TEXT_SIZE: Int = 0
        protected var mScale = 0.0f
    }

    private var mNumRows = DEFAULT_NUM_ROWS
    protected var mRowHeight = DEFAULT_HEIGHT
    private val mStringBuilder: StringBuilder
    private val mFormatter: Formatter
    private val mCalendar: Calendar
    private val mDayLabelCalendar: Calendar
    protected var mPadding = 0
    protected var mMonthDayLabelPaint: Paint? = null
    protected var mMonthNumPaint: Paint? = null
    protected var mMonthTitleBGPaint: Paint? = null
    protected var mMonthTitlePaint: Paint? = null
    protected var mSelectedCirclePaint: Paint? = null
    protected var mTodayCirclePaint: Paint? = null
    protected var mDayTextColor: Int = 0
    protected var mMonthTitleBGColor: Int = 0
    protected var mMonthTitleColor: Int = 0
    protected var mTodayNumberColor: Int = 0
    protected var mFirstJulianDay = -1
    protected var mFirstMonth = -1
    protected var mLastMonth = -1
    protected var mHasToday = false
    protected var mSelectedDay = -1
    protected var mToday = -1
    protected var mWeekStart = 1
    protected var mNumDays = 7
    protected var mNumCells = mNumDays
    protected var mSelectedLeft = -1
    protected var mSelectedRight = -1
    protected var mMonth: Int = 0
    protected var mWidth: Int = 0
    protected var mYear: Int = 0
    private var mDayOfWeekTypeface: String
    private var mMonthTitleTypeface: String
    private var mDayOfWeekStart = 0
    private val mDateFormatSymbols = DateFormatSymbols()
    private val mMonthYearFormatter = SimpleDateFormat("MMMM yyyy")

    private var mOnDayClickListener: OnDayClickListener? = null

    constructor(context: Context) : super(context) {

        val resources = context.resources
        mDayLabelCalendar = Calendar.getInstance()
        mCalendar = Calendar.getInstance()

        mDayOfWeekTypeface = resources.getString(R.string.day_of_week_label_typeface)
        mMonthTitleTypeface = resources.getString(R.string.sans_serif)
        mDayTextColor = ContextCompat.getColor(context, R.color.date_picker_text_normal)
        mTodayNumberColor = ContextCompat.getColor(context, R.color.blue)
        mMonthTitleColor = ContextCompat.getColor(context, R.color.white)
        mMonthTitleBGColor = ContextCompat.getColor(context, R.color.circle_background)

        mStringBuilder = StringBuilder(50)
        mFormatter = Formatter(mStringBuilder, Locale.getDefault())

        MINI_DAY_NUMBER_TEXT_SIZE = resources.getDimensionPixelSize(R.dimen.day_number_size)
        MONTH_LABEL_TEXT_SIZE = resources.getDimensionPixelSize(R.dimen.month_label_size)
        MONTH_DAY_LABEL_TEXT_SIZE =
            resources.getDimensionPixelSize(R.dimen.month_day_label_text_size)
        MONTH_HEADER_SIZE = resources.getDimensionPixelOffset(R.dimen.month_list_item_header_height)
        DAY_SELECTED_CIRCLE_SIZE =
            resources.getDimensionPixelSize(R.dimen.day_number_select_circle_radius)

        mRowHeight =
            (resources.getDimensionPixelOffset(R.dimen.date_picker_view_animator_height) - MONTH_HEADER_SIZE) / 6

        initView()
    }

    private fun calculateNumRows(): Int {
        val offset = findDayOffset()
        val dividend = (offset + mNumCells) / mNumDays
        val remainder = (offset + mNumCells) % mNumDays
        return dividend + if (remainder > 0) 1 else 0
    }

    private fun drawMonthDayLabels(canvas: Canvas) {
        val y = MONTH_HEADER_SIZE - MONTH_DAY_LABEL_TEXT_SIZE / 2
        val dayWidthHalf = (mWidth - mPadding * 2) / (mNumDays * 2)

        for (i in 0 until mNumDays) {
            val calendarDay = (i + mWeekStart) % mNumDays
            val x = (2 * i + 1) * dayWidthHalf + mPadding
            mDayLabelCalendar.set(Calendar.DAY_OF_WEEK, calendarDay)
            canvas.drawText(
                mDateFormatSymbols.shortWeekdays[mDayLabelCalendar.get(Calendar.DAY_OF_WEEK)].toUpperCase(
                    Locale.getDefault()
                ), x.toFloat(), y.toFloat(), mMonthDayLabelPaint!!
            )
        }
    }

    private fun drawMonthTitle(canvas: Canvas) {
        val x = (mWidth + 2 * mPadding) / 2
        val y = (MONTH_HEADER_SIZE - MONTH_DAY_LABEL_TEXT_SIZE) / 2 + MONTH_LABEL_TEXT_SIZE / 3
        canvas.drawText(getMonthAndYearString(), x.toFloat(), y.toFloat(), mMonthTitlePaint!!)
    }

    private fun findDayOffset(): Int {
        return (if (mDayOfWeekStart < mWeekStart) mDayOfWeekStart + mNumDays else mDayOfWeekStart) - mWeekStart
    }

    private fun getMonthAndYearString(): String {
        val millis = mCalendar.timeInMillis
        return mMonthYearFormatter.format(Date(millis))
    }

    private fun onDayClick(calendarDay: SimpleMonthAdapter.CalendarDay) {
        if (mOnDayClickListener != null) {
            mOnDayClickListener!!.onDayClick(this, calendarDay)
        }
    }

    private fun sameDay(monthDay: Int, time: Time): Boolean {
        return mYear == time.year && mMonth == time.month && monthDay == time.monthDay
    }

    protected fun drawMonthNums(canvas: Canvas) {
        var y =
            (mRowHeight + MINI_DAY_NUMBER_TEXT_SIZE) / 2 - DAY_SEPARATOR_WIDTH + MONTH_HEADER_SIZE
        val paddingDay = (mWidth - 2 * mPadding) / (2 * mNumDays)
        var dayOffset = findDayOffset()
        var day = 1

        while (day <= mNumCells) {
            val x = paddingDay * (1 + dayOffset * 2) + mPadding
            if (mSelectedDay == day) {
                canvas.drawCircle(
                    x.toFloat(),
                    (y - MINI_DAY_NUMBER_TEXT_SIZE / 3).toFloat(),
                    DAY_SELECTED_CIRCLE_SIZE.toFloat(),
                    mSelectedCirclePaint!!
                )
            } else if (mHasToday && mToday == day) {
                canvas.drawCircle(
                    x.toFloat(),
                    (y - MINI_DAY_NUMBER_TEXT_SIZE / 3).toFloat(),
                    DAY_SELECTED_CIRCLE_SIZE.toFloat(),
                    mTodayCirclePaint!!
                )
            }
            if (mSelectedDay == day) {
                mMonthNumPaint!!.color = resources.getColor(android.R.color.white)
            } else if (mHasToday && mToday == day) {
                mMonthNumPaint!!.color = resources.getColor(android.R.color.white)
            } else {
                mMonthNumPaint!!.color = mDayTextColor
            }

            canvas.drawText(String.format("%d", day), x.toFloat(), y.toFloat(), mMonthNumPaint)

            dayOffset++
            if (dayOffset == mNumDays) {
                dayOffset = 0
                y += mRowHeight
            }
            day++
        }
    }

    fun getDayFromLocation(x: Float, y: Float): SimpleMonthAdapter.CalendarDay? {
        val padding = mPadding
        if (x < padding || x > mWidth - mPadding) {
            return null
        }

        val yDay = (y - MONTH_HEADER_SIZE).toInt() / mRowHeight
        val day =
            1 + (((x - padding) * mNumDays / (mWidth - padding - mPadding)).toInt() - findDayOffset()) + yDay * mNumDays

        return SimpleMonthAdapter.CalendarDay(mYear, mMonth, day)
    }

    protected fun initView() {
        mMonthTitlePaint = Paint()
        mMonthTitlePaint?.isFakeBoldText = true
        mMonthTitlePaint?.isAntiAlias = true
        mMonthTitlePaint?.textSize = MONTH_LABEL_TEXT_SIZE.toFloat()
        mMonthTitlePaint?.typeface = Typeface.create(mMonthTitleTypeface, Typeface.BOLD)
        mMonthTitlePaint?.color = mDayTextColor
        mMonthTitlePaint?.textAlign = Paint.Align.CENTER
        mMonthTitlePaint?.style = Paint.Style.FILL

        mMonthTitleBGPaint = Paint()
        mMonthTitleBGPaint?.isFakeBoldText = true
        mMonthTitleBGPaint?.isAntiAlias = true
        mMonthTitleBGPaint?.color = mMonthTitleBGColor
        mMonthTitleBGPaint?.textAlign = Paint.Align.CENTER
        mMonthTitleBGPaint?.style = Paint.Style.FILL

        mSelectedCirclePaint = Paint()
        mSelectedCirclePaint?.isFakeBoldText = true
        mSelectedCirclePaint?.isAntiAlias = true
        mSelectedCirclePaint?.color = mTodayNumberColor
        mSelectedCirclePaint?.textAlign = Paint.Align.CENTER
        mSelectedCirclePaint?.style = Paint.Style.FILL

        mTodayCirclePaint = Paint()
        mTodayCirclePaint?.isFakeBoldText = true
        mTodayCirclePaint?.isAntiAlias = true
        mTodayCirclePaint?.color = mDayTextColor
        mTodayCirclePaint?.textAlign = Paint.Align.CENTER
        mTodayCirclePaint?.style = Paint.Style.FILL

        mMonthDayLabelPaint = Paint()
        mMonthDayLabelPaint?.isAntiAlias = true
        mMonthDayLabelPaint?.textSize = MONTH_DAY_LABEL_TEXT_SIZE.toFloat()
        mMonthDayLabelPaint?.color = mDayTextColor
        mMonthDayLabelPaint?.typeface = Typeface.create(mDayOfWeekTypeface, Typeface.NORMAL)
        mMonthDayLabelPaint?.style = Paint.Style.FILL
        mMonthDayLabelPaint?.textAlign = Paint.Align.CENTER
        mMonthDayLabelPaint?.isFakeBoldText = true

        mMonthNumPaint = Paint()
        mMonthNumPaint?.isAntiAlias = true
        mMonthNumPaint?.textSize = MINI_DAY_NUMBER_TEXT_SIZE.toFloat()
        mMonthNumPaint?.style = Paint.Style.FILL
        mMonthNumPaint?.textAlign = Paint.Align.CENTER
        mMonthNumPaint?.isFakeBoldText = false
    }

    override fun onDraw(canvas: Canvas) {
        drawMonthTitle(canvas)
        drawMonthDayLabels(canvas)
        drawMonthNums(canvas)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        setMeasuredDimension(
            MeasureSpec.getSize(widthMeasureSpec), mRowHeight * mNumRows + MONTH_HEADER_SIZE
        )
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        mWidth = w
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_UP) {
            val calendarDay = getDayFromLocation(event.x, event.y)
            if (calendarDay != null) {
                onDayClick(calendarDay)
            }
        }
        return true
    }

    fun reuse() {
        mNumRows = DEFAULT_NUM_ROWS
        requestLayout()
    }

    fun setMonthParams(params: HashMap<String, Int>) {
        if (!params.containsKey(VIEW_PARAMS_MONTH) && !params.containsKey(VIEW_PARAMS_YEAR)) {
            throw InvalidParameterException("You must specify month and year for this view")
        }
        tag = params

        if (params.containsKey(VIEW_PARAMS_HEIGHT)) {
            mRowHeight = params[VIEW_PARAMS_HEIGHT]!!
            if (mRowHeight < MIN_HEIGHT) {
                mRowHeight = MIN_HEIGHT
            }
        }
        if (params.containsKey(VIEW_PARAMS_SELECTED_DAY)) {
            mSelectedDay = params[VIEW_PARAMS_SELECTED_DAY]!!
        }

        mMonth = params[VIEW_PARAMS_MONTH]!!
        mYear = params[VIEW_PARAMS_YEAR]!!

        val today = Time(Time.getCurrentTimezone())
        today.setToNow()
        mHasToday = false
        mToday = -1

        mCalendar.set(Calendar.MONTH, mMonth)
        mCalendar.set(Calendar.YEAR, mYear)
        mCalendar.set(Calendar.DAY_OF_MONTH, 1)
        mDayOfWeekStart = mCalendar.get(Calendar.DAY_OF_WEEK)

        if (params.containsKey(VIEW_PARAMS_WEEK_START)) {
            mWeekStart = params[VIEW_PARAMS_WEEK_START]!!
        } else {
            mWeekStart = mCalendar.firstDayOfWeek
        }

        mNumCells = Utils.getDaysInMonth(mMonth, mYear)
        for (i in 0 until mNumCells) {
            val day = i + 1
            if (sameDay(day, today)) {
                mHasToday = true
                mToday = day
            }
        }

        mNumRows = calculateNumRows()
    }

    fun setOnDayClickListener(onDayClickListener: OnDayClickListener) {
        mOnDayClickListener = onDayClickListener
    }

    interface OnDayClickListener {
        fun onDayClick(simpleMonthView: SimpleMonthView,
                       calendarDay: SimpleMonthAdapter.CalendarDay)
    }
}