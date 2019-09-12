package vn.kingbee.widget.progress

interface CountDownTimerCallback {
    fun onTick(l: Long)

    fun onFinish()
}
