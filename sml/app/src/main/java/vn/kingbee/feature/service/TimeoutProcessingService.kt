package vn.kingbee.feature.service

import java.util.*

interface TimeoutProcessingService {
    fun start()

    fun stop()

    fun registerAction(task : TimerTask)
}