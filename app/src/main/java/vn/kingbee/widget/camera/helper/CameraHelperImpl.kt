package vn.kingbee.widget.camera.helper

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.BitmapFactory
import android.graphics.Rect
import android.os.HandlerThread
import android.media.ImageReader
import android.os.Handler
import android.os.Looper
import vn.kingbee.widget.camera.CameraListener
import android.view.SurfaceView
import timber.log.Timber
import vn.kingbee.utils.ImageUtils
import android.hardware.camera2.CaptureResult
import android.hardware.camera2.CaptureRequest
import android.hardware.camera2.CameraCaptureSession
import android.hardware.camera2.CameraAccessException
import android.hardware.camera2.CameraDevice
import java.util.*
import android.hardware.camera2.CameraManager
import android.graphics.ImageFormat
import android.hardware.camera2.CameraCharacteristics
import android.hardware.camera2.CameraMetadata
import android.hardware.camera2.TotalCaptureResult
import io.reactivex.Observable
import io.reactivex.ObservableOnSubscribe
import io.reactivex.schedulers.Schedulers

class CameraHelperImpl(
    context: Context,
    previewSurface: SurfaceView,
    storeImagePath: String,
    listener: CameraListener,
    scaleRatio: Float,
    previewWidth: Int,
    previewHeight: Int
) : CameraHelper {
    private val MAXIMUM_TIMES_TRY_LOCK_FOCUS = 100

    private val STATE_PREVIEW = 0
    private val STATE_WAITING_LOCK = 1
    private val STATE_WAITING_PRECAPTURE = 2
    private val STATE_WAITING_NON_PRECAPTURE = 3
    private val STATE_PICTURE_TAKEN = 4

    private var mPreviewSurface: SurfaceView? = previewSurface
    private var mCameraListener: CameraListener? = listener
    private var mCameraDevice: CameraDevice? = null
    private var mContext: Context? = context
    private var mStoreImagePath: String? = storeImagePath
    private var mPreviewRequestBuilder: CaptureRequest.Builder? = null
    private var mCaptureSession: CameraCaptureSession? = null
    private var mPreviewRequest: CaptureRequest? = null
    private var mBackgroundThread: HandlerThread? = null
    private var mBackgroundHandler: Handler? = null
    private var mImageReader: ImageReader? = null
    private var mCameraId: String? = null
    private var mState = STATE_PREVIEW
    private var mPreviewRect: Rect? = null
    private var mScaleRatio: Float = scaleRatio

    private var mPreviewWidth: Int = previewWidth
    private var mPreviewHeight: Int = previewHeight

    private val mOnImageAvailableListener = ImageReader.OnImageAvailableListener {
        mImageReader = it
        val image = it.acquireLatestImage()
        mBackgroundHandler?.post(kotlin.run {
            Runnable {
                Timber.d("onImageAvailable save image to storage")
                val buffer = image.planes[0].buffer
                val bytes = ByteArray(buffer.capacity())
                buffer.get(bytes)
                val bitmapImage = BitmapFactory.decodeByteArray(bytes, 0, bytes.size, null)
                val flipBitmap = ImageUtils.rotate(bitmapImage, 180F)
                ImageUtils.saveImageToExternal(flipBitmap, mStoreImagePath!!)
                Handler(Looper.getMainLooper()).postDelayed(
                    kotlin.run {
                        Runnable {
                            if (mCameraListener != null) {
                                mCameraListener?.onCaptured(flipBitmap)
                            }
                        }
                    }, 1000
                )

            }
        })
        Timber.d("onImageAvailable")
    }

    private val mStateCallback = object : CameraDevice.StateCallback() {
        override fun onOpened(cameraDevice: CameraDevice) {
            // This method is called when the camera is opened.  We start camera preview here.
            Timber.d("CameraHelperImpl #StateCallback #onOpened")
            mCameraDevice = cameraDevice
            if (mCameraListener != null) {
                mCameraListener?.onCameraPreview()
            }
            createCameraPreviewSession()
        }

        override fun onDisconnected(cameraDevice: CameraDevice) {
            Timber.d("CameraHelperImpl #StateCallback #onDisconnected")
            cameraDevice.close()
            mCameraDevice = null
        }

        override fun onError(cameraDevice: CameraDevice, error: Int) {
            Timber.d("CameraHelperImpl #StateCallback #onCameraError #ErrorCode $error")
            cameraDevice.close()
            mCameraDevice = null
            dispatchCameraErrorEvent()
        }
    }

    private val mCaptureCallback = object : CameraCaptureSession.CaptureCallback() {
        private var countLockFocus = 0

        private fun process(result: CaptureResult) {
            when (mState) {
                STATE_PREVIEW -> {
                    countLockFocus = 0
                }
                STATE_WAITING_LOCK -> {
                    val afState = result.get(CaptureResult.CONTROL_AF_STATE)
                    Timber.d("CameraHelperImpl #CaptureCallback #STATE_WAITING_LOCK: %s", afState!!)
                    if (CaptureResult.CONTROL_AF_STATE_FOCUSED_LOCKED == afState ||
                        CaptureResult.CONTROL_AF_STATE_NOT_FOCUSED_LOCKED == afState ||
                        countLockFocus >= MAXIMUM_TIMES_TRY_LOCK_FOCUS
                    ) {
                        countLockFocus = 0
                        // CONTROL_AE_STATE can be null on some devices
                        val aeState = result.get(CaptureResult.CONTROL_AE_STATE)
                        if (aeState == null || aeState == CaptureResult.CONTROL_AE_STATE_CONVERGED) {
                            mState = STATE_PICTURE_TAKEN
                            captureStillPicture()
                        } else {
                            runPrecaptureSequence()
                        }
                    } else if (CaptureResult.CONTROL_AF_STATE_PASSIVE_FOCUSED == afState) {
                        countLockFocus++
                    }
                }
                STATE_WAITING_PRECAPTURE -> {
                    // CONTROL_AE_STATE can be null on some devices
                    Timber.d("CameraHelperImpl #CaptureCallback #STATE_WAITING_PRECAPTURE")
                    val aeState = result.get(CaptureResult.CONTROL_AE_STATE)
                    if (aeState == null ||
                        aeState == CaptureResult.CONTROL_AE_STATE_PRECAPTURE ||
                        aeState == CaptureRequest.CONTROL_AE_STATE_FLASH_REQUIRED
                    ) {
                        mState = STATE_WAITING_NON_PRECAPTURE
                    }
                }
                STATE_WAITING_NON_PRECAPTURE -> {
                    // CONTROL_AE_STATE can be null on some devices
                    Timber.d("CameraHelperImpl #CaptureCallback #STATE_WAITING_NON_PRECAPTURE")
                    val aeState = result.get(CaptureResult.CONTROL_AE_STATE)
                    if (aeState == null || aeState != CaptureResult.CONTROL_AE_STATE_PRECAPTURE) {
                        mState = STATE_PICTURE_TAKEN
                        captureStillPicture()
                    }
                }
                else -> {
                    Timber.d("CameraHelperImpl #CaptureCallback #other")
                }
            }
        }

        override fun onCaptureProgressed(
            session: CameraCaptureSession,
            request: CaptureRequest,
            partialResult: CaptureResult
        ) {
            process(partialResult)
        }

        override fun onCaptureCompleted(
            session: CameraCaptureSession,
            request: CaptureRequest,
            result: TotalCaptureResult
        ) {
            process(result)
        }
    }

    private fun createCameraPreviewSession() {
        try {
            // We set up a CaptureRequest.Builder with the output Surface.
            mPreviewRequestBuilder =
                mCameraDevice?.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW)
            val surface = mPreviewSurface?.holder!!.surface
            mPreviewRequestBuilder?.addTarget(surface)
            // Here, we create a CameraCaptureSession for camera preview.
            mCameraDevice?.createCaptureSession(
                Arrays.asList(surface, mImageReader?.surface),
                object : CameraCaptureSession.StateCallback() {

                    override fun onConfigured(cameraCaptureSession: CameraCaptureSession) {
                        // The camera is already closed
                        if (null == mCameraDevice) {
                            return
                        }

                        // When the session is ready, we start displaying the preview.
                        mCaptureSession = cameraCaptureSession
                        try {
                            // Auto focus should be continuous for camera preview.
                            mPreviewRequestBuilder?.set(
                                CaptureRequest.CONTROL_AF_MODE,
                                CaptureRequest.CONTROL_AF_MODE_CONTINUOUS_PICTURE
                            )

                            mPreviewRequestBuilder?.set(
                                CaptureRequest.FLASH_MODE,
                                CaptureRequest.FLASH_MODE_OFF
                            )
                            mPreviewRequestBuilder?.set(
                                CaptureRequest.SCALER_CROP_REGION,
                                mPreviewRect
                            )

                            // Finally, we start displaying the camera preview.
                            mPreviewRequest = mPreviewRequestBuilder?.build()
                            mCaptureSession?.setRepeatingRequest(
                                mPreviewRequest!!,
                                mCaptureCallback, mBackgroundHandler
                            )
                        } catch (e: CameraAccessException) {
                            e.printStackTrace()
                        }

                    }

                    override fun onConfigureFailed(cameraCaptureSession: CameraCaptureSession) {
                        Timber.d("CameraHelperImpl #CameraCaptureSession.StateCallback #onConfigureFailed")
                    }
                }, null
            )
        } catch (e: CameraAccessException) {
            Timber.d("CameraHelperImpl #createCameraPreviewSession #CameraAccessException #%s", e.message)
            dispatchCameraErrorEvent()
        } catch (e: NullPointerException) {
            Timber.d("CameraHelperImpl #createCameraPreviewSession #NullPointerException")
        }
    }

    private fun startBackgroundThread() {
        mBackgroundThread = HandlerThread("CameraBackground")
        try {
            mBackgroundThread?.start()
            mBackgroundHandler = Handler(mBackgroundThread?.looper)
        } catch (exception: IllegalStateException) {
            Timber.d("CameraHelperImpl #startBackgroundThread #CaptureCallback #%s", exception.message)
        }
    }

    private fun stopBackgroundThread() {
        if (mBackgroundThread != null) {
            mBackgroundThread?.quitSafely()
            try {
                mBackgroundThread?.join()
                mBackgroundThread = null
                mBackgroundHandler = null
            } catch (e: InterruptedException) {
                Timber.d("CameraHelperImpl #stopBackgroundThread #CaptureCallback #%s", e.message)
            }
        }
    }

    @SuppressLint("MissingPermission")
    private fun openCamera(scaleRatio: Float) {
        setUpCameraOutputs(scaleRatio)
        val manager = mContext?.getSystemService(Context.CAMERA_SERVICE) as CameraManager
        try {
            manager.openCamera(mCameraId!!, mStateCallback, mBackgroundHandler)
        } catch (e: CameraAccessException) {
            Timber.d("CameraHelperImpl #openCamera #CameraAccessException")
            dispatchCameraErrorEvent()
        } catch (e: NullPointerException) {
            dispatchCameraErrorEvent()
            Timber.d("CameraHelperImpl #openCamera #NullPointerException")
        } catch (e: IllegalArgumentException) {
            Timber.d("CameraHelperImpl #openCamera #IllegalArgumentException")
            dispatchCameraErrorEvent()
        }
    }

    private fun runPrecaptureSequence() {
        try {
            // This is how to tell the camera to trigger.
            mPreviewRequestBuilder?.set(
                CaptureRequest.CONTROL_AE_PRECAPTURE_TRIGGER,
                CaptureRequest.CONTROL_AE_PRECAPTURE_TRIGGER_START
            )
            // Tell #mCaptureCallback to wait for the precapture sequence to be set.
            mState = STATE_WAITING_PRECAPTURE
            mCaptureSession?.capture(
                mPreviewRequestBuilder!!.build(), mCaptureCallback,
                mBackgroundHandler
            )
        } catch (e: CameraAccessException) {
            Timber.d("CameraHelperImpl #runPrecaptureSequence #CameraAccessException")
        }
    }

    private fun closeCamera() {
        if (null != mCaptureSession) {
            mCaptureSession?.close()
            mCaptureSession = null
        }
        if (null != mCameraDevice) {
            mCameraDevice?.close()
            mCameraDevice = null
        }
        if (null != mImageReader) {
            mImageReader?.close()
            mImageReader = null
        }
    }

    private fun setUpCameraOutputs(scaleRatio: Float) {
        val manager = mContext?.getSystemService(Context.CAMERA_SERVICE) as CameraManager
        try {
            for (cameraId in manager.cameraIdList) {
                val characteristics = manager.getCameraCharacteristics(cameraId)

                // We don't use a front facing camera in this sample.
                val facing = characteristics.get(CameraCharacteristics.LENS_FACING)
                if (facing != null && facing == CameraCharacteristics.LENS_FACING_FRONT) {
                    continue
                }

                val cameraSensorRect =
                    characteristics.get(CameraCharacteristics.SENSOR_INFO_ACTIVE_ARRAY_SIZE)
                Timber.d(
                    String.format(
                        "#CameraHelperImpl #CameraSensor left: %s, top: %s, right: %s, bottom: %s ",
                        cameraSensorRect!!.left,
                        cameraSensorRect.top,
                        cameraSensorRect.right,
                        cameraSensorRect.bottom
                    )
                )

                mPreviewRect = ImageUtils.cropRectangle(cameraSensorRect, scaleRatio)

                Timber.d(
                    String.format(
                        "#CameraHelperImpl previewRect left: %s, top: %s, right: %s, bottom: %s ",
                        mPreviewRect?.left,
                        mPreviewRect?.top,
                        mPreviewRect?.right,
                        mPreviewRect?.bottom
                    )
                )

                mImageReader = ImageReader.newInstance(
                    mPreviewWidth, mPreviewHeight,
                    ImageFormat.JPEG, /*maxImages*/1
                )
                mImageReader!!.setOnImageAvailableListener(
                    mOnImageAvailableListener, mBackgroundHandler
                )
                mCameraId = cameraId
                return
            }
        } catch (e: CameraAccessException) {
            dispatchCameraErrorEvent()
            Timber.d("CameraHelperImpl #setUpCameraOutputs #CameraAccessException")
        } catch (e: NullPointerException) {
            dispatchCameraErrorEvent()
            Timber.d("CameraHelperImpl #setUpCameraOutputs #CameraAccessException #NullPointerException")
        } catch (e: IllegalArgumentException) {
            dispatchCameraErrorEvent()
            Timber.d("CameraHelperImpl #setUpCameraOutputs #IllegalArgumentException")
        }
    }

    private fun captureStillPicture() {
        try {
            // This is the CaptureRequest.Builder that we use to take a picture.
            val captureBuilder =
                mCameraDevice?.createCaptureRequest(CameraDevice.TEMPLATE_STILL_CAPTURE)
            captureBuilder!!.set(CaptureRequest.CONTROL_MODE, CaptureRequest.CONTROL_MODE_AUTO)
            captureBuilder.set(CaptureRequest.CONTROL_AE_MODE, CaptureRequest.CONTROL_AE_MODE_ON)
            captureBuilder.addTarget(mImageReader!!.surface)

            // Use the same AE and AF modes as the preview.
            captureBuilder.set(
                CaptureRequest.CONTROL_AF_MODE,
                CaptureRequest.CONTROL_AF_MODE_CONTINUOUS_PICTURE
            )

            // Orientation
            captureBuilder.set(CaptureRequest.SCALER_CROP_REGION, mPreviewRect)

            val captureCallback = object : CameraCaptureSession.CaptureCallback() {

                override fun onCaptureCompleted(
                    session: CameraCaptureSession,
                    request: CaptureRequest,
                    result: TotalCaptureResult
                ) {
                    Timber.d("#captureStillPicture #onCaptureCompleted")
                    // Reset the auto-focus trigger
                    mPreviewRequestBuilder?.set(
                        CaptureRequest.CONTROL_AF_TRIGGER,
                        CameraMetadata.CONTROL_AF_TRIGGER_CANCEL
                    )
                    try {
                        mCaptureSession?.capture(
                            mPreviewRequestBuilder!!.build(),
                            null, null
                        )
                    } catch (e: CameraAccessException) {
                        Timber.d("#captureStillPicture #CameraAccessException")
                    } catch (e: NullPointerException) {
                        Timber.d("#captureStillPicture #NullPointerException")
                    } catch (e: Exception) {
                        Timber.d("#captureStillPicture Capture failure")
                    }

                }
            }

            mCaptureSession?.stopRepeating()
            mCaptureSession?.abortCaptures()
            mCaptureSession?.capture(captureBuilder.build(), captureCallback, null)
        } catch (e: CameraAccessException) {
            Timber.d("CameraHelperImpl #CaptureStillPicture #CameraAccessException #%s", e.message)
        }
    }

    private fun dispatchCameraErrorEvent() {
        if (mCameraListener != null) {
            mCameraListener?.onCameraError()
        }
    }

    override fun start() {
        openCamera(mScaleRatio)
        startBackgroundThread()
    }

    override fun capture() {
        try {
            // This is how to tell the camera to lock focus.
            mPreviewRequestBuilder?.set(
                CaptureRequest.CONTROL_AF_TRIGGER,
                CameraMetadata.CONTROL_AF_TRIGGER_START
            )
            // Tell #mCaptureCallback to wait for the lock.
            mState = STATE_WAITING_LOCK
            mCaptureSession?.capture(
                mPreviewRequestBuilder!!.build(), mCaptureCallback,
                mBackgroundHandler
            )
        } catch (e: CameraAccessException) {
            dispatchCameraErrorEvent()
            Timber.d("CameraHelperImpl #CameraAccessException #%s", e.message)
        } catch (e: IllegalStateException) {
            dispatchCameraErrorEvent()
            Timber.d("CameraHelperImpl #IllegalStateException #%s", e.message)
        } catch (e: IllegalArgumentException) {
            dispatchCameraErrorEvent()
            Timber.d("CameraHelperImpl #IllegalArgumentException #%s", e.message)
        }
    }

    @SuppressLint("CheckResult")
    override fun release() {
        Observable.create(ObservableOnSubscribe<Boolean> { emitter ->
            try {
                mCameraListener = null
                closeCamera()
                stopBackgroundThread()
                emitter.onNext(true)
                emitter.onComplete()
            } catch (error: Exception) {
                emitter.onError(error)
            }
        }).subscribeOn(Schedulers.io())
            .subscribe(
                {
                    Timber.d("CameraHelperImpl Camera release successfully")
                }, {
                    Timber.d("CameraHelperImpl Camera release failure")
                }
            )

    }
}