package vn.kingbee.achilles.repository

import android.content.Context
import timber.log.Timber
import vn.kingbee.achilles.model.MockApiConfiguration
import vn.kingbee.achilles.util.Utils
import java.io.File
import java.util.*

class ExternalStorageRepository(context: Context, configuration: MockApiConfiguration, private val mSdCardDirectory: String) : ContentRepositoryImpl(context, configuration) {

    protected override val tag: String
        get() = this.javaClass.simpleName

    override val scenarioList: Array<String>
        get() {
            val list = File(String.format("%s/%s", this.mSdCardDirectory, this.mockApiConfiguration.scenarioPath)).list()
            Arrays.sort(list!!, this.alphabeticallySortOrder)
            return list
        }

    protected override fun getFileNameFromPrefix(context: Context, filePath: String, prefixFileName: String): String {
        return Utils.getSdCardFileNameFromPrefix(String.format("%s/%s", this.mSdCardDirectory, filePath), prefixFileName)
    }

    protected override fun readContent(context: Context, assertUrl: String): String {
        val url = String.format("%s/%s", this.mSdCardDirectory, assertUrl)
        return Utils.readSdCardFile(url)
    }

    override fun getAllListResultFilePath(fileName: String): Array<String>? {
        return try {
            val folderApi = File(String.format("%s/%s/%s", this.mSdCardDirectory, this.mockApiConfiguration.apiPath, fileName))
            val list = folderApi.list { file, name -> name.startsWith(fileName) && name.endsWith(".json") }
            Arrays.sort(list!!, this.alphabeticallySortOrder)
            list
        } catch (ex: Exception) {
            Timber.tag(this.tag).e(ex.message, *arrayOfNulls(0))
            null
        }

    }
}