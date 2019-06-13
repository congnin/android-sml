package vn.kingbee.utils

import android.annotation.SuppressLint
import android.os.Build

@SuppressLint("ObsoleteSdkInt")
object SystemUtil {

    fun hasKitKat(): Boolean {
        return Build.VERSION.SDK_INT >= 19
    }

    fun hasLollipop(): Boolean {
        return Build.VERSION.SDK_INT >= 21
    }

    fun hasJellyBean(): Boolean {
        return Build.VERSION.SDK_INT >= 16
    }
}