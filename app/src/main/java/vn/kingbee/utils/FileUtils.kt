package vn.kingbee.utils

import android.content.Context
import android.graphics.Bitmap
import android.os.Environment
import com.google.gson.Gson
import io.reactivex.Observable
import timber.log.Timber
import vn.kingbee.application.MyApp
import vn.kingbee.domain.entity.base.KioskConfiguration
import vn.kingbee.ektpreader.EKTPReaderRepositoryMock
import vn.kingbee.model.ProvinceResponse
import vn.kingbee.widget.dialog.ext.help.model.HelpResponse
import vn.kingbee.widget.recyclerview.help.HelpVideoResponse
import java.io.*

object FileUtils {
    private const val DIRECTORY_NAME = "android-widget"
    private const val DIRECTORY_HELP_NAME = "/help/"
    private const val FILE_HELP_FAQ_NAME = "get_help_faq.json"
    private const val FILE_HELP_VIDEO_NAME = "get_help_video.json"
    private const val DIRECTORY_MOCK = "/mock/"
    private const val FILE_PROVINCE_NAME = "province.json"
    private const val FILE_MOCK_EKTP = "mock_ektp.json"
    private const val FILE_ENV_CONFIGURATION = "sit_configurations.json"

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

    fun getHelpFaqFromResource(context: Context): Observable<HelpResponse> {
        return Observable.fromCallable<HelpResponse> {
            val rd = getInputStreamReaderFromAssets(context, FILE_HELP_FAQ_NAME)
            Gson().fromJson(rd, HelpResponse::class.java)
        }
    }

    fun getMockEKTPFromResource(context: Context): Observable<EKTPReaderRepositoryMock.MockEktp> {
        return Observable.fromCallable {
            val rd = getInputStreamReaderFromAssets(context, FILE_MOCK_EKTP)
            Gson().fromJson(rd, EKTPReaderRepositoryMock.MockEktp::class.java)
        }
    }

    fun getEnvConfigurationFromResource(context: Context) : Observable<KioskConfiguration> {
        return Observable.fromCallable {
            val rd = getInputStreamReaderFromAssets(context, FILE_ENV_CONFIGURATION)
            Gson().fromJson(rd, KioskConfiguration::class.java)
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
            Timber.d("CREATE DIRECTORY: %s", dirFile.path)
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

    fun writeBitmapToFile(
        viewBoundariesImage: Bitmap,
        fileName: String,
        fileExtension: String): String? {
        var out: FileOutputStream? = null
        var fileDebug: String? = null
        try {
            fileDebug = getCacheFilePath(fileName, fileExtension)
            val file = File(fileDebug)
            if (file.exists()) {
                file.delete()
            }
            out = FileOutputStream(fileDebug)
            viewBoundariesImage.compress(
                Bitmap.CompressFormat.JPEG,
                10,
                out
            ) // bmp is your Bitmap instance
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            try {
                out?.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }

        }
        return fileDebug
    }

    fun getCacheFilePath(fileName: String, fileExtension: String): String {
        val file = File(checkDirectory(), "$fileName.$fileExtension")
        val parentFile = file.parentFile
        if (!parentFile.exists()) {
            parentFile.mkdirs()
        }
        return file.path
    }

    fun checkDirectory(): File? {
        if (checkSDcard()) {
            val dirFile = File(
                Environment.getExternalStorageDirectory(),
                DIRECTORY_NAME
            )
            dirFile.mkdirs()
            return dirFile
        }
        return null
    }
}