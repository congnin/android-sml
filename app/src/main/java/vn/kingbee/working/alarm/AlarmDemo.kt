package vn.kingbee.working.alarm

import android.app.AlarmManager
import android.app.NotificationManager
import android.content.Context
import android.os.Bundle
import android.widget.Toast
import android.widget.ToggleButton
import androidx.appcompat.app.AppCompatActivity
import butterknife.BindView
import butterknife.ButterKnife
import vn.kingbee.widget.R
import android.app.NotificationChannel
import android.graphics.Color
import android.content.Intent
import android.app.PendingIntent
import vn.kingbee.working.alarm.AlarmReceiver.Companion.NOTIFICATION_ID
import vn.kingbee.working.alarm.AlarmReceiver.Companion.PRIMARY_CHANNEL_ID
import android.os.SystemClock
import android.widget.Button

class AlarmDemo : AppCompatActivity() {
    @BindView(R.id.alarmToggle)
    lateinit var alarmToggle: ToggleButton
    @BindView(R.id.btnNextAlarmClock)
    lateinit var btnNextAlarmClock: Button

    lateinit var mNotificationManager: NotificationManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_alarm_demo)
        ButterKnife.bind(this)

        mNotificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val notifyIntent = Intent(this, AlarmReceiver::class.java)
        val notifyPendingIntent = PendingIntent.getBroadcast(
            this, NOTIFICATION_ID, notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT)

        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager

        val alarmUp = (PendingIntent.getBroadcast(this, NOTIFICATION_ID, notifyIntent,
            PendingIntent.FLAG_NO_CREATE) != null)
        alarmToggle.isChecked = alarmUp

        alarmToggle.setOnCheckedChangeListener { _, isChecked ->
            val toastMessage: String = if (isChecked) {
                setInexactRepeating(alarmManager, notifyPendingIntent)
                "Stand Up Alarm On!"
            } else {
                alarmManager.cancel(notifyPendingIntent)
                mNotificationManager.cancelAll()
                "Stand Up Alarm Off!"
            }

            Toast.makeText(this@AlarmDemo, toastMessage, Toast.LENGTH_SHORT).show()
        }

        // Create the notification channel.
        createNotificationChannel()
    }

    private fun setInexactRepeating(alarmManager: AlarmManager?, notifyPendingIntent: PendingIntent) {
        val repeatInterval = AlarmManager.INTERVAL_FIFTEEN_MINUTES
        val triggerTime = SystemClock.elapsedRealtime() + repeatInterval

        //If the Toggle is turned on, set the repeating alarm with a 15 minute interval
        if (alarmManager != null) {
            alarmManager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                triggerTime, repeatInterval, notifyPendingIntent)
        }
    }

    private fun createNotificationChannel() {
        // Create a notification manager object.
        mNotificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Notification channels are only available in OREO and higher.
        // So, add a check on SDK version.
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {

            // Create the NotificationChannel with all the parameters.
            val notificationChannel = NotificationChannel(PRIMARY_CHANNEL_ID,
                "Stand up notification",
                NotificationManager.IMPORTANCE_HIGH)

            notificationChannel.enableLights(true)
            notificationChannel.lightColor = Color.RED
            notificationChannel.enableVibration(true)
            notificationChannel.description = "Notifies every 15 minutes to stand up and walk"
            mNotificationManager.createNotificationChannel(notificationChannel)
        }
    }
}