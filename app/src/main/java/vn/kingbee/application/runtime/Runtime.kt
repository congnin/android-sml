package vn.kingbee.application.runtime

import vn.kingbee.domain.entity.base.KioskConfiguration
import vn.kingbee.domain.entity.lov.LOV

interface Runtime {
    fun getLOV(): LOV

    fun setLOV(lov: LOV)

    fun getKioskConfiguration(): KioskConfiguration?

    fun setKioskConfiguration(kioskConfiguration: KioskConfiguration?)
}