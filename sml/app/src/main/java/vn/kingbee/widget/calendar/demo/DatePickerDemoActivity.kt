package vn.kingbee.widget.calendar.demo

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import timber.log.Timber
import vn.kingbee.utils.DateUtil
import vn.kingbee.widget.BaseActivity
import vn.kingbee.widget.R
import vn.kingbee.widget.calendar.datepicker.DatePickerFragment
import vn.kingbee.widget.notification.NotificationManager
import vn.kingbee.widget.notification.NotificationManagerImpl
import vn.kingbee.widget.notification.enums.NotificationType
import vn.kingbee.widget.toc.TOCDemo
import java.text.ParseException
import java.util.*

class DatePickerDemoActivity : BaseActivity(), View.OnClickListener {

    lateinit var calendarControl: View
    lateinit var mDate: TextView
    lateinit var notificationManager: NotificationManager<*>
    lateinit var btGood: Button
    lateinit var btError: Button
    lateinit var btInfo: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_date_picker_demo)
        calendarControl = findViewById(R.id.calendarControl)
        mDate = findViewById(R.id.tv_date)
        btGood = findViewById(R.id.bt_good_notification)
        btError = findViewById(R.id.bt_error_notification)
        btInfo = findViewById(R.id.bt_info_notification)

        notificationManager = NotificationManagerImpl(window)

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

        btGood.setOnClickListener(this)
        btError.setOnClickListener(this)
        btInfo.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        if (v != null) {
            when (v.id) {
                R.id.bt_good_notification
                -> notificationManager.show("Your information is saved successfully", NotificationType.GOOD)
                R.id.bt_error_notification
                -> notificationManager.show("Network is turned off", NotificationType.ERROR)
                R.id.bt_info_notification
                -> notificationManager.show("Your name is changed", NotificationType.INFO)
            }
        }
    }

    fun startTOCDemoActivity(v: View) {
        startActivity(Intent(this@DatePickerDemoActivity, TOCDemo::class.java))
    }
}