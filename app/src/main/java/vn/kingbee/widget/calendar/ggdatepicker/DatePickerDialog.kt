package vn.kingbee.widget.calendar.ggdatepicker

import android.content.Context
import android.os.Bundle
import android.os.SystemClock
import android.os.Vibrator
import android.support.v4.app.DialogFragment
import android.text.format.DateUtils
import android.view.*
import android.view.animation.AlphaAnimation
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import vn.kingbee.widget.R
import vn.kingbee.widget.calendar.Utils
import java.text.DateFormatSymbols
import java.text.SimpleDateFormat
import java.util.*

class DatePickerDialog : DialogFragment(), View.OnClickListener, DatePickerController {

    private var mMaxYear = MAX_YEAR
    private var mMinYear = MIN_YEAR
    private var mCurrentView = UNINITIALIZED
    private val mCalendar = Calendar.getInstance()
    private val mDateFormatSymbols = DateFormatSymbols()
    private val mListeners = HashSet<OnDateChangedListener>()
    private var mCallBack: OnDateSetListener? = null
    private var mAnimator: AccessibleDateAnimator? = null
    private var mDelayAnimation = true
    private var mLastVibrate: Long = 0
    private var mWeekStart = mCalendar.firstDayOfWeek
    private var mDayPickerDescription: String? = null
    private var mYearPickerDescription: String? = null
    private var mSelectDay: String? = null
    private var mSelectYear: String? = null

    private var mDayOfWeekView: TextView? = null
    private var mDayPickerView: DayPickerView? = null
    private var mDoneButton: Button? = null
    private var mMonthAndDayView: LinearLayout? = null
    private var mSelectedDayTextView: TextView? = null
    private var mSelectedMonthTextView: TextView? = null
    private var mVibrator: Vibrator? = null
    private var mYearPickerView: YearPickerView? = null
    private var mYearView: TextView? = null

    private var mVibrate = true
    private var mCloseOnSingleTapDay: Boolean = false

    private fun adjustDayInMonthIfNeeded(month: Int, year: Int) {
        val day = mCalendar.get(Calendar.DAY_OF_MONTH)
        val daysInMonth = Utils.getDaysInMonth(month, year)
        if (day > daysInMonth) {
            mCalendar.set(Calendar.DAY_OF_MONTH, daysInMonth)
        }
    }

    fun setVibrate(vibrate: Boolean) {
        mVibrate = vibrate
    }

    private fun setCurrentView(currentView: Int) {
        setCurrentView(currentView, false)
    }

    private fun setCurrentView(currentView: Int, forceRefresh: Boolean) {
        val timeInMillis = mCalendar.timeInMillis
        when (currentView) {
            MONTH_AND_DAY_VIEW -> {
                val monthDayAnim = Utils.getPulseAnimator(mMonthAndDayView as View, 0.9f, 1.05f)
                if (mDelayAnimation) {
                    monthDayAnim.startDelay = ANIMATION_DELAY.toLong()
                    mDelayAnimation = false
                }
                mDayPickerView!!.onDateChanged()
                if (mCurrentView != currentView || forceRefresh) {
                    mMonthAndDayView!!.isSelected = true
                    mYearView!!.isSelected = false
                    mAnimator!!.displayedChild = MONTH_AND_DAY_VIEW
                    mCurrentView = currentView
                }
                monthDayAnim.start()
                val monthDayDesc =
                    DateUtils.formatDateTime(context, timeInMillis, DateUtils.FORMAT_SHOW_DATE)
                mAnimator!!.contentDescription = "$mDayPickerDescription: $monthDayDesc"
                Utils.tryAccessibilityAnnounce(mAnimator, mSelectDay)
            }
            YEAR_VIEW -> {
                val yearAnim = Utils.getPulseAnimator(mYearView as View, 0.85f, 1.1f)
                if (mDelayAnimation) {
                    yearAnim.startDelay = ANIMATION_DELAY.toLong()
                    mDelayAnimation = false
                }
                mYearPickerView!!.onDateChanged()
                if (mCurrentView != currentView || forceRefresh) {
                    mMonthAndDayView!!.isSelected = false
                    mYearView!!.isSelected = true
                    mAnimator!!.displayedChild = YEAR_VIEW
                    mCurrentView = currentView
                }
                yearAnim.start()
                val dayDesc = YEAR_FORMAT.format(timeInMillis)
                mAnimator!!.contentDescription = "$mYearPickerDescription: $dayDesc"
                Utils.tryAccessibilityAnnounce(mAnimator, mSelectYear)
            }
        }
    }

    private fun updateDisplay(announce: Boolean) {

        if (this.mDayOfWeekView != null) {
            this.mCalendar.firstDayOfWeek = mWeekStart
            this.mDayOfWeekView!!.text =
                mDateFormatSymbols.weekdays[this.mCalendar.get(Calendar.DAY_OF_WEEK)].toUpperCase(
                    Locale.getDefault()
                )
        }

        this.mSelectedMonthTextView!!.text =
            mDateFormatSymbols.months[this.mCalendar.get(Calendar.MONTH)].toUpperCase(
                Locale.getDefault()
            )

        mSelectedDayTextView!!.text = DAY_FORMAT.format(mCalendar.time)
        mYearView!!.text = YEAR_FORMAT.format(mCalendar.time)

        // Accessibility.
        val millis = mCalendar.timeInMillis
        mAnimator!!.setDateMillis(millis)
        var flags = DateUtils.FORMAT_SHOW_DATE or DateUtils.FORMAT_NO_YEAR
        val monthAndDayText = DateUtils.formatDateTime(activity, millis, flags)
        mMonthAndDayView!!.contentDescription = monthAndDayText

        if (announce) {
            flags = DateUtils.FORMAT_SHOW_DATE or DateUtils.FORMAT_SHOW_YEAR
            val fullDateText = DateUtils.formatDateTime(activity, millis, flags)
            Utils.tryAccessibilityAnnounce(mAnimator, fullDateText)
        }
    }

    private fun updatePickers() {
        val iterator = mListeners.iterator()
        while (iterator.hasNext()) {
            iterator.next().onDateChanged()
        }
    }

    override fun getFirstDayOfWeek(): Int {
        return mWeekStart
    }

    fun setFirstDayOfWeek(startOfWeek: Int) {
        if (startOfWeek < Calendar.SUNDAY || startOfWeek > Calendar.SATURDAY) {
            throw IllegalArgumentException("Value must be between Calendar.SUNDAY and " + "Calendar.SATURDAY")
        }
        mWeekStart = startOfWeek
        if (mDayPickerView != null) {
            mDayPickerView!!.onChange()
        }
    }

    override fun getMaxYear(): Int {
        return mMaxYear
    }

    override fun getMinYear(): Int {
        return mMinYear
    }

    override fun getSelectedDay(): SimpleMonthAdapter.CalendarDay {
        return SimpleMonthAdapter.CalendarDay(mCalendar)
    }

    fun initialize(onDateSetListener: OnDateSetListener,
                   year: Int,
                   month: Int,
                   day: Int,
                   vibrate: Boolean) {
        if (year > MAX_YEAR) throw IllegalArgumentException("year end must < $MAX_YEAR")
        if (year < MIN_YEAR) throw IllegalArgumentException("year end must > $MIN_YEAR")
        mCallBack = onDateSetListener
        mCalendar.set(Calendar.YEAR, year)
        mCalendar.set(Calendar.MONTH, month)
        mCalendar.set(Calendar.DAY_OF_MONTH, day)
        mVibrate = vibrate
    }

    override fun onClick(view: View) {
        tryVibrate()
        if (view.id == R.id.date_picker_year) setCurrentView(YEAR_VIEW)
        else if (view.id == R.id.date_picker_month_and_day) setCurrentView(MONTH_AND_DAY_VIEW)
    }

    override fun onCreate(bundle: Bundle?) {
        super.onCreate(bundle)
        activity!!.window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)
        mVibrator = activity!!.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        if (bundle != null) {
            mCalendar.set(Calendar.YEAR, bundle.getInt(KEY_SELECTED_YEAR))
            mCalendar.set(Calendar.MONTH, bundle.getInt(KEY_SELECTED_MONTH))
            mCalendar.set(Calendar.DAY_OF_MONTH, bundle.getInt(KEY_SELECTED_DAY))
            mVibrate = bundle.getBoolean(KEY_VIBRATE)
        }
    }

    override fun onDetach() {
        super.onDetach()
        if (mCallBack != null) mCallBack!!.onDateDismissed(this)
    }

    override fun onCreateView(layoutInflater: LayoutInflater,
                              parent: ViewGroup?,
                              bundle: Bundle?): View? {
        dialog.window!!.requestFeature(Window.FEATURE_NO_TITLE)

        val view = layoutInflater.inflate(R.layout.date_picker_dialog, null)

        mDayOfWeekView = view.findViewById(R.id.date_picker_header)
        mMonthAndDayView = view.findViewById(R.id.date_picker_month_and_day)
        mMonthAndDayView!!.setOnClickListener(this)
        mSelectedMonthTextView = view.findViewById(R.id.date_picker_month)
        mSelectedDayTextView = view.findViewById(R.id.date_picker_day)
        mYearView = view.findViewById(R.id.date_picker_year)
        mYearView!!.setOnClickListener(this)

        var listPosition = -1
        var currentView = MONTH_AND_DAY_VIEW
        var listPositionOffset = 0
        if (bundle != null) {
            mWeekStart = bundle.getInt(KEY_WEEK_START)
            mMinYear = bundle.getInt(KEY_YEAR_START)
            mMaxYear = bundle.getInt(KEY_YEAR_END)
            currentView = bundle.getInt(KEY_CURRENT_VIEW)
            listPosition = bundle.getInt(KEY_LIST_POSITION)
            listPositionOffset = bundle.getInt(KEY_LIST_POSITION_OFFSET)
        }

        mDayPickerView = DayPickerView(context!!, this)
        mYearPickerView = YearPickerView(context!!, this)

        val resources = resources
        mDayPickerDescription = resources.getString(R.string.day_picker_description)
        mSelectDay = resources.getString(R.string.select_day)
        mYearPickerDescription = resources.getString(R.string.year_picker_description)
        mSelectYear = resources.getString(R.string.select_year)

        mAnimator = view.findViewById(R.id.animator)
        mAnimator!!.addView(mDayPickerView)
        mAnimator!!.addView(mYearPickerView)
        mAnimator!!.setDateMillis(mCalendar.timeInMillis)

        val inAlphaAnimation = AlphaAnimation(0.0f, 1.0f)
        inAlphaAnimation.duration = 300L
        mAnimator!!.inAnimation = inAlphaAnimation

        val outAlphaAnimation = AlphaAnimation(1.0f, 0.0f)
        outAlphaAnimation.duration = 300L
        mAnimator!!.outAnimation = outAlphaAnimation

        mDoneButton = view.findViewById(R.id.done)
        mDoneButton!!.setOnClickListener { onDoneButtonClick() }

        updateDisplay(false)
        setCurrentView(currentView, true)

        if (listPosition != -1) {
            if (currentView == MONTH_AND_DAY_VIEW) {
                mDayPickerView!!.postSetSelection(listPosition)
            }
            if (currentView == YEAR_VIEW) {
                mYearPickerView!!.postSetSelectionFromTop(listPosition, listPositionOffset)
            }
        }
        return view
    }

    private fun onDoneButtonClick() {
        tryVibrate()
        if (mCallBack != null) {
            mCallBack!!.onDateSet(
                this, mCalendar.get(Calendar.YEAR), mCalendar.get(Calendar.MONTH), mCalendar.get(
                    Calendar.DAY_OF_MONTH
                )
            )
        }
        dismiss()
    }

    override fun onDayOfMonthSelected(year: Int, month: Int, day: Int) {
        mCalendar.set(Calendar.YEAR, year)
        mCalendar.set(Calendar.MONTH, month)
        mCalendar.set(Calendar.DAY_OF_MONTH, day)
        updatePickers()
        updateDisplay(true)

        if (mCloseOnSingleTapDay) {
            onDoneButtonClick()
        }
    }

    override fun onSaveInstanceState(bundle: Bundle) {
        super.onSaveInstanceState(bundle)
        bundle.putInt(KEY_SELECTED_YEAR, mCalendar.get(Calendar.YEAR))
        bundle.putInt(KEY_SELECTED_MONTH, mCalendar.get(Calendar.MONTH))
        bundle.putInt(KEY_SELECTED_DAY, mCalendar.get(Calendar.DAY_OF_MONTH))
        bundle.putInt(KEY_WEEK_START, mWeekStart)
        bundle.putInt(KEY_YEAR_START, mMinYear)
        bundle.putInt(KEY_YEAR_END, mMaxYear)
        bundle.putInt(KEY_CURRENT_VIEW, mCurrentView)

        var listPosition = -1
        if (mCurrentView == 0) {
            listPosition = mDayPickerView!!.getMostVisiblePosition()
        }
        if (mCurrentView == 1) {
            listPosition = mYearPickerView!!.firstVisiblePosition
            bundle.putInt(KEY_LIST_POSITION_OFFSET, mYearPickerView!!.getFirstPositionOffset())
        }
        bundle.putInt(KEY_LIST_POSITION, listPosition)
        bundle.putBoolean(KEY_VIBRATE, mVibrate)
    }

    override fun onYearSelected(year: Int) {
        adjustDayInMonthIfNeeded(mCalendar.get(Calendar.MONTH), year)
        mCalendar.set(Calendar.YEAR, year)
        updatePickers()
        setCurrentView(MONTH_AND_DAY_VIEW)
        updateDisplay(true)
    }

    override fun registerOnDateChangedListener(onDateChangedListener: OnDateChangedListener) {
        mListeners.add(onDateChangedListener)
    }

    fun setOnDateSetListener(onDateSetListener: OnDateSetListener) {
        mCallBack = onDateSetListener
    }

    fun setYearRange(minYear: Int, maxYear: Int) {
        if (maxYear <= minYear) throw IllegalArgumentException("Year end must be larger than year start")
        if (maxYear > MAX_YEAR) throw IllegalArgumentException("max year end must < $MAX_YEAR")
        if (minYear < MIN_YEAR) throw IllegalArgumentException("min year end must > $MIN_YEAR")
        mMinYear = minYear
        mMaxYear = maxYear
        if (mDayPickerView != null) mDayPickerView!!.onChange()
    }

    override fun tryVibrate() {
        if (mVibrator != null && mVibrate) {
            val timeInMillis = SystemClock.uptimeMillis()
            if (timeInMillis - mLastVibrate >= 125L) {
                mVibrator!!.vibrate(5L)
                mLastVibrate = timeInMillis
            }
        }
    }

    fun setCloseOnSingleTapDay(closeOnSingleTapDay: Boolean) {
        mCloseOnSingleTapDay = closeOnSingleTapDay
    }

    interface OnDateChangedListener {
        fun onDateChanged()
    }

    interface OnDateSetListener {
        fun onDateSet(datePickerDialog: DatePickerDialog, year: Int, month: Int, day: Int)

        fun onDateDismissed(datePickerDialog: DatePickerDialog)
    }

    companion object {
        const val ANIMATION_DELAY = 500
        const val KEY_WEEK_START = "week_start"
        const val KEY_YEAR_START = "year_start"
        const val KEY_YEAR_END = "year_end"
        const val KEY_CURRENT_VIEW = "current_view"
        const val KEY_LIST_POSITION = "list_position"
        const val KEY_LIST_POSITION_OFFSET = "list_position_offset"
        private const val KEY_SELECTED_YEAR = "year"
        private const val KEY_SELECTED_MONTH = "month"
        private const val KEY_SELECTED_DAY = "day"
        private const val KEY_VIBRATE = "vibrate"

        private const val MAX_YEAR = 2037
        private const val MIN_YEAR = 1902
        private const val UNINITIALIZED = -1
        private const val MONTH_AND_DAY_VIEW = 0
        private const val YEAR_VIEW = 1

        private val DAY_FORMAT = SimpleDateFormat("dd", Locale.getDefault())
        private val YEAR_FORMAT = SimpleDateFormat("yyyy", Locale.getDefault())

        fun newInstance(onDateSetListener: OnDateSetListener,
                        year: Int,
                        month: Int,
                        day: Int): DatePickerDialog {
            return newInstance(onDateSetListener, year, month, day, true)
        }

        fun newInstance(onDateSetListener: OnDateSetListener,
                        year: Int,
                        month: Int,
                        day: Int,
                        vibrate: Boolean): DatePickerDialog {
            val datePickerDialog = DatePickerDialog()
            datePickerDialog.initialize(onDateSetListener, year, month, day, vibrate)
            return datePickerDialog
        }
    }
}