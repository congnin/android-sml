package vn.kingbee.widget.progress

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import android.widget.Button
import android.widget.TextView
import io.reactivex.Observable
import kotlinx.android.synthetic.main.activity_circle_process_bar.*
import vn.kingbee.widget.R
import io.reactivex.disposables.Disposable
import java.util.concurrent.TimeUnit
import io.reactivex.android.schedulers.AndroidSchedulers
import vn.kingbee.widget.progress.demo.CircleProgressIndicatorDemo

class CircleProcessBarActivity : AppCompatActivity(), CountDownTimerCallback {
    val QUESTION_TIMEOUT = 30L
    private var mTimeoutProgressBar: CircularProgressbar? = null
    private var mTxtTimeoutCounter: TextView? = null
    private var mTxtTimeoutSeconds: TextView? = null
    private var mCountDownTimerView: CountDownTimerView? = null
    private var btReset: Button? = null
    private var looper: Disposable? = null
    private lateinit var btCircularProgressIndicator: Button

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

        btCircularProgressIndicator = findViewById(R.id.bt_circular_progress_indicator)
        btCircularProgressIndicator.setOnClickListener {
            startActivity(Intent(this@CircleProcessBarActivity, CircleProgressIndicatorDemo::class.java))
        }
        addEvents()
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

    private fun addEvents() {
        forgot_pin_mcq_question_progress_circle.setMax(100)
        Handler().postDelayed({ startCountdownCircleProgress() }, 2000)
    }

    private fun startCountdownCircleProgress() {
        stopCountdownCircleProgress()
        updateCountdownCircleProgress(QUESTION_TIMEOUT)
        looper = Observable
            .interval(1, TimeUnit.SECONDS)
            .take(QUESTION_TIMEOUT)
            .subscribeOn(AndroidSchedulers.mainThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { duration ->
                val timeLeft = QUESTION_TIMEOUT - 1 - duration
                updateCountdownCircleProgress(timeLeft)
                if (timeLeft <= 0) {
                    startCountdownCircleProgress()
                }
            }
    }

    private fun stopCountdownCircleProgress() {
        if (looper != null) {
            looper?.dispose();
            looper = null;
        }
    }

    private fun updateCountdownCircleProgress(timeLeft: Long) {

        forgot_pin_mcq_question_progress_second.text = String.format("%ds", timeLeft)
        forgot_pin_mcq_question_progress_circle.setProgress((timeLeft * 100 / QUESTION_TIMEOUT).toFloat())
    }

    override fun onDestroy() {
        stopCountdownCircleProgress()
        super.onDestroy()
    }
}