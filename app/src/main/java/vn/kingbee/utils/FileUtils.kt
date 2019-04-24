package vn.kingbee.utils

import android.content.Context
import android.os.Environment
import android.util.Log
import com.google.gson.Gson
import io.reactivex.Observable
import timber.log.Timber
import vn.kingbee.application.MyApp
import vn.kingbee.model.ProvinceResponse
import vn.kingbee.widget.recyclerview.help.HelpVideoResponse
import java.io.*

class FileUtils {
    companion object {
        private val DIRECTORY_NAME = "android-widget"
        private val DIRECTORY_HELP_NAME = "/help/"
        private val FILE_HELP_VIDEO_NAME = "get_help_video.json"
        private val DIRECTORY_MOCK = "/mock/"
        private val FILE_PROVINCE_NAME = "province.json"

        fun concatDirectoryPath(parentDirectory: String, childDirectory: String): String? {
            if (parentDirectory.isEmpty() && childDirectory.isEmpty()) {
                return null
            } else if (parentDirectory.isEmpty()) {
                return childDirectory
            } else if (childDirectory.isEmpty()) {
                return parentDirectory
            }

            return File(parentDirectory, childDirectory).path
        }

        fun getDownloadLocalPath(path: String): String {
            val downloadDirectory =
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                    .absolutePath
            return String.format("%s/%s", downloadDirectory, path)
        }

        fun getProvinceFromResource(context: Context): Observable<ProvinceResponse> {
            return Observable.fromCallable<ProvinceResponse> {
                val rd = getInputStreamReaderFromAssets(context, FILE_PROVINCE_NAME)
                Gson().fromJson(rd, ProvinceResponse::class.java)
            }
        }

        fun getHelpVideoFromResource(context: Context): Observable<HelpVideoResponse> {
            return Observable.fromCallable<HelpVideoResponse> {
                val rd = getInputStreamReaderFromAssets(context, FILE_HELP_VIDEO_NAME)
                Gson().fromJson(rd, HelpVideoResponse::class.java)
            }
        }

        @Throws(UnsupportedEncodingException::class)
        fun getInputStreamReaderFromLocal(context: Context, directory: String, fileName: String): Reader {
            val raw = getInputStreamFromLocal(directory + fileName)
            return BufferedReader(InputStreamReader(raw, "UTF-8"))
        }

        @Throws(UnsupportedEncodingException::class)
        fun getInputStreamReaderFromAssets(context: Context, fileName: String): Reader {
            val raw = getInputStreamFromAssets(context, fileName)
            return BufferedReader(InputStreamReader(raw, "UTF-8"))
        }

        @Throws(IOException::class)
        private fun getInputStreamFromLocal(filePath: String): InputStream {
            val context = MyApp.getInstance().applicationContext
            val file = File(checkDirectory(context).path + filePath)
            return BufferedInputStream(FileInputStream(file))
        }

        @Throws(IOException::class)
        private fun getInputStreamFromAssets(context: Context, filePath: String): InputStream? {
            val manager = context.assets
            return manager?.open(filePath)
        }

        fun checkDirectory(ctx: Context): File {
            return checkDirectory(ctx, DIRECTORY_NAME)
        }

        fun checkDirectory(ctx: Context, directoryName: String): File {
            val dirFile: File
            if (checkSDcard()) {
                dirFile = File(Environment.getExternalStorageDirectory(), directoryName)
            } else {
                dirFile = File(ctx.cacheDir, directoryName)
            }
            if (!dirFile.exists()) {
                dirFile.mkdirs()
                Log.d("DIRECTORY", "CREATE DIRECTORY: " + dirFile.path)
            }
            return dirFile
        }

        private fun checkSDcard(): Boolean {
            val mExternalStorageWriteable: Boolean
            val state = Environment.getExternalStorageState()
            if (Environment.MEDIA_MOUNTED == state) {
                mExternalStorageWriteable = true
            } else if (Environment.MEDIA_MOUNTED_READ_ONLY == state) {
                mExternalStorageWriteable = false
            } else {
                mExternalStorageWriteable = false
            }
            return mExternalStorageWriteable
        }
    }
}