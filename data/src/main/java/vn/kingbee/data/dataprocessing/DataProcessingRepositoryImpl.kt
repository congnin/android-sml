package vn.kingbee.data.dataprocessing

import android.content.Context
import vn.kingbee.domain.dataprocess.AppBus
import vn.kingbee.domain.dataprocess.DataProcessingRepository
import vn.kingbee.domain.dataprocess.Runtime
import javax.inject.Inject

class DataProcessingRepositoryImpl : DataProcessingRepository {

    private val runtime: Runtime
    private val appBus: AppBus

    constructor(
        runtime: Runtime,
        appBus: AppBus) {
        this.runtime = runtime
        this.appBus = appBus
    }

    override fun getAccessToken(): String? {
        return runtime.getAppToken()?.accessToken
    }

    override fun getMsisdn(): String? {
        return appBus.getMsisdn()
    }
}