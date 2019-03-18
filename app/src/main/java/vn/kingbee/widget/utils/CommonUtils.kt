package vn.kingbee.widget.utils

import android.os.SystemClock

class CommonUtils {

    companion object {
        private val EXPECT_DELAY_TIME_BETWEEN_CLICKS_DEFAULT: Long = 650
        private var lastClickTime: Long = 0
        fun isClickAvailable(): Boolean {
            return isClickAvailable(EXPECT_DELAY_TIME_BETWEEN_CLICKS_DEFAULT)
        }

        fun isClickAvailable(expectDelayTimeBetweenClicks: Long): Boolean {
            val currentTime = SystemClock.elapsedRealtime()
            if (currentTime - lastClickTime > expectDelayTimeBetweenClicks) {
                lastClickTime = currentTime
                return true
            } else {
                return false
            }
        }
    }


}
