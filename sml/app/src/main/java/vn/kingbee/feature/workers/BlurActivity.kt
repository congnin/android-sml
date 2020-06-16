package vn.kingbee.feature.workers

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.RadioGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import vn.kingbee.widget.R

class BlurActivity : AppCompatActivity() {
    lateinit var mViewModel: BlurViewModel
    lateinit var mImageView: ImageView
    lateinit var mProgressBar: ProgressBar
    lateinit var mGoButton: Button
    lateinit var mOutputButton: Button
    lateinit var mCancelButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_blur)

        mViewModel = ViewModelProviders.of(this).get(BlurViewModel::class.java)

        mImageView = findViewById(R.id.image_view)
        mProgressBar = findViewById(R.id.progress_bar)
        mGoButton = findViewById(R.id.go_button)
        mOutputButton = findViewById(R.id.see_file_button)
        mCancelButton = findViewById(R.id.cancel_button)

        // Image uri should be stored in the ViewModel; put it there then display
        val intent = intent
        val imageUriExtra = intent.getStringExtra(Constants.KEY_IMAGE_URI)
        mViewModel.setImageUri(imageUriExtra)
        if (mViewModel.imageUri != null) {
            Glide.with(this).load(mViewModel.imageUri).into(mImageView)
        }

        // Setup blur image file button
        mGoButton.setOnClickListener { view -> mViewModel.applyBlur(getBlurLevel()) }

        // Setup view output image file button
        mOutputButton.setOnClickListener {
            val currentUri = mViewModel.outputUri
            if (currentUri != null) {
                val actionView = Intent(Intent.ACTION_VIEW, currentUri)
                if (actionView.resolveActivity(packageManager) != null) {
                    startActivity(actionView)
                }
            }
        }

        // Hookup the Cancel button
        mCancelButton.setOnClickListener { view -> mViewModel.cancelWork() }

        // Show work status
        mViewModel.outputWorkInfo.observe(this, Observer { listOfWorkInfo ->
            // Note that these next few lines grab a single WorkInfo if it exists
            // This code could be in a Transformation in the ViewModel; they are included here
            // so that the entire process of displaying a WorkInfo is in one location.

            // If there are no matching work info, do nothing
            if (listOfWorkInfo == null || listOfWorkInfo.isEmpty()) {
                return@Observer
            }

            // We only care about the one output status.
            // Every continuation has only one worker tagged TAG_OUTPUT
            val workInfo = listOfWorkInfo[0]

            val finished = workInfo.state.isFinished
            if (!finished) {
                this@BlurActivity.showWorkInProgress()
            } else {
                this@BlurActivity.showWorkFinished()

                // Normally this processing, which is not directly related to drawing views on
                // screen would be in the ViewModel. For simplicity we are keeping it here.
                val outputData = workInfo.outputData

                val outputImageUri = outputData.getString(Constants.KEY_IMAGE_URI)

                // If there is an output file show "See File" button
                if (outputImageUri != null && !TextUtils.isEmpty(outputImageUri)) {
                    mViewModel.setOutputUri(outputImageUri)
                    mOutputButton.visibility = View.VISIBLE
                }
            }
        })
    }

    /**
     * Shows and hides views for when the Activity is processing an image
     */
    private fun showWorkInProgress() {
        mProgressBar.visibility = View.VISIBLE
        mCancelButton.visibility = View.VISIBLE
        mGoButton.visibility = View.GONE
        mOutputButton.visibility = View.GONE
    }

    /**
     * Shows and hides views for when the Activity is done processing an image
     */
    private fun showWorkFinished() {
        mProgressBar.visibility = View.GONE
        mCancelButton.visibility = View.GONE
        mGoButton.visibility = View.VISIBLE
    }

    /**
     * Get the blur level from the radio button as an integer
     * @return Integer representing the amount of times to blur the image
     */
    private fun getBlurLevel(): Int {
        val radioGroup = findViewById<RadioGroup>(R.id.radio_blur_group)

        when (radioGroup.checkedRadioButtonId) {
            R.id.radio_blur_lv_1 -> return 1
            R.id.radio_blur_lv_2 -> return 2
            R.id.radio_blur_lv_3 -> return 3
        }

        return 1
    }
}