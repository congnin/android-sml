package vn.kingbee.utils

import android.graphics.Bitmap
import android.graphics.Matrix
import android.graphics.Rect
import android.os.Environment
import timber.log.Timber
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

const val ANDROID_WIDGET = "android-widget"

class ImageUtils {
    companion object {
        fun rotate(image: Bitmap, angle: Float): Bitmap {
            val matrix = Matrix()
            matrix.postRotate(angle)
            return Bitmap.createBitmap(image, 0, 0, image.width, image.height, matrix, true)
        }

        fun isSolidColor(target: Bitmap): Boolean {
            Timber.d("#ValidateImage #start validate image")
            val startTime = System.currentTimeMillis()
            var totalColor = 0.0
            for (i in 0 until target.width) {
                for (j in 0 until target.height) {
                    totalColor += target.getPixel(i, j)
                }
            }
            val averageColor = (totalColor / (target.height * target.width)).toInt()
            Timber.d("#ValidateImage  #duration to process image: " + (System.currentTimeMillis() - startTime))
            Timber.d(String.format("#ValidateImage #average color: %s", averageColor))
            val topLeftPixel = target.getPixel(0, 0)
            val topRightPixel = target.getPixel(target.width - 1, 0)
            val bottomRightPixel = target.getPixel(target.width - 1, target.height - 1)
            val bottomLeftPixel = target.getPixel(0, target.height - 1)
            val centerPixel = target.getPixel(target.width / 2, target.height / 2)
            val anchorAverage =
                (topLeftPixel + topRightPixel + bottomLeftPixel + bottomRightPixel + centerPixel) / 5
            Timber.d(String.format("#ValidateImage  #anchor color: %s", anchorAverage))
            val isSolid = averageColor == anchorAverage
            Timber.d(String.format("#ValidateImage  #isSolid %s", isSolid))
            return isSolid
        }

        fun saveImageToExternal(image: Bitmap, filename: String) {
            val root = Environment.getExternalStorageDirectory().absolutePath
            val directory = File(String.format("%s/%s", root, ANDROID_WIDGET))
            val file = File(directory, filename)
            val parent = File(file.parent)
            if (!parent.exists()) parent.mkdirs()

            var out: FileOutputStream? = null
            try {
                out = FileOutputStream(file)
                image.compress(Bitmap.CompressFormat.JPEG, 75, out)
                out.flush()
            } catch (e: Exception) {
                Timber.d("Cannot store USB camera images to disk " + filename + e.message)
            } finally {
                if (out != null) {
                    try {
                        out.close()
                    } catch (e: IOException) {

                    }
                }
            }
        }

        fun cropRectangle(originRect: Rect, scaleRatio: Float): Rect {
            if (scaleRatio > 0 && scaleRatio < 1) {
                val cropWidth = (originRect.right * scaleRatio).toInt()
                val cropHeight = cropWidth * 9 / 16
                val deltaWidth = (originRect.right * (1 - scaleRatio) / 2).toInt()
                val deltaHeight = (originRect.bottom - cropHeight) / 2
                Timber.d(String.format("Crop width: %s, height: %s", cropWidth, cropHeight))

                val left = originRect.left + deltaWidth
                val right = originRect.right - deltaWidth
                val top = originRect.top + deltaHeight
                val bottom = originRect.bottom - deltaHeight
                return Rect(left, top, right, bottom)
            }

            return originRect
        }
    }
}