package vn.kingbee.utils

import android.content.Context
import android.os.Environment
import android.util.Log
import com.google.gson.Gson
import io.reactivex.Observable
import timber.log.Timber
import vn.kingbee.application.MyApp
import vn.kingbee.widget.recyclerview.help.HelpVideoResponse
import java.io.*

class FileUtils {
    companion object {
        private val DIRECTORY_NAME = "android-widget"
        private val DIRECTORY_HELP_NAME = "/help/"
        private val FILE_HELP_VIDEO_NAME = "get_help_video.json"

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

        fun getHelpVideoFromResource(context: Context): Observable<HelpVideoResponse> {
            return Observable.fromCallable<HelpVideoResponse> {
                val rd = getInputStreamReader(context, DIRECTORY_HELP_NAME, FILE_HELP_VIDEO_NAME)
                Gson().fromJson(rd, HelpVideoResponse::class.java)
            }
        }

        @Throws(UnsupportedEncodingException::class)
        fun getInputStreamReader(context: Context, directory: String, fileName: String): Reader {
            val raw = getInputStream(context, directory, fileName)
            return BufferedReader(InputStreamReader(raw, "UTF-8"))
        }

        private fun getInputStream(context: Context, path: String, fileName: String): InputStream {
            var stream: InputStream? = null
            try {
                stream = getInputStreamFromLocal(path + fileName)
            } catch (e: Exception) {
                Timber.e(e, "EXCEPTION GET HELP INPUT STREAM LOCAL: " + e.message)
            }

            if (stream == null) {
                try {
                    stream = getInputStreamFromAssets(context, fileName)
                } catch (e: IOException) {
                    Timber.e(e, "EXCEPTION GET HELP INPUT STREAM ASSETS: " + e.message)
                }

            }
            return stream!!
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