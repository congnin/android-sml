package vn.kingbee.application.runtime

import vn.kingbee.domain.setting.entity.lov.LOV

interface Runtime {
    fun getLOV(): LOV

    fun setLOV(lov: LOV)
}