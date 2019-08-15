package vn.kingbee.domain.setting.usecase

import io.reactivex.Observable
import vn.kingbee.domain.dataprocess.DataProcessingRepository
import vn.kingbee.domain.entity.configuration.ConfigInfo
import vn.kingbee.domain.entity.kiosk.KioskDataResponse
import vn.kingbee.domain.entity.lov.LOV
import vn.kingbee.domain.setting.repository.SettingRepository

class SettingUseCaseImpl : SettingUseCase {

    private var settingRepository: SettingRepository
    private var dataProcessingRepository: DataProcessingRepository

    constructor(settingRepository: SettingRepository,
                dataProcessingRepository: DataProcessingRepository) {
        this.settingRepository = settingRepository
        this.dataProcessingRepository = dataProcessingRepository
    }

    override fun getLOVs(): Observable<LOV> {
        return settingRepository.getLOVs(dataProcessingRepository.getMsisdn(),
            dataProcessingRepository.getAccessToken())
    }

    override fun getConfigInfo(): Observable<ConfigInfo> {
        return settingRepository.getConfigInfo()
    }

    override fun getKioskConfigInfo(deviceId: String): Observable<List<KioskDataResponse>> {
        return settingRepository.getKioskConfigInfo(deviceId)
    }
}