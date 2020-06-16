package vn.kingbee.feature.workers

import android.app.Application
import android.net.Uri
import android.text.TextUtils
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.work.*
import vn.kingbee.feature.workers.Constants.IMAGE_MANIPULATION_WORK_NAME
import vn.kingbee.feature.workers.Constants.KEY_IMAGE_URI
import vn.kingbee.feature.workers.Constants.TAG_OUTPUT
import vn.kingbee.feature.workers.utils.BlurWorker
import vn.kingbee.feature.workers.utils.CleanupWorker
import vn.kingbee.feature.workers.utils.SaveImageToFileWorker

class BlurViewModel(application: Application) : AndroidViewModel(application) {

    private val mWorkManager: WorkManager = WorkManager.getInstance(application)
    /**
     * Getters
     */
    internal var imageUri: Uri? = null
        private set
    internal var outputUri: Uri? = null
        private set
    internal val outputWorkInfo: LiveData<List<WorkInfo>>

    init {

        // This transformation makes sure that whenever the current work Id changes the WorkInfo
        // the UI is listening to changes
        outputWorkInfo = mWorkManager.getWorkInfosByTagLiveData(TAG_OUTPUT)
    }

    /**
     * Create the WorkRequest to apply the blur and save the resulting image
     * @param blurLevel The amount to blur the image
     */
    internal fun applyBlur(blurLevel: Int) {

        // Add WorkRequest to Cleanup temporary images
        var continuation = mWorkManager
            .beginUniqueWork(
                IMAGE_MANIPULATION_WORK_NAME,
                ExistingWorkPolicy.REPLACE,
                OneTimeWorkRequest.from(CleanupWorker::class.java)
            )

        // Add WorkRequests to blur the image the number of times requested
        for (i in 0 until blurLevel) {
            val blurBuilder = OneTimeWorkRequest.Builder(BlurWorker::class.java)

            // Input the Uri if this is the first blur operation
            // After the first blur operation the input will be the output of previous
            // blur operations.
            if (i == 0) {
                blurBuilder.setInputData(createInputDataForUri())
            }

            continuation = continuation.then(blurBuilder.build())
        }

        // Create charging constraint
        val constraints = Constraints.Builder()
            .setRequiresCharging(true)
            .build()

        // Add WorkRequest to save the image to the filesystem
        val save = OneTimeWorkRequest.Builder(SaveImageToFileWorker::class.java)
            .setConstraints(constraints)
            .addTag(TAG_OUTPUT)
            .build()
        continuation = continuation.then(save)

        // Actually start the work
        continuation.enqueue()

    }

    /**
     * Cancel work using the work's unique name
     */
    internal fun cancelWork() {
        mWorkManager.cancelUniqueWork(IMAGE_MANIPULATION_WORK_NAME)
    }

    /**
     * Creates the input data bundle which includes the Uri to operate on
     * @return Data which contains the Image Uri as a String
     */
    private fun createInputDataForUri(): Data {
        val builder = Data.Builder()
        if (imageUri != null) {
            builder.putString(KEY_IMAGE_URI, imageUri!!.toString())
        }
        return builder.build()
    }

    private fun uriOrNull(uriString: String): Uri? {
        return if (!TextUtils.isEmpty(uriString)) {
            Uri.parse(uriString)
        } else null
    }

    /**
     * Setters
     */
    internal fun setImageUri(uri: String) {
        imageUri = uriOrNull(uri)
    }

    internal fun setOutputUri(outputImageUri: String) {
        outputUri = uriOrNull(outputImageUri)
    }
}