package vn.kingbee.domain.dataprocess

import vn.kingbee.domain.entity.base.KioskConfiguration
import vn.kingbee.domain.entity.configuration.ConfigInfo
import vn.kingbee.domain.entity.lov.LOV
import vn.kingbee.domain.entity.token.AccessTokenResponse

interface Runtime {
    fun getLOV(): LOV

    fun setLOV(lov: LOV)

    fun getKioskConfiguration(): KioskConfiguration?

    fun setKioskConfiguration(kioskConfiguration: KioskConfiguration?)

    fun getAppToken(): AccessTokenResponse?

    fun setAppToken(accessTokenResponse: AccessTokenResponse?)

    fun setConfiguration(configInfo: ConfigInfo)

    fun getConfiguration(): Map<String, String>?

    fun getConfigurationValue(key: String, defaultValue: String): String
}