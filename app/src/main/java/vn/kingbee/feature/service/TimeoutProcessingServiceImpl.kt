package vn.kingbee.feature.service

import android.os.Handler
import java.util.*

class TimeoutProcessingServiceImpl : TimeoutProcessingService {

    private var timeCountDown: Long = 0
    private var mHandler: Handler
    private var runnable: Runnable

    constructor(builder: Builder) {
        timeCountDown = builder.timeCountDown

        mHandler = Handler()
        runnable = Runnable {  }
    }

    override fun start() {
        stop()
        mHandler.postDelayed(runnable, timeCountDown)
    }

    override fun stop() {
        mHandler.removeCallbacks(runnable)
    }

    override fun registerAction(task: TimerTask) {
        this.runnable = task
    }

    class Builder {
        internal var timeCountDown: Long = 0

        fun timeCountDown(value: Long): Builder {
            timeCountDown = value
            return this
        }

        fun build(): TimeoutProcessingServiceImpl {
            return TimeoutProcessingServiceImpl(this)
        }
    }
}