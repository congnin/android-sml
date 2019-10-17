package vn.kingbee.feature.workers.utils

import android.content.Context
import android.graphics.BitmapFactory
import android.net.Uri
import android.text.TextUtils
import androidx.work.Data
import androidx.work.Worker
import androidx.work.WorkerParameters
import timber.log.Timber
import vn.kingbee.feature.workers.Constants
import java.io.FileNotFoundException

class BlurWorker(appContext: Context, workerParams: WorkerParameters) : Worker(
    appContext,
    workerParams
) {

    override fun doWork(): Result {
        val applicationContext: Context = applicationContext

        // Makes a notification when the work starts and slows down the work so that it's easier to
        // see each WorkRequest start, even on emulated devices
        WorkerUtils.makeStatusNotification("Blurring image", applicationContext)
        WorkerUtils.sleep()

        val resourceUri = inputData.getString(Constants.KEY_IMAGE_URI)
        try {
            if (TextUtils.isEmpty(resourceUri)) {
                Timber.e(TAG, "Invalid input uri")
                throw IllegalArgumentException("Invalid input uri")
            }

            val resolver = applicationContext.contentResolver

            // Create a bitmap
            val bitmap = BitmapFactory.decodeStream(
                resolver.openInputStream(Uri.parse(resourceUri))
            )

            // Blur the bitmap
            val output = WorkerUtils.blurBitmap(bitmap, applicationContext)

            // Write bitmap to a temp file
            val outputUri = WorkerUtils.writeBitmapToFile(applicationContext, output)

            // Return the output for the temp file
            val outputData = Data.Builder().putString(
                Constants.KEY_IMAGE_URI, outputUri.toString()
            ).build()

            // If there were no errors, return SUCCESS
            return Result.success(outputData)
        } catch (fileNotFoundException: FileNotFoundException) {
            Timber.e(fileNotFoundException, TAG, "Failed to decode input stream")
            throw RuntimeException("Failed to decode input stream", fileNotFoundException)

        } catch (throwable: Throwable) {

            // If there were errors, return FAILURE
            Timber.e(throwable, TAG, "Error applying blur")
            return Result.failure()
        }
    }

    companion object {
        private val TAG = BlurWorker::class.java.simpleName
    }
}