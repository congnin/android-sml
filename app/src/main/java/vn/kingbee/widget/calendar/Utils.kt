package vn.kingbee.widget.calendar

import android.animation.Keyframe
import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.annotation.SuppressLint
import android.os.Build
import android.view.View
import android.view.accessibility.AccessibilityManager
import java.util.*

class Utils {
    companion object {
        private const val PULSE_ANIMATOR_DURATION = 544

        fun getDaysInMonth(month: Int, year: Int): Int {
            when (month) {
                Calendar.JANUARY, Calendar.MARCH, Calendar.MAY, Calendar.JULY, Calendar.AUGUST, Calendar.OCTOBER, Calendar.DECEMBER -> return 31
                Calendar.APRIL, Calendar.JUNE, Calendar.SEPTEMBER, Calendar.NOVEMBER -> return 30
                Calendar.FEBRUARY -> return if (year % 4 == 0) 29 else 28
                else -> throw IllegalArgumentException("Invalid Month")
            }
        }

        fun getPulseAnimator(labelToAnimate: View,
                             decreaseRatio: Float,
                             increaseRatio: Float): ObjectAnimator {
            val k0 = Keyframe.ofFloat(0f, 1f)
            val k1 = Keyframe.ofFloat(0.275f, decreaseRatio)
            val k2 = Keyframe.ofFloat(0.69f, increaseRatio)
            val k3 = Keyframe.ofFloat(1f, 1f)

            val scaleX = PropertyValuesHolder.ofKeyframe("scaleX", k0, k1, k2, k3)
            val scaleY = PropertyValuesHolder.ofKeyframe("scaleY", k0, k1, k2, k3)
            val pulseAnimator =
                ObjectAnimator.ofPropertyValuesHolder(labelToAnimate, scaleX, scaleY)
            pulseAnimator.duration = PULSE_ANIMATOR_DURATION.toLong()

            return pulseAnimator
        }

        @SuppressLint("ObsoleteSdkInt")
        fun isJellybeanOrLater(): Boolean {
            return Build.VERSION.SDK_INT >= 16
        }

        /**
         * Try to speak the specified text, for accessibility. Only available on JB or later.
         *
         * @param text Text to announce.
         */
        @SuppressLint("NewApi")
        fun tryAccessibilityAnnounce(view: View?, text: CharSequence?) {
            if (isJellybeanOrLater() && view != null && text != null) {
                view.announceForAccessibility(text)
            }
        }

        @SuppressLint("ObsoleteSdkInt")
        fun isTouchExplorationEnabled(accessibilityManager: AccessibilityManager): Boolean {
            return if (Build.VERSION.SDK_INT >= 14) {
                accessibilityManager.isTouchExplorationEnabled
            } else {
                false
            }
        }
    }
}