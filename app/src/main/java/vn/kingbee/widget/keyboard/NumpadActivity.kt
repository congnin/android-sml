package vn.kingbee.widget.keyboard

import android.graphics.Rect
import android.inputmethodservice.KeyboardView
import android.os.Bundle
import android.view.MotionEvent
import vn.kingbee.widget.BaseActivity
import vn.kingbee.widget.R
import android.widget.RelativeLayout
import android.widget.ScrollView
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEventListener
import vn.kingbee.widget.edittext.material.MaterialEditText

class NumpadActivity : BaseActivity(), NumpadKeyboard.OnKeyboardStateChangedListener {

    private var mContentScrollView: ScrollView? = null
    private var mPinKeyboardView: NumpadKeyboardView? = null
    private var mOTPEditText: MaterialEditText? = null
    private var mNumberAccountEditText: NumpadKeyboardEditText? = null

    private var mPinKeyboard: NumpadKeyboard? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_numpad_keyboard)

        addViews()
        addEvents()
    }

    private fun addViews() {
        mPinKeyboardView = findViewById(R.id.pinKeyboardView)
        mOTPEditText = findViewById(R.id.edtOTP)
        mNumberAccountEditText = findViewById(R.id.edtAccountNumber)

        mContentScrollView = findViewById(R.id.fragment_payment_confirm_contentScrollView)
    }

    private fun addEvents() {
        mPinKeyboard = NumpadKeyboard(this, mPinKeyboardView!!, this)
        mPinKeyboard?.registerResponder(mOTPEditText?.getEditText() as NumpadKeyboardEditText, true)
        mOTPEditText?.setNumpadType(NumpadKeyboardType.NUMPAD_PIN)

        mPinKeyboard?.registerResponder(mNumberAccountEditText!!, true)
        mNumberAccountEditText?.setNumpadType(NumpadKeyboardType.NUMPAD_ACCOUNT_NUMBER)

        KeyboardVisibilityEvent.setEventListener(this, KeyboardVisibilityEventListener {
            systemKeyboardOpen(it)
        })
    }

    override fun onDisplayNumpadKeyboard(currentKeyboard: KeyboardView, responderView: NumpadKeyboardEditText) {
        //show the custom keyboard below the scrollview
        val params = RelativeLayout.LayoutParams(
            RelativeLayout.LayoutParams.MATCH_PARENT,
            RelativeLayout.LayoutParams.MATCH_PARENT
        )
        params.addRule(RelativeLayout.ABOVE, mPinKeyboardView!!.id)
        mContentScrollView?.layoutParams = params
    }

    override fun onHideNumpadKeyboard(currentKeyboard: KeyboardView, responderView: NumpadKeyboardEditText) {

    }

    override fun onKeyPressNumpadKeyboard(responderView: NumpadKeyboardEditText, keyCode: Int): Boolean {
        return false   //use default behavior
    }

    override fun hideCustomKeyboard(event: MotionEvent) {
        val keyboardRect = Rect()
        mPinKeyboardView?.getGlobalVisibleRect(keyboardRect)
        if (mPinKeyboard != null && !keyboardRect.contains(event.rawX.toInt(), event.rawY.toInt())) {
            mPinKeyboard?.hideCustomKeyboard()
        }
    }

    private fun systemKeyboardOpen(isOpen: Boolean) {
        if (isOpen) {
            if (mPinKeyboard != null) {
                mPinKeyboard?.hideCustomKeyboard()
            }
        }
    }
}