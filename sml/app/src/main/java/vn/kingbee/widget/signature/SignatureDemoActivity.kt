package vn.kingbee.widget.signature

import android.os.Bundle
import android.widget.TextView
import vn.kingbee.utils.FileUtils
import vn.kingbee.widget.BaseActivity
import vn.kingbee.widget.R
import vn.kingbee.widget.button.fitbutton.FitButton
import vn.kingbee.widget.signature.views.SignaturePad

class SignatureDemoActivity : BaseActivity() {
    private var mBtNext: FitButton? = null
    private var mBtRefresh: TextView? = null
    private var mViewSignature: SignaturePad? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signature_demo)

        addViews()
        addEvents()
    }

    fun addViews() {
        mBtNext = findViewById(R.id.bt_next)
        mBtRefresh = findViewById(R.id.tv_refresh)
        mViewSignature = findViewById(R.id.view_signature)
    }

    fun addEvents() {
        mBtNext?.setOnClickListener {
            saveSignatureImage()
        }
        mBtRefresh?.setOnClickListener {
            mViewSignature?.clear()
        }
    }

    fun saveSignatureImage() {
        FileUtils.writeBitmapToFile(
            mViewSignature?.getSignatureBitmap()!!,
            ONBOARDING_ESIGNATURE_FILE,
            ESIGNATURE_EXTENSION
        )
    }

    companion object {
        private const val ONBOARDING_DIRECTORY = "onboarding"
        private const val ONBOARDING_ESIGNATURE_FILE = "$ONBOARDING_DIRECTORY/e_signature_temp"
        const val ESIGNATURE_EXTENSION = "jpg"
    }
}