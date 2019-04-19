package vn.kingbee.widget.calendar.ggdatepicker

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import android.widget.BaseAdapter
import java.util.*

class SimpleMonthAdapter(context: Context,
                         datePickerController: DatePickerController) : BaseAdapter(), SimpleMonthView.OnDayClickListener {

    companion object {
        const val MONTHS_IN_YEAR = 12
        protected var WEEK_7_OVERHANG_HEIGHT = 7
    }

    private val mContext: Context = context
    private val mController: DatePickerController = datePickerController

    private var mSelectedDay: CalendarDay? = null

    init {
        init()
        setSelectedDay(mController.getSelectedDay())
    }

    private fun isSelectedDayInMonth(year: Int, month: Int): Boolean {
        return mSelectedDay!!.year == year && mSelectedDay!!.month == month
    }

    override fun getCount(): Int = (mController.getMaxYear() - mController.getMinYear() + 1) * MONTHS_IN_YEAR

    override fun getItem(position: Int): Any? = null

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val v: SimpleMonthView
        var drawingParams: HashMap<String, Int>? = null
        if (convertView != null) {
            v = convertView as SimpleMonthView
            drawingParams = v.tag as HashMap<String, Int>
        } else {
            v = SimpleMonthView(mContext)
            v.layoutParams = AbsListView.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT
            )
            v.isClickable = true
            v.setOnDayClickListener(this)
        }
        if (drawingParams == null) {
            drawingParams = HashMap()
        }
        drawingParams.clear()

        val month = position % MONTHS_IN_YEAR
        val year = position / MONTHS_IN_YEAR + mController.getMinYear()

        var selectedDay = -1
        if (isSelectedDayInMonth(year, month)) {
            selectedDay = mSelectedDay!!.day
        }

        v.reuse()

        drawingParams[SimpleMonthView.VIEW_PARAMS_SELECTED_DAY] = selectedDay
        drawingParams[SimpleMonthView.VIEW_PARAMS_YEAR] = year
        drawingParams[SimpleMonthView.VIEW_PARAMS_MONTH] = month
        drawingParams[SimpleMonthView.VIEW_PARAMS_WEEK_START] = mController.getFirstDayOfWeek()
        v.setMonthParams(drawingParams)
        v.invalidate()

        return v
    }

    protected fun init() {
        mSelectedDay = CalendarDay(System.currentTimeMillis())
    }

    override fun onDayClick(simpleMonthView: SimpleMonthView, calendarDay: CalendarDay) {
        onDayTapped(calendarDay)
    }

    protected fun onDayTapped(calendarDay: CalendarDay) {
        mController.tryVibrate()
        mController.onDayOfMonthSelected(calendarDay.year, calendarDay.month, calendarDay.day)
        setSelectedDay(calendarDay)
    }

    fun setSelectedDay(calendarDay: CalendarDay) {
        mSelectedDay = calendarDay
        notifyDataSetChanged()
    }

    class CalendarDay {
        internal var day: Int = 0
        internal var month: Int = 0
        internal var year: Int = 0
        internal var calendar: Calendar? = null

        constructor() {
            setTime(System.currentTimeMillis())
        }

        constructor(year: Int, month: Int, day: Int) {
            setDay(year, month, day)
        }

        constructor(timeInMillis: Long) {
            setTime(timeInMillis)
        }

        constructor(calendar: Calendar) {
            year = calendar.get(Calendar.YEAR)
            month = calendar.get(Calendar.MONTH)
            day = calendar.get(Calendar.DAY_OF_MONTH)
        }

        private fun setTime(timeInMillis: Long) {
            if (calendar == null) {
                calendar = Calendar.getInstance()
            }
            calendar!!.timeInMillis = timeInMillis
            month = this.calendar!!.get(Calendar.MONTH)
            year = this.calendar!!.get(Calendar.YEAR)
            day = this.calendar!!.get(Calendar.DAY_OF_MONTH)
        }

        fun set(calendarDay: CalendarDay) {
            year = calendarDay.year
            month = calendarDay.month
            day = calendarDay.day
        }

        fun setDay(year: Int, month: Int, day: Int) {
            this.year = year
            this.month = month
            this.day = day
        }
    }
}