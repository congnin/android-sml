package vn.kingbee.achilles.repository

import android.content.Context
import timber.log.Timber
import vn.kingbee.achilles.model.MockApiConfiguration
import vn.kingbee.achilles.util.Utils
import java.io.IOException
import java.util.*

class AssetRepository(context: Context, configuration: MockApiConfiguration)
    : ContentRepositoryImpl(context, configuration) {

    protected override val tag: String
        get() = this.javaClass.simpleName

    override val scenarioList: Array<String>
        get() {
            return try {
                val list = this.context.assets.list(this.mockApiConfiguration.scenarioPath!!)
                Arrays.sort(list, this.alphabeticallySortOrder)
                list
            } catch (ex: IOException) {
                Timber.tag(this.tag).e(ex)
                emptyArray()
            }
        }

    override fun getFileNameFromPrefix(context: Context, filePath: String, prefixFileName: String): String {
        return Utils.getAssetFileNameFromPrefix(context, filePath, prefixFileName)
    }

    override fun readContent(context: Context, assertUrl: String): String {
        return Utils.readAsset(context, assertUrl)
    }

    override fun getAllListResultFilePath(fileName: String): Array<String>? {
        try {
            val list = this.context.assets.list(this.mockApiConfiguration.apiPath + "/" + fileName)
            Arrays.sort(list, this.alphabeticallySortOrder)
            return list
        } catch (ex: IOException) {
            Timber.tag(this.tag).e(ex)
            return null
        }

    }
}