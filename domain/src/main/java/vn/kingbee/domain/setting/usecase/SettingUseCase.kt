package vn.kingbee.domain.setting.usecase

import io.reactivex.Observable
import vn.kingbee.domain.setting.entity.configuration.ConfigInfo
import vn.kingbee.domain.setting.entity.kiosk.KioskDataResponse
import vn.kingbee.domain.setting.entity.lov.LOV

interface SettingUseCase {
    fun getLOVs(): Observable<LOV>

    fun getConfigInfo(): Observable<ConfigInfo>

    fun getKioskConfigInfo(deviceId: String): Observable<List<KioskDataResponse>>
}