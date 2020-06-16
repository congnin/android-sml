package vn.kingbee.utils

import timber.log.Timber


object MyLog {
    private const val MY_LOG_TAG = "vtt"

    fun logI(msg: String) {
        logI("", msg)
    }

    fun logI(tag: String, msg: String) {
        Timber.i("[%s] %s-%s %s", currentThreadName, "vtt", tag, msg)
    }

    private val currentThreadName: String
        get() = Thread.currentThread().name

    fun logE(e: Throwable?, tag: String, message: String) {
        Timber.e(e, "[%s] %s-%s %s", currentThreadName, "vtt", tag, message)
    }

    fun logE(tag: String, message: String) {
        logE(null, tag, message)
    }

    fun logE(message: String) {
        logE("", message)
    }

    fun logD(e: InterruptedException, tag: String, message: String) {
        Timber.d(e, "[%s] %s-%s %s", currentThreadName, "vtt", tag, message)
    }
}