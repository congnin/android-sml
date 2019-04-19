package vn.kingbee.widget.calendar.demo

import android.os.Bundle
import android.view.View
import org.joda.time.DateTime
import vn.kingbee.widget.BaseActivity
import vn.kingbee.widget.R
import vn.kingbee.widget.calendar.ggdatepicker.DatePickerDialog

class DatePickerDemoActivity : BaseActivity(), DatePickerDialog.OnDateSetListener {
    override fun onDateSet(datePickerDialog: DatePickerDialog, year: Int, month: Int, day: Int) {

    }

    override fun onDateDismissed(datePickerDialog: DatePickerDialog) {

    }

    private var mSelectedReminderDateTime: DateTime = DateTime()
    private val DIALOG_CALENDAR = "calendar"

    var calendarControl : View? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_date_picker_demo)
        calendarControl = findViewById(R.id.calendarControl)

        calendarControl?.setOnClickListener {
            val datePickerDialog = DatePickerDialog.newInstance(
                this,
                mSelectedReminderDateTime.year,
                mSelectedReminderDateTime.monthOfYear - 1,
                mSelectedReminderDateTime.dayOfMonth,
                false
            )
            datePickerDialog.setYearRange(
                mSelectedReminderDateTime.year - 1,
                mSelectedReminderDateTime.year + 1
            )
            datePickerDialog.show(supportFragmentManager, DIALOG_CALENDAR)
        }
    }
}