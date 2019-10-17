package vn.kingbee.feature.workers.utils

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.renderscript.Allocation
import android.renderscript.Element
import android.renderscript.RenderScript
import android.renderscript.ScriptIntrinsicBlur
import androidx.annotation.WorkerThread
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import timber.log.Timber
import vn.kingbee.feature.workers.Constants
import vn.kingbee.widget.R
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import java.util.*

object WorkerUtils {
    private val TAG = WorkerUtils.javaClass.simpleName

    fun makeStatusNotification(message: String, context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = Constants.VERBOSE_NOTIFICATION_CHANNEL_NAME
            val description = Constants.VERBOSE_NOTIFICATION_CHANNEL_DESCRIPTION
            val important = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(Constants.CHANNEL_ID, name, important)
            channel.description = description

            val notificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }

        val builder = NotificationCompat.Builder(context, Constants.CHANNEL_ID)
            .setSmallIcon(R.mipmap.ic_launcher_foreground)
            .setContentTitle(Constants.NOTIFICATION_TITLE)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setVibrate(LongArray(0))

        NotificationManagerCompat.from(context).notify(Constants.NOTIFICATION_ID, builder.build())
    }

    fun sleep() {
        try {
            Thread.sleep(Constants.DELAY_TIME_MILLIS, 0)
        } catch (e: InterruptedException) {
            Timber.d(TAG, e.message)
        }
    }

    @WorkerThread
    fun blurBitmap(
        bitmap: Bitmap,
        applicationContext: Context
    ): Bitmap {

        var rsContext: RenderScript? = null
        try {

            // Create the output bitmap
            val output = Bitmap.createBitmap(
                bitmap.width, bitmap.height, bitmap.config
            )

            // Blur the image
            rsContext = RenderScript.create(applicationContext, RenderScript.ContextType.DEBUG)
            val inAlloc = Allocation.createFromBitmap(rsContext, bitmap)
            val outAlloc = Allocation.createTyped(rsContext, inAlloc.getType())
            val theIntrinsic = ScriptIntrinsicBlur.create(rsContext, Element.U8_4(rsContext))
            theIntrinsic.setRadius(10f)
            theIntrinsic.setInput(inAlloc)
            theIntrinsic.forEach(outAlloc)
            outAlloc.copyTo(output)

            return output
        } finally {
            if (rsContext != null) {
                rsContext.finish()
            }
        }
    }

    @Throws(FileNotFoundException::class)
    fun writeBitmapToFile(
        applicationContext: Context,
        bitmap: Bitmap
    ): Uri {

        val name = String.format("blur-filter-output-%s.png", UUID.randomUUID().toString())
        val outputDir = File(applicationContext.filesDir, Constants.OUTPUT_PATH)
        if (!outputDir.exists()) {
            outputDir.mkdirs() // should succeed
        }
        val outputFile = File(outputDir, name)
        var out: FileOutputStream? = null
        try {
            out = FileOutputStream(outputFile)
            bitmap.compress(Bitmap.CompressFormat.PNG, 0 /* ignored for PNG */, out)
        } finally {
            if (out != null) {
                try {
                    out.close()
                } catch (ignore: IOException) {
                }

            }
        }
        return Uri.fromFile(outputFile)
    }
}