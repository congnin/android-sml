package vn.kingbee.widget.camera

import android.graphics.Bitmap

interface CameraListener {
    fun onCaptured(bitmap: Bitmap)

    fun onCameraError()

    fun onCameraPreview()
}