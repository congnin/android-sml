package vn.kingbee.widget.edittext.material.base

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import vn.kingbee.widget.edittext.material.validation.BaseValidationRule
import android.view.LayoutInflater
import androidx.core.content.ContextCompat
import android.view.inputmethod.EditorInfo
import android.text.method.DigitsKeyListener
import android.util.TypedValue
import androidx.annotation.StringRes
import com.google.android.material.textfield.TextInputLayout
import android.widget.EditText
import android.app.Activity
import vn.kingbee.utils.CommonUtils
import android.text.*
import vn.kingbee.widget.R

abstract class BaseMaterialEditText : FrameLayout, View.OnFocusChangeListener, TextWatcher {
    protected var mContext: Context? = null
    protected var mSecondHint: String? = null
    protected var mErrorEmptyMessage: String? = null
    protected var mIsRequired: Boolean = false
    private var mIsIgnoreEmptyValidationCheck: Boolean = false

    protected var mTextColor: Int = 0
    protected var mSecondHintTextColor: Int = 0

    protected var mRules: List<BaseValidationRule>? = null

    private var mOnTextChangedListener: OnTextChangedListener? = null
    private var mOnFocusChangeListener: View.OnFocusChangeListener? = null

    private var mEnable = true

    constructor(context: Context) : super(context) {
        init(context, null)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init(context, attrs)
    }

    private fun init(context: Context, attrs: AttributeSet?) {
        mContext = context
        mRules = ArrayList()
        val view = LayoutInflater.from(mContext).inflate(getLayout(), this, false)
        initView(view)
        initEvent()
        initAttrs(attrs)
        addView(view)
    }

    protected abstract fun initView(view: View)

    protected fun initEvent() {
        getEditText().onFocusChangeListener = this
        getEditText().addTextChangedListener(this)
    }

    @SuppressLint("CustomViewStyleable")
    protected fun initAttrs(attrs: AttributeSet?) {
        if (attrs == null) {
            return
        }

        val ta = context.obtainStyledAttributes(attrs, R.styleable.MaterialEditText, 0, 0)
        getTextInputLayout().hint = ta.getString(R.styleable.MaterialEditText_editTextFirstHint)
        mSecondHint = ta.getString(R.styleable.MaterialEditText_editTextSecondHint)
        mErrorEmptyMessage = ta.getString(R.styleable.MaterialEditText_emptyErrorMessage)

        if (mErrorEmptyMessage != null && !mErrorEmptyMessage!!.isEmpty()) {
            mIsRequired = true
        }

        val inputType = ta.getInt(R.styleable.MaterialEditText_android_inputType, -1)
        if (inputType != -1) {
            getEditText().inputType = inputType
        }

        val textSize = ta.getDimensionPixelSize(R.styleable.MaterialEditText_android_textSize, 0)
        if (textSize != 0) {
            getEditText().setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize.toFloat())
        }

        val lengthMax = ta.getInt(R.styleable.MaterialEditText_android_maxLength, 0)
        if (lengthMax != 0) {
            getEditText().filters = arrayOf<InputFilter>(InputFilter.LengthFilter(lengthMax))
        }
        val digits = ta.getString(R.styleable.MaterialEditText_android_digits)
        if (digits != null && !digits.isEmpty()) {
            getEditText().keyListener = DigitsKeyListener.getInstance(digits)
        }

        val imeAction = ta.getInt(R.styleable.MaterialEditText_android_imeOptions, EditorInfo.IME_ACTION_DONE)
        getEditText().imeOptions = imeAction

        mTextColor = ta.getColor(R.styleable.MaterialEditText_textColor, ContextCompat.getColor(context, R.color.black))
        getEditText().setTextColor(mTextColor)

        mSecondHintTextColor = ta.getColor(
            R.styleable.MaterialEditText_edtTextSecondHintColor,
            ContextCompat.getColor(context, R.color.light_grey3)
        )
        getEditText().setHintTextColor(mSecondHintTextColor)

        setEnable(ta.getBoolean(R.styleable.MaterialEditText_enable, true))
        getEditText().isEnabled = ta.getBoolean(R.styleable.MaterialEditText_enableEditText, true)
        getEditText().setCompoundDrawablesWithIntrinsicBounds(
            0,
            0,
            ta.getResourceId(R.styleable.MaterialEditText_drawableRightEditText, 0),
            0
        )
        getEditText().setSingleLine(ta.getBoolean(R.styleable.MaterialEditText_singleLine, true))
        val type = getEditText().inputType
        getEditText().inputType = type or InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS
        ta.recycle()
    }

    protected abstract fun getLayout(): Int

    protected abstract fun getEditText(): EditText

    protected abstract fun getTextInputLayout(): TextInputLayout

    abstract fun getText(): String

    fun setText(@StringRes id: Int) {
        getEditText().setText(id)
    }

    open fun setText(text: String) {
        if (text.isEmpty()) {
            getEditText().hint = ""
        }
        getEditText().setText(text)
    }

    fun showSecondHint() {
        getEditText().hint = mSecondHint
    }

    fun addValidationRule(rule: BaseValidationRule) {
        (mRules as? ArrayList)?.add(rule)
    }

    fun clearValidationRule() {
        (mRules as? ArrayList)?.clear()
    }

    @SuppressLint("ClickableViewAccessibility")
    fun setEnable(isEnable: Boolean) {
        mEnable = isEnable
        if (mEnable) {
            getEditText().setTextColor(mTextColor)
            getEditText().setOnTouchListener(null)
            getEditText().isFocusable = true
            getEditText().isFocusableInTouchMode = true
        } else {
            getEditText().setTextColor(ContextCompat.getColor(context, R.color.light_grey))
            getEditText().setOnTouchListener { _, _ -> true }
            getEditText().isFocusable = false
            getEditText().isFocusableInTouchMode = false
        }
    }

    fun isEnable(): Boolean {
        return mEnable
    }

    fun isValid(): Boolean {
        if (visibility == View.GONE || visibility == View.INVISIBLE || !mEnable) {
            return true
        }
        clearErrorMessage()

        if (!mIsIgnoreEmptyValidationCheck) {
            if (getEditText().text.toString().trim().isEmpty() && mIsRequired) {
                showError(mErrorEmptyMessage!!)
                getEditText().hint = mSecondHint
                return false
            }

            // These code shouldn't be exist in the core class. This try fixing the bug in the special case.
            if (!mIsRequired && getEditText().text.toString().isEmpty()) {
                return true
            }
        }

        for (rule in mRules!!) {
            if (!rule.isValid(getText())) {
                showError(rule.errorMessage)
                return false
            }
        }
        return true
    }

    fun setEmptyErrorMessage(errorEmptyMessage: String) {
        mErrorEmptyMessage = errorEmptyMessage
        mIsRequired = true
    }

    /**
     * This is the tricky to support ignoring the empty rule
     * This method try to break the core rule which defied as the standard rule at the beginning.
     */
    fun ignoreEmptyValidationCheck() {
        mIsIgnoreEmptyValidationCheck = true
    }

    fun showError(message: String) {
        getTextInputLayout().error = message
    }

    fun clearErrorMessage() {
        getTextInputLayout().error = null
    }

    fun setFirstHint(hint: String) {
        getTextInputLayout().hint = hint
    }

    override fun onFocusChange(v: View?, hasFocus: Boolean) {
        handleFocusChange(hasFocus)
        if (mOnFocusChangeListener != null) {
            mOnFocusChangeListener?.onFocusChange(v, hasFocus)
        }
    }

    protected fun handleFocusChange(hasFocus: Boolean) {
        if (hasFocus) {
            getEditText().hint = mSecondHint
            CommonUtils.showKeyboard(context as Activity, getEditText())
        } else {
            if (TextUtils.isEmpty(getTextInputLayout().error)) {
                getEditText().hint = ""
            }
            isValid()
        }
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        // No implementation
    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        // No implementation
    }

    override fun afterTextChanged(s: Editable?) {
        clearErrorMessage()
        if (mOnTextChangedListener != null) {
            mOnTextChangedListener?.afterTextChanged(s.toString())
        }
    }

    fun setOnTextChangeListener(listener: OnTextChangedListener) {
        mOnTextChangedListener = listener
    }

    override fun setOnFocusChangeListener(l: OnFocusChangeListener?) {
        mOnFocusChangeListener = l
    }

    interface OnTextChangedListener {
        fun afterTextChanged(s: String)
    }
}