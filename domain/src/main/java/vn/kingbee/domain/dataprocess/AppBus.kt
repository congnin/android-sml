package vn.kingbee.domain.dataprocess

interface AppBus {
    fun setMsisdn(value: String?)

    fun getMsisdn(): String?
}