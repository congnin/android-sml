package vn.kingbee.widget.calendar.ggdatepicker

interface DatePickerController {
    fun getFirstDayOfWeek(): Int

    fun getMaxYear(): Int

    fun getMinYear(): Int

    fun getSelectedDay(): SimpleMonthAdapter.CalendarDay

    fun onDayOfMonthSelected(year: Int, month: Int, day: Int)

    fun onYearSelected(year: Int)

    fun registerOnDateChangedListener(onDateChangedListener: DatePickerDialog.OnDateChangedListener)

    fun tryVibrate()
}