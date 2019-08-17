package vn.kingbee.feature.base.callback

import okhttp3.Headers

interface IBaseError {
    fun onServerError()

    fun onUnKnowError()

    fun onTimeOutError()

    fun onNoInternetConnectionError()

    fun onHttpError(e: Throwable)

    fun onHeaderError(headers: Headers) {}

    fun onEmptyResponse() {}

    fun onNetError(netUntil: String) {}
}