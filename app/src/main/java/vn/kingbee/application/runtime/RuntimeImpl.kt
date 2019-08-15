package vn.kingbee.application.runtime

import android.content.SharedPreferences
import android.text.TextUtils
import com.google.gson.Gson
import vn.kingbee.domain.dataprocess.Runtime
import vn.kingbee.domain.entity.base.KioskConfiguration
import vn.kingbee.domain.entity.lov.LOV
import vn.kingbee.domain.entity.token.AccessTokenResponse
import vn.kingbee.utils.security.SecurityUtil

class RuntimeImpl : Runtime, BaseRuntimeImpl {

    constructor(sharedPreferences: SharedPreferences, gson: Gson) : super(sharedPreferences, gson)

    override fun getLOV(): LOV {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun setLOV(lov: LOV) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getKioskConfiguration(): KioskConfiguration? {
        val data = sharedPreferences.getString(PREF_KIOSK_CONFIGURATION_DATA, "")
        if (TextUtils.isEmpty(data)) {
            return null
        }
        return gson.fromJson(data, KioskConfiguration::class.java)
    }

    override fun setKioskConfiguration(kioskConfiguration: KioskConfiguration?) {
        if (kioskConfiguration != null) {
            val editor = sharedPreferences.edit()
            val data = gson.toJson(kioskConfiguration)
            editor.putString(PREF_KIOSK_CONFIGURATION_DATA, data)
            editor.apply()
        }
    }

    override fun getAppToken(): AccessTokenResponse? {
        val jsonAppToken = sharedPreferences.getString(PREF_APP_TOKEN, "")
        return if (TextUtils.isEmpty(jsonAppToken)) {
            null
        } else gson.fromJson(jsonAppToken, AccessTokenResponse::class.java)
    }

    override fun setAppToken(accessTokenResponse: AccessTokenResponse?) {
        val tokenEncode = "Bearer " + accessTokenResponse?.accessToken
        accessTokenResponse?.accessToken = tokenEncode
        val data = gson.toJson(accessTokenResponse)
        sharedPreferences.edit().putString(PREF_APP_TOKEN, data).apply()
    }

    companion object {
        private val PREF_KIOSK_CONFIGURATION_DATA = SecurityUtil.sha256("PREF_KIOSK_CONFIGURATION_DATA")
        private val PREF_STAFF_INFO = SecurityUtil.sha256("PREF_STAFF_INFO")

        private val PREF_STAFF_ACCESS_TOKEN = SecurityUtil.sha256("PREF_STAFF_ACCESS_TOKEN")

        private val PREF_SELECT_ENVIRONMENT = SecurityUtil.sha256("PREF_SELECT_ENVIRONMENT")

        private val PREF_SELECT_SKIP_STORY = SecurityUtil.sha256("PREF_SELECT_SKIP_STORY")

        private val PREF_APP_TOKEN = SecurityUtil.sha256("PREF_APP_TOKEN")
    }
}