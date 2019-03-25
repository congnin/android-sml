package vn.kingbee.widget.pin

import android.graphics.Rect
import android.inputmethodservice.Keyboard
import android.inputmethodservice.KeyboardView
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.widget.RelativeLayout
import android.widget.ScrollView
import android.widget.TextView
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEventListener
import vn.kingbee.widget.BaseActivity
import vn.kingbee.widget.R
import vn.kingbee.widget.keyboard.NumpadKeyboard
import vn.kingbee.widget.keyboard.NumpadKeyboardEditText
import vn.kingbee.widget.keyboard.NumpadKeyboardType
import vn.kingbee.widget.keyboard.NumpadKeyboardView
import vn.kingbee.widget.pin.square.SquarePinEntryView
import vn.kingbee.widget.utils.CommonUtils

class PinActivity : BaseActivity(), NumpadKeyboard.OnKeyboardStateChangedListener {

    private var mContentScrollView: ScrollView? = null
    private var otpView: SquarePinEntryView? = null
    private var mKeyboardView: NumpadKeyboardView? = null
    private var mKeyboard: NumpadKeyboard? = null
    private var otpResend: TextView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pin)

        addViews()

        addEvents()
    }

    private fun addViews() {
        otpView = findViewById(R.id.otp_view)
        otpResend = findViewById(R.id.fragment_otp_resend)
        mKeyboardView = findViewById(R.id.keyboardView)

        mContentScrollView = findViewById(R.id.mainScrollView)
    }

    private fun addEvents() {
        mKeyboard = NumpadKeyboard(this, mKeyboardView!!, this)
        mKeyboard?.registerResponder(otpView?.getEditText() as NumpadKeyboardEditText, false)
        (otpView?.getEditText() as NumpadKeyboardEditText).setNumpadType(NumpadKeyboardType.NUMPAD_PIN)

        KeyboardVisibilityEvent.setEventListener(this, KeyboardVisibilityEventListener {
            systemKeyboardOpen(it)
        })
        otpView?.setOnClickListener {
            otpView?.getEditText()?.requestFocus()
            mKeyboardView?.visibility = View.VISIBLE
        }

        otpResend?.setOnClickListener {

        }
    }

    override fun onDisplayNumpadKeyboard(currentKeyboard: KeyboardView, responderView: NumpadKeyboardEditText) {
        val params = RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.MATCH_PARENT
        )
        params.addRule(RelativeLayout.ABOVE, mKeyboardView!!.id)
        mContentScrollView?.layoutParams = params
    }

    override fun onHideNumpadKeyboard(currentKeyboard: KeyboardView, responderView: NumpadKeyboardEditText) {

    }

    override fun onKeyPressNumpadKeyboard(responderView: NumpadKeyboardEditText, keyCode: Int): Boolean {
        return false
    }

    override fun hideCustomKeyboard(event: MotionEvent) {
//        val keyboardRect = Rect()
//        mKeyboardView?.getGlobalVisibleRect(keyboardRect)
//        if (mKeyboard != null && !keyboardRect.contains(event.rawX.toInt(), event.rawY.toInt())) {
//            mKeyboard?.hideCustomKeyboard()
//        }
    }

    private fun systemKeyboardOpen(isOpen: Boolean) {
        if (isOpen) {
            if (mKeyboard != null) {
                mKeyboard?.hideCustomKeyboard()
            }
        }
    }
}