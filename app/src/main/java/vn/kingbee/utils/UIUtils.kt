package vn.kingbee.utils

import android.util.DisplayMetrics
import android.view.View
import android.view.WindowManager
import android.view.animation.Animation
import android.view.animation.Transformation
import android.widget.LinearLayout

object UIUtils {
    private const val TABLET_97_WIDTH = 1024

    fun linearExpand(targetView: View, duration: Int) {
        targetView.measure(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
        val targetHeight = targetView.measuredHeight

        targetView.layoutParams.height = 1
        targetView.visibility = View.VISIBLE
        val expandAnimation = object : Animation() {
            override fun applyTransformation(interpolatedTime: Float, t: Transformation) {
                targetView.layoutParams.height = if (interpolatedTime == 1f)
                    LinearLayout.LayoutParams.WRAP_CONTENT
                else
                    (targetHeight * interpolatedTime).toInt()
                targetView.requestLayout()
            }

            override fun willChangeBounds(): Boolean {
                return true
            }
        }

        expandAnimation.duration = duration.toLong()
        targetView.startAnimation(expandAnimation)
    }

    fun linearCollapse(targetView: View, duration: Int) {
        val initialHeight = targetView.measuredHeight

        val collapseAnimation = object : Animation() {
            override fun applyTransformation(interpolatedTime: Float, t: Transformation) {
                if (interpolatedTime == 1f) {
                    targetView.visibility = View.GONE
                } else {
                    targetView.layoutParams.height = initialHeight - (initialHeight * interpolatedTime).toInt()
                    targetView.requestLayout()
                }
            }

            override fun willChangeBounds(): Boolean {
                return true
            }
        }

        collapseAnimation.duration = duration.toLong()
        targetView.startAnimation(collapseAnimation)
    }

    fun isLegacyDevice(windowManager: WindowManager): Boolean {
        val displayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displayMetrics)
        return displayMetrics.widthPixels == TABLET_97_WIDTH
    }
}