package vn.kingbee.achilles.util

import com.google.gson.Gson
import kotlin.jvm.internal.Intrinsics

object GsonHelper {
    @JvmStatic
    fun jsonFromObject(gSon: Gson, prepareApi: Any): String {
        Intrinsics.checkParameterIsNotNull(gSon, "gSon")
        Intrinsics.checkParameterIsNotNull(prepareApi, "prepareApi")
        val var10000 = gSon.toJson(prepareApi)
        Intrinsics.checkExpressionValueIsNotNull(var10000, "gSon.toJson(prepareApi)")
        return var10000
    }

    @JvmStatic
    fun jsonToObject(gSon: Gson, jsonStr: String, clazz: Class<*>): Any {
        Intrinsics.checkParameterIsNotNull(gSon, "gSon")
        Intrinsics.checkParameterIsNotNull(jsonStr, "jsonStr")
        Intrinsics.checkParameterIsNotNull(clazz, "clazz")
        return gSon.fromJson<Any>(jsonStr, clazz)
    }
}