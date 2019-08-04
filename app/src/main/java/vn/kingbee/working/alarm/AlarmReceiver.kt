package vn.kingbee.working.alarm

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import vn.kingbee.widget.R

class AlarmReceiver : BroadcastReceiver() {

    lateinit var mNotificationManager: NotificationManager

    override fun onReceive(context: Context, intent: Intent) {
        mNotificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        deliverNotification(context)
    }

    private fun deliverNotification(context: Context) {
        val contentIntent = Intent(context, AlarmDemo::class.java)

        val contentPendingIntent = PendingIntent.getActivity(context, NOTIFICATION_ID,
            contentIntent, PendingIntent.FLAG_UPDATE_CURRENT)

        val builder = NotificationCompat.Builder(context, PRIMARY_CHANNEL_ID)
            .setSmallIcon(R.mipmap.ic_replay)
            .setContentTitle("Stand Up Alert")
            .setContentText("You should stand up and walk around now!")
            .setContentIntent(contentPendingIntent)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .setDefaults(NotificationCompat.DEFAULT_ALL)

        mNotificationManager.notify(NOTIFICATION_ID, builder.build())
    }

    companion object {
        const val NOTIFICATION_ID = 0
        const val PRIMARY_CHANNEL_ID = "primary_notification_channel"
    }
}
