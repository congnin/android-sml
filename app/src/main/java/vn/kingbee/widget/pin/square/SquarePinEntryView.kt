package vn.kingbee.widget.pin.square

import android.annotation.SuppressLint
import android.content.Context
import android.os.Handler
import android.os.Message
import android.support.v7.widget.LinearLayoutCompat
import android.util.AttributeSet
import vn.kingbee.widget.edittext.EditTextInterface
import android.widget.EditText
import android.view.Gravity
import android.support.v4.content.ContextCompat
import android.util.TypedValue
import vn.kingbee.widget.R
import android.support.v4.view.ViewCompat
import android.view.inputmethod.EditorInfo
import android.text.InputType
import android.text.InputFilter
import timber.log.Timber
import android.text.Editable
import android.text.TextWatcher
import android.view.animation.AnimationUtils
import vn.kingbee.widget.keyboard.NumpadKeyboardEditText
import vn.kingbee.widget.keyboard.NumpadKeyboardType

const val DEFAULT_PIN_CIRCLE_SIZE = 24F
const val DELAY_TIME = 500L

class SquarePinEntryView : LinearLayoutCompat, EditTextInterface {

    private var mDigits = 4
    private var mDigitWidth: Int = 0
    private var mDigitHeight: Int = 0
    private var mDefaultDrawableResource: Int = 0
    private var mInActiveDrawableResource: Int = 0
    private var mFocusedDrawableResource: Int = 0
    private var mTextBackgroundDrawableResource: Int = 0
    private var mErrorDrawableResource: Int = 0
    private var mDigitSpacing: Int = 0
    private var mDigitTextSize: Int = 0
    private var mDigitTextColor: Int = 0
    private var mDigitFocusedBackground: Int = 0
    private var mCircleSize: Int = 0
    private var mEditText: NumpadKeyboardEditText? = null

    private var mOnPinValidated: EventListener? = null
    private var mOnPinUnvalidtated: EventListener? = null

    private var mIsTextIsInDeleteMode = false
    private var mIsPinEntryShowing = false
//    private val mDelayHidePinEntryHandler = DelayHidePinEntryHandler()

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        initAttributes(context, attrs)
        // Add child views
        addViews()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        initAttributes(context, attrs)
        // Add child views
        addViews()
    }

    private fun initAttributes(context: Context, attrs: AttributeSet) {
        val array = getContext().obtainStyledAttributes(attrs, R.styleable.SquarePinEntryView)
        mDigits = array.getInt(
                R.styleable.SquarePinEntryView_pinNumbers,
                context.resources.getInteger(R.integer.pin_max_length)
        )
        val metrics = resources.displayMetrics
        mDigitWidth = array.getDimensionPixelSize(
                R.styleable.SquarePinEntryView_pinSize,
                TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 30f, metrics).toInt()
        )
        mDigitHeight = array.getDimensionPixelSize(
                R.styleable.SquarePinEntryView_pinSize,
                TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 30f, metrics).toInt()
        )
        mDigitSpacing = array.getDimensionPixelSize(
                R.styleable.SquarePinEntryView_pinSpacing,
                TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 20f, metrics).toInt()
        )
        mDigitTextSize = array.getDimensionPixelSize(R.styleable.SquarePinEntryView_pinTextSize, 12)
        // Get theme to resolve defaults
        val theme = getContext().theme

        // Background colour, default to android:windowBackground from theme
        val background = TypedValue()
        theme.resolveAttribute(android.R.attr.windowBackground, background, true)

        mDefaultDrawableResource = array.getResourceId(
                R.styleable.SquarePinEntryView_pinDefaultColor,
                background.resourceId
        )

        mInActiveDrawableResource = array.getResourceId(R.styleable.SquarePinEntryView_pinInActiveColor, -1)

        mTextBackgroundDrawableResource = array.getResourceId(R.styleable.SquarePinEntryView_pinBackgroundColor, -1)

        mFocusedDrawableResource = array.getResourceId(R.styleable.SquarePinEntryView_pinActiveColor, -1)

        mErrorDrawableResource = array.getResourceId(R.styleable.SquarePinEntryView_pinErrorColor, -1)

        mCircleSize = array.getDimensionPixelSize(
                R.styleable.SquarePinEntryView_pinCircleSize,
                TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                        DEFAULT_PIN_CIRCLE_SIZE, metrics).toInt()
        )

        mDigitFocusedBackground = array.getResourceId(R.styleable.PinEntryView_digitBackground, -1)

        // Text colour, default to android:textColorPrimary from theme
        val textColor = TypedValue()
        theme.resolveAttribute(android.R.attr.textColorPrimary, textColor, true)
        mDigitTextColor = array.getColor(
                R.styleable.SquarePinEntryView_pinTextColor,
                ContextCompat.getColor(getContext(), if (textColor.resourceId > 0) textColor.resourceId else textColor.data)
        )

        // recycle array
        array.recycle()
        gravity = Gravity.CENTER
    }

    fun getText(): String {
        return mEditText?.text.toString()
    }

    fun setText(text: CharSequence) {
        var newText = text
        if (newText.length > mDigits) {
            newText = newText.subSequence(0, mDigits)
        }
        mEditText?.setText(newText)
    }

    fun getEditText(): EditText {
        return mEditText!!
    }

    fun shake() {
        val animation = AnimationUtils.loadAnimation(
                context,
                R.anim.shake_horizontal
        )
        startAnimation(animation)
    }

    private fun addViews() {
        // Add a digit view for each digit
        addPinItems()
        // Add an "invisible" edit text to handle input
        addHiddenEditText()
    }

    private fun removeAllCallback() {
//        mDelayHidePinEntryHandler.removeCallbacksAndMessages()
    }

    private fun addHiddenEditText() {
        initEditTextValues()
        addView(mEditText)
        mEditText?.addTextChangedListener(
                object : TextWatcher {
                    override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                        mIsTextIsInDeleteMode = count > after
                        hideSAPinPreviousItem(s)
                    }

                    override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                        // Do nothing
                    }

                    override fun afterTextChanged(s: Editable) {
                        Timber.i("SquarePinEntryView.afterTextChanged(), s=$s")
                        val length = s.length
                        if (length == 0) {
                            setPinItemStatusAtFirstTime()
                            return
                        }
                        if (mIsTextIsInDeleteMode) {
//                            mDelayHidePinEntryHandler.removeCallbacksAndMessages()
                            setPinItemStatusWhenDeleteAtPosition(length)
                        } else {
                            processWhenUserTypeAtPosition(s, length)
                        }

                        if (length == mDigits) {
                            mOnPinValidated?.onEvent()
                        } else {
                            mOnPinUnvalidtated?.onEvent()
                        }
                    }
                })
    }

    private fun hideSAPinPreviousItem(s: CharSequence) {
        Timber.i("SquarePinEntryView.hideSAPinPreviousItem(), s=$s")
        for (i in 0..s.length - 1) {
            val pinEntry = getChildAt(i) as PinItem
            pinEntry.inActiveFillFocus()
        }
    }

    private fun processWhenUserTypeAtPosition(s: Editable, length: Int) {
        Timber.i("SquarePinEntryView.processWhenUserTypeAtPosition(), s=$s, length=$length")
        var indexView = length - 1
        if (indexView < 0) {
            indexView = 0
        }
        val pinEntryView = getChildAt(indexView) as PinItem
        pinEntryView.activeEmptyFocus()
        pinEntryView.setText(Character.toString(s[indexView]))

        Timber.i("SquarePinEntryView.processWhenUserTypeAtPosition(), indexView=$indexView")
        for (i in indexView + 1 until mDigits) {
            val pinItem = getChildAt(i) as PinItem
            pinItem.setDefaultStatus()
        }

        mIsPinEntryShowing = true
//        mDelayHidePinEntryHandler.removeCallbacksAndMessages()
//        mDelayHidePinEntryHandler.setPinEntryTextView(pinEntryView)
//        mDelayHidePinEntryHandler.sendEmptyMessageDelayed(indexView, DELAY_TIME)
    }

    private fun setPinItemStatusWhenDeleteAtPosition(length: Int) {
        for (i in length until mDigits) {
            val pinEntry = getChildAt(i) as PinItem
            if (i == length) {
                pinEntry.activeEmptyFocus()
            } else {
                pinEntry.setDefaultStatus()
            }
        }
    }

    private fun setPinItemStatusAtFirstTime() {
        for (i in 0 until mDigits) {
            val pinEntry = getChildAt(i) as PinItem
            if (i == 0) {
                pinEntry.activeEmptyFocus()
            } else {
                pinEntry.setDefaultStatus()
            }
        }
    }

    private fun initEditTextValues() {
        mEditText = NumpadKeyboardEditText(context)
        mEditText?.gravity = Gravity.CENTER
        mEditText?.setBackgroundColor(ContextCompat.getColor(context, android.R.color.transparent))
        mEditText?.setTextColor(ContextCompat.getColor(context, android.R.color.transparent))
        mEditText?.isCursorVisible = false
        mEditText?.filters = arrayOf<InputFilter>(InputFilter.LengthFilter(mDigits))
        mEditText?.inputType = InputType.TYPE_CLASS_NUMBER
        mEditText?.imeOptions = EditorInfo.IME_FLAG_NO_EXTRACT_UI
        val params = LinearLayoutCompat.LayoutParams(0, 0)
        mEditText?.layoutParams = params
        ViewCompat.setImportantForAccessibility(
                mEditText!!,
                ViewCompat.IMPORTANT_FOR_ACCESSIBILITY_NO
        )
        mEditText?.setNumpadType(NumpadKeyboardType.NUMPAD_PIN)
    }

    private fun addPinItems() {
        for (i in 0 until mDigits) {
            val digitView = SquarePinItem(context)
            digitView.setBackgroundDefault(ContextCompat.getDrawable(context, mDefaultDrawableResource)!!)
            digitView.background = ContextCompat.getDrawable(context, mInActiveDrawableResource)
            digitView.setFocusBackground(ContextCompat.getDrawable(context, mFocusedDrawableResource)!!)
            digitView.setErrorColor(ContextCompat.getDrawable(context, mErrorDrawableResource)!!)
            digitView.setTextBackgroundColor(ContextCompat.getDrawable(context, mTextBackgroundDrawableResource)!!)
            digitView.setTextColor(mDigitTextColor)
            digitView.setTextSize(mDigitTextSize)
            digitView.setCircleSize(mCircleSize)
            val params = LinearLayoutCompat.LayoutParams(mDigitWidth, mDigitHeight)
            if (i == 0) {
                digitView.activeEmptyFocus()
            } else {
                digitView.setDefaultStatus()
                params.leftMargin = mDigitSpacing
            }
            addView(digitView.getPinItem(), i, params)
        }
    }

    @SuppressLint("ResourceType")
    override fun setBackgroundResource(resourceId: Int) {
        if (resourceId > 0) {
            for (i in 0 until mDigits) {
                getChildAt(i).setBackgroundResource(resourceId)
            }
        }
    }

    fun setOnPinValidatedListener(event: EventListener) {
        this.mOnPinValidated = event
    }

    fun setOnPinInvalidatedListener(event: EventListener) {
        this.mOnPinUnvalidtated = event
    }

    override fun appendCharacter(ch: CharSequence) {
        mEditText?.append(ch)
    }

    override fun clear() {
        mEditText?.setText("")
        removeAllCallback()
    }

    override fun getValue(): String {
        return mEditText?.text.toString()
    }

    override fun setValue(value: String) {
        mEditText?.setText(value)
    }

    private fun setPinStatus(position: Int, mPinEntryTextView: PinItem) {
        mPinEntryTextView.inActiveFillFocus()
        if (position < mDigits - 1) {
            val item = getChildAt(position + 1) as PinItem
            item.activeEmptyFocus()
        }
    }

    fun setErrorStatus() {
        clear()
        for (i in 0 until mDigits) {
            val pinEntry = getChildAt(i) as PinItem
            pinEntry.setErrorStatus()
        }
    }

    @SuppressLint("HandlerLeak")
    private inner class DelayHidePinEntryHandler : Handler() {
        private var mPinEntryTextView: PinItem? = null

        internal fun setPinEntryTextView(tv: PinItem) {
            mPinEntryTextView = tv
        }

        override fun handleMessage(msg: Message) {
            if (mPinEntryTextView != null) {
                setPinStatus(msg.what, mPinEntryTextView!!)
            }
        }

        internal fun removeCallbacksAndMessages() {
            super.removeCallbacksAndMessages(null)
        }
    }
}