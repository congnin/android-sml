package vn.kingbee.domain.setting.usecase

import io.reactivex.Observable
import vn.kingbee.domain.setting.entity.configuration.ConfigInfo
import vn.kingbee.domain.setting.entity.kiosk.KioskDataResponse
import vn.kingbee.domain.setting.entity.lov.LOV
import vn.kingbee.domain.setting.repository.SettingRepository

class SettingUseCaseImpl : SettingUseCase {

    lateinit var settingRepository: SettingRepository

    constructor(settingRepository: SettingRepository) {
        this.settingRepository = settingRepository
    }

    override fun getLOVs(): Observable<LOV> {
        return settingRepository.getLOVs()
    }

    override fun getConfigInfo(): Observable<ConfigInfo> {
        return settingRepository.getConfigInfo()
    }

    override fun getKioskConfigInfo(deviceId: String): Observable<List<KioskDataResponse>> {
        return settingRepository.getKioskConfigInfo(deviceId)
    }
}