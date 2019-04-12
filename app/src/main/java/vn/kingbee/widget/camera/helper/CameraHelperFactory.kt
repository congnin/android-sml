package vn.kingbee.widget.camera.helper

import android.content.Context
import android.view.SurfaceView
import android.view.WindowManager
import vn.kingbee.widget.camera.CameraListener

val KTP_ZOOM_LEVEL = 15
val KTP_SCALE_RATIO = 0.35f
val KTP_SURFACE_WIDTH = 1280
val KTP_SURFACE_HEIGHT = 720

class CameraHelperFactory {
    companion object {

        fun getKTPInstance(
            windowManager: WindowManager,
            context: Context,
            surfaceView: SurfaceView,
            imagePath: String,
            cameraListener: CameraListener
        ): CameraHelper {
            return getCameraHelperInstance(
                windowManager, context, surfaceView, imagePath,
                cameraListener, KTP_ZOOM_LEVEL, KTP_SCALE_RATIO,
                KTP_SURFACE_WIDTH, KTP_SURFACE_HEIGHT,
                KTP_SURFACE_WIDTH, KTP_SURFACE_HEIGHT
            )
        }

        private fun getCameraHelperInstance(
            windowManager: WindowManager, context: Context,
            surfaceView: SurfaceView, imagePath: String,
            cameraListener: CameraListener,
            zoomLevel: Int, scaleLevel: Float,
            surfaceWidth: Int, surfaceHeight: Int,
            imageWidth: Int, imageHeight: Int
        ): CameraHelper {
            return CameraHelperImpl(
                context, surfaceView, imagePath, cameraListener, scaleLevel,
                imageWidth, imageHeight
            )
        }
    }
}