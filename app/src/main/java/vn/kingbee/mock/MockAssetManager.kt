package vn.kingbee.mock

import android.content.Context
import timber.log.Timber
import java.io.IOException
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

object MockAssetManager {
    const val JSON_EXT = ".json"
    private const val MOCK_API_FOLDER = "mock_api"
    private const val SLASH = "/"
    private const val HASH = "#"
    private val selectedOptions = HashMap<String, String>()
    private val mockOptions = HashMap<String, List<String>>()

    var isDataInit = false

    fun init(context: Context) {
        scanAssetFiles(context)
        isDataInit = true
    }

    private fun scanAssetFiles(context: Context): Map<String, List<String>>? {
        try {
            val assets = context.assets
            val functionList = assets.list(MOCK_API_FOLDER) ?: return null
            for (function in functionList) {
                if (!function.endsWith(JSON_EXT)) {
                    val subItems = assets.list(MOCK_API_FOLDER + SLASH + function)
                    if (subItems != null) {
                        val items = ArrayList(Arrays.asList(*subItems))
                        selectedOptions[function] = function + JSON_EXT
                        mockOptions[function] = items
                    }
                }
            }
        } catch (e: IOException) {
            Timber.d("Can get json mock list!!!")
        }
        return mockOptions
    }

    fun getMockOptions(): Map<String, Iterable<String>> {
        return mockOptions
    }

    fun setSelection(name: String, selection: String) {
        selectedOptions[name] = selection
        FullMockConfigData.applySelection(name, selection)
    }

    fun getSelection(name: String): String? {
        return selectedOptions[name]
    }

    fun getSelectionIndex(name: String): Int {
        val items = mockOptions[name]
        val selected = selectedOptions[name]
        if (items != null) {
            for (i in items.indices) {
                if (items[i] == selected)
                    return i
            }
        }
        return 0
    }

    fun extractHttpCodeFromFile(rawName: String): Int {
        if (rawName.contains(HASH)) {
            val fileName = rawName.substring(0, rawName.length - JSON_EXT.length)
            val nameSegments = fileName.split(HASH.toRegex())
                .dropLastWhile { it.isEmpty() }.toTypedArray()
            if (nameSegments.size > 1) {
                return Integer.parseInt(nameSegments[1])
            }
        }
        return 200
    }

    @Throws(IOException::class)
    fun openMockFile(context: Context, functionName: String, fileName: String)
            = context.assets.open(MOCK_API_FOLDER + SLASH + functionName + SLASH + fileName)

    fun applyFullMockDefault(configs: MutableList<MockConfig>) {
        configs.forEach { selectedOptions[it.responseBodyPath] = it.responseBodyFile!! }
    }
}