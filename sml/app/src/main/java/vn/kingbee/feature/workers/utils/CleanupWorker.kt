package vn.kingbee.feature.workers.utils

import android.content.Context
import android.text.TextUtils
import androidx.work.Worker
import androidx.work.WorkerParameters
import timber.log.Timber
import vn.kingbee.feature.workers.Constants
import java.io.File

/**
 * Cleans up temporary files generated during blurring process
 */
class CleanupWorker
/**
 * Creates an instance of the [Worker].
 *
 * @param appContext   the application [Context]
 * @param workerParams the set of [WorkerParameters]
 */
    (appContext: Context, workerParams: WorkerParameters) : Worker(appContext, workerParams) {

    override fun doWork(): Result {
        val applicationContext = applicationContext

        // Makes a notification when the work starts and slows down the work so that it's easier to
        // see each WorkRequest start, even on emulated devices
        WorkerUtils.makeStatusNotification(
            "Cleaning up old temporary files",
            applicationContext
        )
        WorkerUtils.sleep()

        try {
            val outputDirectory = File(
                applicationContext.filesDir,
                Constants.OUTPUT_PATH
            )
            if (outputDirectory.exists()) {
                val entries = outputDirectory.listFiles()
                if (entries != null && entries.size > 0) {
                    for (entry in entries) {
                        val name = entry.name
                        if (!TextUtils.isEmpty(name) && name.endsWith(".png")) {
                            val deleted = entry.delete()
                            Timber.i(String.format("Deleted %s - %s", name, deleted))
                        }
                    }
                }
            }
            return Result.success()
        } catch (exception: Exception) {
            Timber.e(exception, "Error cleaning up")
            return Result.failure()
        }

    }

    companion object {

        private val TAG = CleanupWorker::class.java.simpleName
    }
}