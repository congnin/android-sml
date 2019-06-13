package vn.kingbee.utils

import android.animation.Animator
import android.view.View
import androidx.annotation.NonNull
import timber.log.Timber

object AnimationUtil {
    fun translateY(@NonNull view: View, translateY: Int, duration: Int, listener: Animator.AnimatorListener) {
        Timber.d("Animation", "translate: $translateY")
        view.animate()
            .translationY(translateY.toFloat())
            .setDuration(duration.toLong())
            .setListener(listener)
            .start()
    }
}