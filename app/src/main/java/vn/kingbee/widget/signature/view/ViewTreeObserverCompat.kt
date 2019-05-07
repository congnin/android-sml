package vn.kingbee.widget.signature.view

import android.annotation.SuppressLint
import android.os.Build
import android.view.ViewTreeObserver

class ViewTreeObserverCompat {
    companion object {
        /**
         * Remove a previously installed global layout callback.
         * @param observer the view observer
         * @param victim the victim
         */
        @SuppressLint("NewApi", "ObsoleteSdkInt")
        fun removeOnGlobalLayoutListener(observer: ViewTreeObserver,
                                         victim: ViewTreeObserver.OnGlobalLayoutListener) {
            // Future (API16+)...
            if (Build.VERSION.SDK_INT >= 16) {
                observer.removeOnGlobalLayoutListener(victim)
            } else {
                observer.removeGlobalOnLayoutListener(victim)
            }// Legacy...
        }
    }
}