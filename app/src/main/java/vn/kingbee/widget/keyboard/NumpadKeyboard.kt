package vn.kingbee.widget.keyboard

import android.annotation.SuppressLint
import android.inputmethodservice.Keyboard
import android.inputmethodservice.KeyboardView
import android.view.View
import android.app.Activity
import android.os.Build
import android.text.InputType
import android.view.MotionEvent
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.view.inputmethod.InputMethodManager
import vn.kingbee.widget.R
import vn.kingbee.widget.utils.CommonUtils


const val KEY_CODE_NIL = -1
const val KEY_CODE_BACK = Keyboard.KEYCODE_DELETE
const val KEY_CODE_DONE = Keyboard.KEYCODE_DONE
const val KEY_CODE_DECIMAL = 46
const val KEY_CODE_NEXT = 55005
const val KEY_CODE_CLEAR = 55006
const val KEY_CODE_TRIPLE_ZERO = 55007
const val PTBC_CURRENCY = "RP "

class NumpadKeyboard(
    activity: Activity, keyboardView: NumpadKeyboardView, stateListener: OnKeyboardStateChangedListener
) {


    private var mKeyboardView: NumpadKeyboardView? = keyboardView
    private var mActivity: Activity? = activity
    private var mStateListener: OnKeyboardStateChangedListener? = stateListener
    private var mActiveResponderView: NumpadKeyboardEditText? = null

    private val mOnKeyboardActionListener = object : KeyboardView.OnKeyboardActionListener {

        override fun onKey(primaryCode: Int, keyCodes: IntArray) {
            //give a chance for the listener to handle the key press
            if (!mStateListener!!.onKeyPressNumpadKeyboard(mActiveResponderView!!, primaryCode)) {
                //listener did not handle keypress event

                //use the default behavior
                val editable = mActiveResponderView?.text

                val start = mActiveResponderView?.selectionStart

                if (primaryCode == KEY_CODE_DONE) {
                    hideCustomKeyboard()
                } else if (primaryCode == KEY_CODE_CLEAR) {
                    editable?.clear()
                } else if (primaryCode == KEY_CODE_BACK) {
                    if (editable != null && start!! > 0) {
                        editable.delete(start - 1, start)
                    }
                } else if (primaryCode == KEY_CODE_NEXT) {
                    hideCustomKeyboard()
                    val viewToFocus = mActiveResponderView?.focusSearch(View.FOCUS_DOWN)
                    viewToFocus?.requestFocus()
                } else if (primaryCode == KEY_CODE_TRIPLE_ZERO) {
                    editable!!.insert(start!!, "000")
                } else if (primaryCode == KEY_CODE_DECIMAL) {
                    //TODO: implement logic for handling decimal
                } else if (primaryCode == KEY_CODE_NIL) {
                    //do nothing
                } else {
                    //it's a digit
                    editable!!.insert(start!!, Character.toString(primaryCode.toChar()))
                }

                val keyboardType = mActiveResponderView?.getNumpadType()
                if (keyboardType === NumpadKeyboardType.NUMPAD_CURRENCY_AMOUNT) {
                    //format the amount as currency amount
                    if (editable != null && editable.isNotEmpty()) {
                        val digitString = editable.toString().replace("[^\\d]".toRegex(), "")
                        if (!digitString.isEmpty()) {
                            editable.replace(
                                0,
                                editable.length,
                                digitString.toBigDecimal().toString()
                            )
                        } else {
                            editable.replace(0, editable.length, "")
                        }
                    }
                } else {
                    if (editable != null && editable.isNotEmpty()) {
                        val digitString = editable.toString().replace("[^\\d]".toRegex(), "")
                        if (!digitString.isEmpty()) {
                            editable.replace(0, editable.length, digitString)
                        } else {
                            editable.replace(0, editable.length, "")
                        }
                    }
                }
            }
        }

        override fun onPress(arg0: Int) {}

        override fun onRelease(primaryCode: Int) {}

        override fun onText(text: CharSequence) {}

        override fun swipeDown() {}

        override fun swipeLeft() {}

        override fun swipeRight() {}

        override fun swipeUp() {}
    }

    init {
        mKeyboardView?.isPreviewEnabled = false
    }

    fun isCustomKeyboardVisible(): Boolean {
        return mKeyboardView?.visibility == View.VISIBLE
    }

    fun showCustomKeyboard(responderView: NumpadKeyboardEditText, shouldAnimate: Boolean) {
        //deselect previous selected field
        mKeyboardView?.deselectCurrentActiveResponderViewIfNeed()
        mKeyboardView?.setCurrentActiveResponderView(responderView)

        mKeyboardView?.setOnKeyboardActionListener(mOnKeyboardActionListener)
        mActiveResponderView = responderView
        responderView.isSelected = true

        //hide the android keyboard if shown
        val focusedView = mActivity?.currentFocus
        if (focusedView != null) {
            val imm = mActivity?.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(focusedView.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
        }

        //setting the keyboard layout
        val numpadType = mActiveResponderView?.getNumpadType()
        when (numpadType) {
            NumpadKeyboardType.NUMPAD_CURRENCY_AMOUNT -> mKeyboardView?.keyboard =
                Keyboard(mActivity, R.xml.currency_amount_keyboard)
            NumpadKeyboardType.NUMPAD_ACCOUNT_NUMBER -> mKeyboardView?.keyboard =
                Keyboard(mActivity, R.xml.bank_account_number_keyboard)
            NumpadKeyboardType.NUMPAD_PIN -> mKeyboardView?.keyboard = Keyboard(mActivity, R.xml.pin_keyboard)
        }
        if (!isCustomKeyboardVisible()) {
            mKeyboardView?.visibility = View.VISIBLE
            if (shouldAnimate) {
                //animate the keyboard sliding up
                val slide = AnimationUtils.loadAnimation(mActivity, R.anim.slide_bottom_up)
                slide.setAnimationListener(object : Animation.AnimationListener {
                    override fun onAnimationStart(animation: Animation?) {

                    }

                    override fun onAnimationEnd(animation: Animation?) {
                        //delay the call until animation ends
                        mStateListener?.onDisplayNumpadKeyboard(mKeyboardView!!, mActiveResponderView!!)
                    }

                    override fun onAnimationRepeat(animation: Animation?) {

                    }
                })
                mKeyboardView?.startAnimation(slide)
            } else {
                //notify the listener
                mStateListener?.onDisplayNumpadKeyboard(mKeyboardView!!, mActiveResponderView!!)
            }
        }

        if (mActiveResponderView?.getNumpadType() == NumpadKeyboardType.NUMPAD_CURRENCY_AMOUNT) {
            mActiveResponderView?.setPrefix(PTBC_CURRENCY)      //show the currency code, and a space
            mActiveResponderView?.hint = ""
        } else if (mActiveResponderView?.getNumpadType() == NumpadKeyboardType.NUMPAD_ACCOUNT_NUMBER) {
            mActiveResponderView?.hint =
                mActiveResponderView?.hint.toString() + " "     //reset hint to enable Paste option
        }
    }

    fun hideCustomKeyboard() {
        mKeyboardView?.deselectCurrentActiveResponderViewIfNeed()
        if (mActiveResponderView != null) {
            mActiveResponderView?.inputType = InputType.TYPE_NULL
            mActiveResponderView?.clearFocus()
        }
        if (isCustomKeyboardVisible()) {
            mStateListener?.onHideNumpadKeyboard(mKeyboardView!!, mActiveResponderView!!)

            mKeyboardView?.visibility = View.GONE
            mKeyboardView?.isEnabled = false

            if (mActiveResponderView?.getNumpadType() == NumpadKeyboardType.NUMPAD_CURRENCY_AMOUNT) {
                //checking that entered currency amount is valid

                var isEmptyOrInvalid = false
                //don't show currency if no amount is entered or if amount is negative or zero
                if (mActiveResponderView?.length() == 0) {
                    //empty text, set invalid flag
                    isEmptyOrInvalid = true
                } else {
                    //checking if digit is non-positive
                    val digitString = mActiveResponderView?.text.toString().replace("[^\\d]", "")
                    if (digitString.isNotEmpty()) {
                        val digits = digitString.toDouble()
                        if (digits <= 0) {
                            //digit is non-positive, set invalid flag
                            isEmptyOrInvalid = true
                        }
                    }
                }

                if (isEmptyOrInvalid) {
                    //empty or invalid entry, delete the text
                    mActiveResponderView?.setText("")
                    mActiveResponderView?.setPrefix("")
                }
            }
        }

        if (mActiveResponderView != null && mActiveResponderView?.getTag(R.id.PLACEHOLDER_TEXT) != null) {
            //restore the placeholder
            mActiveResponderView?.hint = mActiveResponderView?.getTag(R.id.PLACEHOLDER_TEXT).toString()
        }
    }

    @SuppressLint("ObsoleteSdkInt", "ClickableViewAccessibility")
    fun registerResponder(responderView: NumpadKeyboardEditText, shouldShowCursor: Boolean) {
        responderView.inputType = InputType.TYPE_NULL

        if (responderView.hint != null) {
            //save the placeholder text
            responderView.setTag(R.id.PLACEHOLDER_TEXT, responderView.hint.toString())
        }

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            if (shouldShowCursor) {
                responderView.setTextIsSelectable(true)    //Required for cursor to show
            }
        }

        if (!shouldShowCursor) {
            responderView.isCursorVisible = false
        }
        responderView.setOnClickListener { view ->
            showCustomKeyboard(view as NumpadKeyboardEditText, true)
            CommonUtils.hideKeyboard(mActivity, responderView)
        }

        responderView.setOnTouchListener(object : View.OnTouchListener {
            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                val editText = v as NumpadKeyboardEditText

                if (shouldShowCursor) {
                    editText.isCursorVisible = true    //force show the cursor indicator
                }
                editText.onTouchEvent(event)   //propogate event
                showCustomKeyboard(editText, true)
                CommonUtils.hideKeyboard(mActivity, responderView)

                if (shouldShowCursor && android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                    editText.setRawInputType(InputType.TYPE_CLASS_TEXT)    //required to show the cursor indicator
                }

                return true // Consume touch event
            }
        })

        responderView.onFocusChangeListener = View.OnFocusChangeListener { v, hasFocus ->
            if (hasFocus) {
                CommonUtils.hideKeyboard(mActivity, responderView)
            }
        }
    }


    interface OnKeyboardStateChangedListener {
        fun onDisplayNumpadKeyboard(currentKeyboard: KeyboardView, responderView: NumpadKeyboardEditText)

        fun onHideNumpadKeyboard(currentKeyboard: KeyboardView, responderView: NumpadKeyboardEditText)

        fun onKeyPressNumpadKeyboard(responderView: NumpadKeyboardEditText, keyCode: Int): Boolean
    }
}