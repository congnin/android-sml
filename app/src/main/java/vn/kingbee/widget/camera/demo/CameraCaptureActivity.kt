package vn.kingbee.widget.camera.demo

import android.graphics.Bitmap
import android.os.Bundle
import android.os.Handler
import android.view.SurfaceHolder
import android.view.SurfaceView
import android.view.View
import android.widget.ImageView
import vn.kingbee.widget.BaseActivity
import vn.kingbee.widget.R
import vn.kingbee.widget.camera.CameraListener
import vn.kingbee.widget.camera.helper.CameraHelper
import vn.kingbee.widget.camera.helper.CameraHelperFactory
import vn.kingbee.widget.camera.helper.KTP_SURFACE_HEIGHT
import vn.kingbee.widget.camera.helper.KTP_SURFACE_WIDTH
import vn.kingbee.widget.utils.ANDROID_WIDGET
import vn.kingbee.widget.utils.FileUtils
import android.graphics.drawable.AnimationDrawable
import vn.kingbee.widget.utils.PermissionUtils

class CameraCaptureActivity : BaseActivity(), CameraListener {

    private val DELAY_SHOW_MESSAGE_TIME = 1000
    val E_KTP_PATH = "e_ktp.jpg"

    private var imgPreviewAnim: ImageView? = null
    private var previewSurfaceView: SurfaceView? = null
    private var mCaptureButton: View? = null

    var cameraHelper: CameraHelper? = null

    private var mainThreadHandler: Handler? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_capture)

        addViews()
        addEvents()
    }

    private fun addViews() {
        imgPreviewAnim = findViewById(R.id.img_preview_anim)
        previewSurfaceView = findViewById(R.id.preview_surface)
        mCaptureButton = findViewById(R.id.capture_layout)
    }

    private fun addEvents() {
        if (PermissionUtils.isRuntimePermissionRequired()) {
            if
        }
        initEKTPView()
        initCameraView()

        mCaptureButton?.setOnClickListener {
            mainThreadHandler?.postDelayed({ cameraHelper?.capture() }, 1000)
        }
    }

    fun initCameraView() {
        previewSurfaceView?.holder?.addCallback(object : SurfaceHolder.Callback {
            override fun surfaceCreated(holder: SurfaceHolder?) {
                cameraHelper = CameraHelperFactory.getKTPInstance(
                    windowManager, this@CameraCaptureActivity,
                    previewSurfaceView!!, getImagePath(), this@CameraCaptureActivity
                )
                holder?.setFixedSize(KTP_SURFACE_WIDTH, KTP_SURFACE_HEIGHT)
                cameraHelper?.start()
            }

            override fun surfaceChanged(
                holder: SurfaceHolder?,
                format: Int,
                width: Int,
                height: Int
            ) {

            }

            override fun surfaceDestroyed(holder: SurfaceHolder?) {

            }
        })
    }

    fun initEKTPView() {
        // Animation background
        mainThreadHandler?.postDelayed(
            { previewSurfaceView?.visibility = View.VISIBLE },
            2000
        )
        imgPreviewAnim?.visibility = View.VISIBLE
        (imgPreviewAnim?.background as AnimationDrawable).start()
    }

    fun getImagePath(): String = FileUtils.concatDirectoryPath(ANDROID_WIDGET, E_KTP_PATH)!!

    override fun onCaptured(bitmap: Bitmap) {

    }

    override fun onCameraError() {

    }

    override fun onCameraPreview() {
        mainThreadHandler?.post(kotlin.run {
            Runnable {

            }
        })
    }

    override fun onStop() {
        super.onStop()
        release()
    }

    protected fun release() {
        if (cameraHelper != null) {
            cameraHelper?.release()
        }
        previewSurfaceView?.visibility = View.INVISIBLE
        mainThreadHandler?.removeCallbacksAndMessages(null)
    }
}