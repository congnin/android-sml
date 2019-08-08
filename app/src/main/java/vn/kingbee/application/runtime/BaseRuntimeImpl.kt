package vn.kingbee.application.runtime

import android.content.SharedPreferences
import android.text.TextUtils
import com.google.gson.Gson
import timber.log.Timber

open class BaseRuntimeImpl {
    lateinit var sharedPreferences: SharedPreferences

    lateinit var gson: Gson

    constructor(sharedPreferences: SharedPreferences, gson: Gson) {
        this.sharedPreferences = sharedPreferences
        this.gson = gson
    }

    fun <T> getPrefObject(cls: Class<T>, key: String, willCleanPrefIfError: Boolean): T? {
        val strValue = this.sharedPreferences.getString(key, "")
        if (!TextUtils.isEmpty(strValue)) {
            try {
                return this.gson.fromJson(strValue, cls)
            } catch (ex: Exception) {
                Timber.e("$ex BaseRuntimeImpl - getPrefObject error: $strValue")
                if (willCleanPrefIfError) {
                    this.sharedPreferences.edit().remove(key).apply()
                }
            }
        }
        return null
    }

    fun setPrefObject(key: String, obj: Any) {
        this.sharedPreferences.edit().putString(key, this.gson.toJson(obj)).apply()
    }
}