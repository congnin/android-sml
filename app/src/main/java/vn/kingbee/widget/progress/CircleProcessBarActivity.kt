package vn.kingbee.widget.progress

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Button
import android.widget.TextView
import vn.kingbee.widget.R

class CircleProcessBarActivity : AppCompatActivity(), CountDownTimerCallback {

    private var mTimeoutProgressBar: CircularProgressbar? = null
    private var mTxtTimeoutCounter: TextView? = null
    private var mTxtTimeoutSeconds: TextView? = null
    private var mCountDownTimerView: CountDownTimerView? = null
    private var btReset: Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_circle_process_bar)
        mTimeoutProgressBar = findViewById(R.id.circular_progress_bar)
        mTxtTimeoutCounter = findViewById(R.id.text_timeout_counter)
        mTxtTimeoutSeconds = findViewById(R.id.text_timeout_seconds)
        btReset = findViewById(R.id.btReset)

        startCountdownTimer()

        btReset!!.setOnClickListener {
            resetCountdownTimer()
        }
    }

    private fun getCountDownTimeGot(timeGot: Int): CountDownTimerView {
        return CountDownTimerView(
            this, mTimeoutProgressBar!!, mTxtTimeoutCounter!!,
            mTxtTimeoutSeconds!!, timeGot, this
        )
    }


    override fun onFinish() {
        stopCountdownTimer()
    }

    override fun onTick(l: Long) {

    }

    private fun startCountdownTimer() {
        if (mCountDownTimerView == null) {
            mCountDownTimerView = getCountDownTimeGot(30)
        }
        mCountDownTimerView!!.startCountDownTimer()
    }

    private fun stopCountdownTimer() {
        if (mCountDownTimerView != null) {
            mCountDownTimerView!!.cancelCountDownTimer()
            mCountDownTimerView = null
        }
    }

    private fun resetCountdownTimer() {
        stopCountdownTimer()
        startCountdownTimer()
    }
}