package vn.kingbee.application.runtime

import android.content.SharedPreferences
import android.text.TextUtils
import com.google.gson.Gson
import org.apache.commons.lang3.StringUtils
import org.json.JSONException
import org.json.JSONObject
import vn.kingbee.domain.dataprocess.Runtime
import vn.kingbee.domain.entity.base.KioskConfiguration
import vn.kingbee.domain.entity.configuration.ConfigInfo
import vn.kingbee.domain.entity.configuration.Configuration
import vn.kingbee.domain.entity.lov.LOV
import vn.kingbee.domain.entity.token.AccessTokenResponse
import vn.kingbee.utils.security.SecurityUtil
import java.util.HashMap

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

    override fun setConfiguration(configInfo: ConfigInfo) {
        if (!configInfo.configurations.isNullOrEmpty()) {
            val mapConfigData = convertToMap(configInfo.configurations!!)
            val jsonString = JSONObject(mapConfigData).toString()
            sharedPreferences.edit().putString(PREF_CONFIGURATION_DATA, jsonString).apply()
        }
    }

    override fun getConfiguration(): Map<String, String>? {
        val configurations = HashMap<String, String>()
        val configsString = sharedPreferences.getString(PREF_CONFIGURATION_DATA, "")
        if (StringUtils.isEmpty(configsString)) {
            return null
        }
        try {
            val jsonObject = JSONObject(configsString)
            val keysItr = jsonObject.keys()
            while (keysItr.hasNext()) {
                val key = keysItr.next()
                val value = jsonObject.get(key) as String
                configurations[key] = value
            }
        } catch (e: JSONException) {
            e.printStackTrace()
        }

        return configurations
    }

    override fun getConfigurationValue(key: String, defaultValue: String): String {
        val configurations = getConfiguration()
        try {
            val configValue = configurations!![key]
            return configValue ?: defaultValue
        } catch (ex: NullPointerException) {
            return StringUtils.EMPTY
        } catch (ex: ClassCastException) {
            return StringUtils.EMPTY
        }
    }

    private fun convertToMap(configurations: List<Configuration>): Map<String, String> {
        val map = HashMap<String, String>()
        for (configuration in configurations) {
            if (configuration.key != null && configuration.value != null) {
                map[configuration.key!!] = configuration.value!!
            }
        }
        return map
    }

    companion object {
        private val PREF_KIOSK_CONFIGURATION_DATA = SecurityUtil.sha256("PREF_KIOSK_CONFIGURATION_DATA")
        private val PREF_STAFF_INFO = SecurityUtil.sha256("PREF_STAFF_INFO")

        private val PREF_STAFF_ACCESS_TOKEN = SecurityUtil.sha256("PREF_STAFF_ACCESS_TOKEN")

        private val PREF_SELECT_ENVIRONMENT = SecurityUtil.sha256("PREF_SELECT_ENVIRONMENT")

        private val PREF_SELECT_SKIP_STORY = SecurityUtil.sha256("PREF_SELECT_SKIP_STORY")

        private val PREF_APP_TOKEN = SecurityUtil.sha256("PREF_APP_TOKEN")

        private val PREF_CONFIGURATION_DATA = SecurityUtil.sha256("PREF_CONFIGURATION_DATA")
    }
}