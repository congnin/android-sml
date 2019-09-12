package vn.kingbee.application.appbus

import vn.kingbee.domain.dataprocess.AppBus

class AppBusImpl : AppBus {
    private var msisdn: String? = null

    override fun setMsisdn(value: String?) {
        this.msisdn = value
    }

    override fun getMsisdn(): String? {
        return msisdn
    }
}