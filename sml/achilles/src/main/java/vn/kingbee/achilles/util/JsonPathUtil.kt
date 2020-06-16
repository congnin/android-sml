package vn.kingbee.achilles.util

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import org.json.JSONException
import org.json.JSONObject

object JsonPathUtil {

    fun createGson(): Gson {
        return GsonBuilder().create()
    }

    @Throws(JSONException::class)
    fun getJsonValue(path: String, jsonObject: JSONObject): String {
        var temporaryJsonObject = jsonObject
        val tokens = path.split("\\.".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        var result = ""

        for (i in tokens.indices) {
            if (i == tokens.size - 1) {
                result = temporaryJsonObject.getString(tokens[i])
            } else {
                temporaryJsonObject = temporaryJsonObject.getJSONObject(tokens[i])
            }
        }

        return result
    }
}