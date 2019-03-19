package vn.kingbee.widget.pin

import android.os.Bundle
import android.widget.TextView
import vn.kingbee.widget.BaseActivity
import vn.kingbee.widget.R
import vn.kingbee.widget.pin.square.SquarePinEntryView
import vn.kingbee.widget.utils.CommonUtils

class PinActivity : BaseActivity() {
    private var otpView: SquarePinEntryView? = null
    private var otpResend: TextView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pin)

        addViews()

        addEvents()
    }

    fun addViews() {
        otpView = findViewById(R.id.otp_view)
        otpResend = findViewById(R.id.fragment_otp_resend)
    }

    fun addEvents() {
        otpView?.setOnClickListener {
            otpView?.getEditText()?.requestFocus()
            CommonUtils.showKeyboard(this, otpView!!)
        }


        otpResend?.setOnClickListener {

        }
    }
}