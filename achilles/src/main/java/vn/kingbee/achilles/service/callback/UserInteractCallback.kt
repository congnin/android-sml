package vn.kingbee.achilles.service.callback

import vn.kingbee.achilles.service.model.PrepareApi

interface UserInteractCallback {

    val isUserInteractModeEnable: Boolean

    @Deprecated("")
    fun onPrepareApi(var1: String, var2: String, var3: Array<String>)

    fun onPrepareApi(var1: PrepareApi)

    fun onApiStarted(var1: String)
}