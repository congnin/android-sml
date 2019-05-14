package vn.kingbee.widget.calendar.demo

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import timber.log.Timber
import vn.kingbee.utils.DateUtil
import vn.kingbee.widget.BaseActivity
import vn.kingbee.widget.R
import vn.kingbee.widget.calendar.datepicker.DatePickerFragment
import vn.kingbee.widget.toc.TOCDemo
import java.text.ParseException
import java.util.*

class DatePickerDemoActivity : BaseActivity() {

    lateinit var calendarControl : View
    lateinit var mDate: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_date_picker_demo)
        calendarControl = findViewById(R.id.calendarControl)
        mDate = findViewById(R.id.tv_date)

        calendarControl.setOnClickListener {
            var date: Date? = null

            try {
                date = DateUtil.getDate(mDate.text as String, DateUtil.DD_MM_YYYY)
            } catch (e: ParseException) {
                Timber.e(e, "cannot parse date string in " +
                        "calendarTextView: " + mDate.text)
            }


            val dialogFragment = DatePickerFragment.newInstance(date)
            dialogFragment
                .getDateChoose()
                .subscribe { selectedDate -> mDate.text = DateUtil.format(selectedDate, DateUtil.DD_MM_YYYY) }
            dialogFragment.show(supportFragmentManager, "datePicker")
        }
    }

    fun startTOCDemoActivity(v: View) {
        startActivity(Intent(this@DatePickerDemoActivity, TOCDemo::class.java))
    }
}