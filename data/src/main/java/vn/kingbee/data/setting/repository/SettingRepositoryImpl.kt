package vn.kingbee.data.setting.repository

import io.reactivex.Observable
import vn.kingbee.data.setting.service.SettingService
import vn.kingbee.domain.setting.entity.configuration.ConfigInfo
import vn.kingbee.domain.setting.entity.kiosk.KioskDataResponse
import vn.kingbee.domain.setting.entity.lov.LOV
import vn.kingbee.domain.setting.repository.SettingRepository

class SettingRepositoryImpl : SettingRepository {

    lateinit var settingService: SettingService

    constructor(settingService: SettingService) {
        this.settingService = settingService
    }

    override fun getLOVs(): Observable<LOV> {
        return settingService.getLOVs().map { response -> response.data }
    }

    override fun getConfigInfo(): Observable<ConfigInfo> {
        return settingService.getConfigInfo().map { response -> response.data }
    }

    override fun getKioskConfigInfo(deviceId: String): Observable<List<KioskDataResponse>> {
        return settingService.getKioskConfig(deviceId)
            .map { response -> response.data }
            .map { it.kiosks }
    }
}