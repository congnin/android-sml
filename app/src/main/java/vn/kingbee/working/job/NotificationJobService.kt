package vn.kingbee.working.job

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.job.JobParameters
import android.app.job.JobService
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import androidx.core.app.NotificationCompat
import vn.kingbee.widget.R

//https://github.com/google-developer-training/android-fundamentals-apps-v2/tree/master/NotificationScheduler
//https://codelabs.developers.google.com/codelabs/android-training-job-scheduler/index.html?index=..%2F..%2Fandroid-training#0
class NotificationJobService : JobService() {

    lateinit var mNotifyManager: NotificationManager

    /**
     * Creates a Notification channel, for OREO and higher.
     */
    private fun createNotificationChannel() {
        //Define notification manager object.
        mNotifyManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Notification channels are only available in OREO and higher.
        // So, add a check on SDK version.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Create the NotificationChannel with all the parameters.
            val notificationChannel = NotificationChannel(PRIMARY_CHANNEL_ID,
                "Job Service notification", NotificationManager.IMPORTANCE_HIGH)

            notificationChannel.enableLights(true)
            notificationChannel.lightColor = Color.RED
            notificationChannel.enableVibration(true)
            notificationChannel.description = "Notifications from Job Service"

            mNotifyManager.createNotificationChannel(notificationChannel)
        }
    }

    override fun onStopJob(params: JobParameters?): Boolean {
        return false
    }

    override fun onStartJob(params: JobParameters?): Boolean {
        //Create the notification channel
        createNotificationChannel()

        //Set up the notification content intent to launch the app when clicked
        val contentPendingIntent = PendingIntent.getActivity(
            this, 0, Intent(this, JobDemo::class.java),
            PendingIntent.FLAG_UPDATE_CURRENT)

        val builder = NotificationCompat.Builder(this, PRIMARY_CHANNEL_ID)
            .setContentTitle("Job Service")
            .setContentText("Your Job ran to completion!")
            .setContentIntent(contentPendingIntent)
            .setSmallIcon(R.mipmap.ic_launcher_round)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setDefaults(NotificationCompat.DEFAULT_ALL)
            .setAutoCancel(true)

        mNotifyManager.notify(0, builder.build())

        return false
    }

    //Notification channel ID.
    companion object {
        private const val PRIMARY_CHANNEL_ID = "primary_notification_channel"
    }
}