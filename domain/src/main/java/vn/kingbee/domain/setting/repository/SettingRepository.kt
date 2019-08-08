package vn.kingbee.domain.setting.repository

import io.reactivex.Observable
import vn.kingbee.domain.setting.entity.configuration.ConfigInfo
import vn.kingbee.domain.setting.entity.kiosk.KioskDataResponse
import vn.kingbee.domain.setting.entity.lov.LOV

interface SettingRepository {
    fun getLOVs(): Observable<LOV>

    fun getConfigInfo(): Observable<ConfigInfo>

    fun getKioskConfigInfo(deviceId: String):Observable<List<KioskDataResponse>>
}