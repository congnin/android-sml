package vn.kingbee.data.setting.repository

import io.reactivex.Observable
import vn.kingbee.data.setting.service.SettingService
import vn.kingbee.domain.entity.configuration.ConfigInfo
import vn.kingbee.domain.entity.kiosk.KioskDataResponse
import vn.kingbee.domain.entity.lov.LOV
import vn.kingbee.domain.setting.repository.SettingRepository

class SettingRepositoryImpl : SettingRepository {

    private var settingService: SettingService

    constructor(settingService: SettingService) {
        this.settingService = settingService
    }

    override fun getLOVs(msisdn: String?, authorization: String?): Observable<LOV> {
        return settingService.getLOVs(msisdn, authorization).map { response -> response.data }
    }

    override fun getConfigInfo(msisdn: String?, authorization: String?): Observable<ConfigInfo> {
        return settingService.getConfigInfo(msisdn, authorization).map { response -> response.data }
    }

    override fun getKioskConfigInfo(msisdn: String?, authorization: String?): Observable<List<KioskDataResponse>> {
        return settingService.getKioskConfig(msisdn, msisdn, authorization)
            .map { response -> response.data }
            .map { it.kiosks }
    }
}