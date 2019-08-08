package vn.kingbee.application.runtime

import android.content.SharedPreferences
import com.google.gson.Gson
import vn.kingbee.domain.setting.entity.lov.LOV

class RuntimeImpl : Runtime, BaseRuntimeImpl {

    constructor(sharedPreferences: SharedPreferences, gson: Gson) : super(sharedPreferences, gson)

    override fun getLOV(): LOV {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun setLOV(lov: LOV) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}