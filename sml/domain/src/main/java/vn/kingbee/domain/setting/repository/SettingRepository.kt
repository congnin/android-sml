package vn.kingbee.domain.setting.repository

import io.reactivex.Observable
import vn.kingbee.domain.entity.configuration.ConfigInfo
import vn.kingbee.domain.entity.kiosk.KioskDataResponse
import vn.kingbee.domain.entity.lov.LOV

interface SettingRepository {
    fun getLOVs(msisdn: String?, authorization: String?): Observable<LOV>

    fun getConfigInfo(msisdn: String?, authorization: String?): Observable<ConfigInfo>

    fun getKioskConfigInfo(msisdn: String?,
                           authorization: String?):Observable<List<KioskDataResponse>>
}