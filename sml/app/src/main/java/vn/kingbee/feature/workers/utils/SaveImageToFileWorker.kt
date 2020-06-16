package vn.kingbee.feature.workers.utils

import android.content.Context
import android.graphics.BitmapFactory
import android.net.Uri
import android.provider.MediaStore
import android.text.TextUtils
import android.util.Log
import androidx.work.Data
import androidx.work.Worker
import androidx.work.WorkerParameters
import timber.log.Timber
import vn.kingbee.feature.workers.Constants
import java.text.SimpleDateFormat
import java.util.*

/**
 * Saves the image to a permanent file
 */
class SaveImageToFileWorker
/**
 * Creates an instance of the [Worker].
 *
 * @param appContext   the application [Context]
 * @param workerParams the set of [WorkerParameters]
 */
    (
    appContext: Context,
    workerParams: WorkerParameters
) : Worker(appContext, workerParams) {

    override fun doWork(): Result {
        val applicationContext = applicationContext

        // Makes a notification when the work starts and slows down the work so that it's easier to
        // see each WorkRequest start, even on emulated devices
        WorkerUtils.makeStatusNotification("Saving image", applicationContext)
        WorkerUtils.sleep()

        val resolver = applicationContext.contentResolver
        try {
            val resourceUri = inputData
                .getString(Constants.KEY_IMAGE_URI)
            val bitmap = BitmapFactory.decodeStream(
                resolver.openInputStream(Uri.parse(resourceUri))
            )
            val imageUrl = MediaStore.Images.Media.insertImage(
                resolver, bitmap, TITLE, DATE_FORMATTER.format(Date())
            )
            if (TextUtils.isEmpty(imageUrl)) {
                Timber.e(TAG,"Writing to MediaStore failed")
                return Result.failure()
            }
            val outputData = Data.Builder()
                .putString(Constants.KEY_IMAGE_URI, imageUrl)
                .build()
            return Result.success(outputData)
        } catch (exception: Exception) {
            Timber.tag(TAG).e(exception, "Unable to save image to Gallery")
            return Result.failure()
        }

    }

    companion object {

        private val TAG = SaveImageToFileWorker::class.java.simpleName

        private val TITLE = "Blurred Image"
        private val DATE_FORMATTER =
            SimpleDateFormat("yyyy.MM.dd 'at' HH:mm:ss z", Locale.getDefault())
    }
}