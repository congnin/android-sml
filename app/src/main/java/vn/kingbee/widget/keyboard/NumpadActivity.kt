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
    private var mKeyboardView: NumpadKeyboardView? = null
    private var mOTPEditText: MaterialEditText? = null
    private var mCurrentAmount: MaterialEditText? = null
    private var mNumberAccountEditText: NumpadKeyboardEditText? = null

    private var mKeyboard: NumpadKeyboard? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_numpad_keyboard)

        addViews()
        addEvents()
    }

    private fun addViews() {
        mKeyboardView = findViewById(R.id.keyboardView)
        mOTPEditText = findViewById(R.id.edtOTP)
        mCurrentAmount = findViewById(R.id.edtCurrencyAmount)
        mNumberAccountEditText = findViewById(R.id.edtAccountNumber)

        mContentScrollView = findViewById(R.id.scrollView)
    }

    private fun addEvents() {
        mKeyboard = NumpadKeyboard(this, mKeyboardView!!, this)
        mKeyboard?.registerResponder(mOTPEditText?.getEditText() as NumpadKeyboardEditText, true)
        mOTPEditText?.setNumpadType(NumpadKeyboardType.NUMPAD_PIN)

        mKeyboard?.registerResponder(mNumberAccountEditText!!, true)
        mNumberAccountEditText?.setNumpadType(NumpadKeyboardType.NUMPAD_ACCOUNT_NUMBER)

        mKeyboard?.registerResponder(mCurrentAmount?.getEditText() as NumpadKeyboardEditText, true)
        mCurrentAmount?.setNumpadType(NumpadKeyboardType.NUMPAD_CURRENCY_AMOUNT)

        KeyboardVisibilityEvent.setEventListener(this, KeyboardVisibilityEventListener {
            systemKeyboardOpen(it)
        })
    }

    override fun onDisplayNumpadKeyboard(currentKeyboard: KeyboardView,
                                         responderView: NumpadKeyboardEditText) {
        //show the custom keyboard below the scrollview
        val params = RelativeLayout.LayoutParams(
            RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT
        )
        params.addRule(RelativeLayout.ABOVE, mKeyboardView!!.id)
        mContentScrollView?.layoutParams = params
    }

    override fun onHideNumpadKeyboard(currentKeyboard: KeyboardView,
                                      responderView: NumpadKeyboardEditText) {

    }

    override fun onKeyPressNumpadKeyboard(responderView: NumpadKeyboardEditText,
                                          keyCode: Int): Boolean {
        return false   //use default behavior
    }

    override fun hideCustomKeyboard(event: MotionEvent) {
        val keyboardRect = Rect()
        mKeyboardView?.getGlobalVisibleRect(keyboardRect)
        if (mKeyboard != null && !keyboardRect.contains(event.rawX.toInt(), event.rawY.toInt())) {
            mKeyboard?.hideCustomKeyboard()
        }
    }

    private fun systemKeyboardOpen(isOpen: Boolean) {
        if (isOpen) {
            if (mKeyboard != null) {
                mKeyboard?.hideCustomKeyboard()
            }
        }
    }

    override fun onBackPressed() {
        if (!hideCurrencyKeyboard()) {
            super.onBackPressed()
        }
    }

    private fun hideCurrencyKeyboard(): Boolean {
        if (mKeyboard != null && mKeyboard!!.isCustomKeyboardVisible()) {
            mKeyboard?.hideCustomKeyboard()
            return true
        }

        return false
    }
}