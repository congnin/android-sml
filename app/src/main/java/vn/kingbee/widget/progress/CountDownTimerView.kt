package vn.kingbee.widget.progress

import android.content.Context
import android.os.Handler
import android.widget.TextView
import vn.kingbee.widget.R

import java.text.DecimalFormat

class CountDownTimerView(
    private val mContext: Context,
    private val mTimeoutProgressBar: CircularProgressbar,
    private val mTxtTimeoutCounter: TextView,
    private val mTxtTimeoutSeconds: TextView,
    private val mCountDownTimer: Int,
    private val mTimeOutListener: CountDownTimerCallback?
) {
    private val mTimeOutHandler = Handler()
    private var mCountDown: Int = 0
    private val mRunnableCountDow = object : Runnable {
        override fun run() {
            this@CountDownTimerView.mCountDown--
            if (this@CountDownTimerView.mCountDown < 0) {
                if (this@CountDownTimerView.mTimeOutListener != null) {
                    this@CountDownTimerView.mTimeOutListener.onFinish()
                }
            } else {
                this@CountDownTimerView.setTextCounter()
                this@CountDownTimerView.mTimeOutHandler.postDelayed(this, this@CountDownTimerView.timeDelay.toLong())
            }

        }
    }

    private val timeDelay: Int
        get() = if (this.mCountDown > 0) 990 else TIME_DELAY_TO_END

    init {
        this.setupProgress()
    }

    fun startCountDownTimer() {
        this.mCountDown = this.mCountDownTimer
        this.setTextCounter()
        this.cancelCountDownTimer()
        this.mTimeOutHandler.postDelayed(this.mRunnableCountDow, this.timeDelay.toLong())
        this.mTimeoutProgressBar.setProgress(this.mCountDownTimer.toFloat(), true)
    }

    fun cancelCountDownTimer() {
        this.mTimeOutHandler.removeCallbacksAndMessages(null as Any?)
        this.mTimeoutProgressBar.stopAnimation()
    }

    private fun setTextCounter() {
        val decimalFormat = DecimalFormat("00")
        this.mTxtTimeoutCounter.text = decimalFormat.format(this.mCountDown.toLong())
        if (this.mCountDown <= 1) {
            this.mTxtTimeoutSeconds.text = this.mContext.getString(R.string.LBL_SECOND)
        } else {
            this.mTxtTimeoutSeconds.text = this.mContext.getString(R.string.LBL_SECONDS)
        }

    }

    private fun setupProgress() {
        this.mTimeoutProgressBar.setMaxProgress(this.mCountDownTimer.toFloat())
        this.mTimeoutProgressBar.setAnimationDuration((this.mCountDownTimer * TIME_DELAY_ONE_SECOND).toLong())
    }

    companion object {
        private val TIME_DELAY_ONE_SECOND = 1000
        private val TIME_DELAY_TO_END = 10
    }
}
