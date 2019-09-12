package vn.kingbee.working.job

import android.annotation.SuppressLint
import android.app.job.JobInfo
import android.app.job.JobScheduler
import android.content.ComponentName
import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import vn.kingbee.widget.R

class JobDemo : AppCompatActivity() {
    @BindView(R.id.networkOptions)
    lateinit var networkOptions: RadioGroup
    @BindView(R.id.idleSwitch)
    lateinit var mDeviceIdleSwitch: Switch
    @BindView(R.id.chargingSwitch)
    lateinit var mDeviceChargingSwitch: Switch
    @BindView(R.id.seekBar)
    lateinit var mSeekBar: SeekBar
    @BindView(R.id.seekBarProgress)
    lateinit var seekBarProgress: TextView

    private var mScheduler: JobScheduler? = null

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_job_demo)
        ButterKnife.bind(this)

        mSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {

            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (progress > 0) {
                    seekBarProgress.text = "$progress s"
                } else {
                    seekBarProgress.text = "Not Set"
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {

            }

        })
    }

    @OnClick(R.id.btScheduleJob)
    fun scheduleJobClicked(view: View) {
        scheduleJob(view)
    }

    @OnClick(R.id.btCancelJob)
    fun cancelJobClicked(view: View) {
        cancelJobs(view)
    }

    private fun scheduleJob(view: View) {
        val selectedNetworkID = networkOptions.checkedRadioButtonId
        var selectedNetworkOption = JobInfo.NETWORK_TYPE_NONE

        when (selectedNetworkID) {
            R.id.noNetwork -> selectedNetworkOption = JobInfo.NETWORK_TYPE_NONE
            R.id.anyNetwork -> selectedNetworkOption = JobInfo.NETWORK_TYPE_ANY
            R.id.wifiNetwork -> selectedNetworkOption = JobInfo.NETWORK_TYPE_UNMETERED
        }

        mScheduler = getSystemService(Context.JOB_SCHEDULER_SERVICE) as JobScheduler
        val serviceName = ComponentName(packageName, NotificationJobService::class.java.name)
        val builder = JobInfo.Builder(JOB_ID, serviceName)
            .setRequiredNetworkType(selectedNetworkOption)
            .setRequiresDeviceIdle(mDeviceIdleSwitch.isChecked)
            .setRequiresCharging(mDeviceChargingSwitch.isChecked)

        val seekBarInteger = mSeekBar.progress.toLong()
        val seekBarSet = seekBarInteger > 0
        if (seekBarSet) {
            builder.setOverrideDeadline(seekBarInteger * 1000)
        }

        val constraintSet = (selectedNetworkOption != JobInfo.NETWORK_TYPE_NONE)
                || mDeviceIdleSwitch.isChecked || mDeviceChargingSwitch.isChecked
                || seekBarSet
        if (constraintSet) {
            val myJobInfo = builder.build()
            mScheduler?.schedule(myJobInfo)

            Toast.makeText(this, "Job Scheduled, job will run when " +
                    "the constraints are met.", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "Please set at least one constraint",
                Toast.LENGTH_SHORT).show()
        }
    }

    private fun cancelJobs(view: View) {
        if (mScheduler != null) {
            mScheduler?.cancelAll()
            mScheduler = null
            Toast.makeText(this, "Jobs cancelled", Toast.LENGTH_SHORT).show()
        }
    }

    companion object {
        private const val JOB_ID = 0
    }
}