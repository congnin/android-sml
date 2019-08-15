package vn.kingbee.data.setting.service

import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import vn.kingbee.domain.entity.base.HeaderBuilder
import vn.kingbee.domain.entity.base.KioskResponse
import vn.kingbee.domain.entity.configuration.ConfigInfo
import vn.kingbee.domain.entity.kiosk.KioskConfigDataResponse
import vn.kingbee.domain.entity.lov.LOV

interface SettingService {
    @GET("settings/lovs")
    fun getLOVs(@Header(HeaderBuilder.MSISDN_HEADER_KEY) msisdn: String?,
                @Header(HeaderBuilder.AUTHORIZATION_KEY) authorization: String?)
            : Observable<KioskResponse<LOV>>

    @GET("settings/configurations")
    fun getConfigInfo(): Observable<KioskResponse<ConfigInfo>>

    @GET("settings/kiosks/{deviceId}")
    fun getKioskConfig(@Path("deviceId") deviceId: String): Observable<KioskResponse<KioskConfigDataResponse>>
}