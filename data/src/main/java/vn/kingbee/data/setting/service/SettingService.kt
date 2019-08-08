package vn.kingbee.data.setting.service

import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Path
import vn.kingbee.domain.base.KioskResponse
import vn.kingbee.domain.setting.entity.configuration.ConfigInfo
import vn.kingbee.domain.setting.entity.kiosk.KioskConfigDataResponse
import vn.kingbee.domain.setting.entity.lov.LOV

interface SettingService {
    @GET("settings/lovs")
    fun getLOVs(): Observable<KioskResponse<LOV>>

    @GET("settings/configurations")
    fun getConfigInfo(): Observable<KioskResponse<ConfigInfo>>

    @GET("settings/kiosks/{deviceId}")
    fun getKioskConfig(@Path("deviceId") deviceId: String): Observable<KioskResponse<KioskConfigDataResponse>>
}