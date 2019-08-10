package vn.kingbee.widget.edittext.state

import android.widget.GridLayout
import android.widget.EditText
import android.text.InputFilter
import android.graphics.drawable.Drawable
import android.content.Context
import android.text.InputType
import butterknife.ButterKnife
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import butterknife.BindView
import android.widget.ImageButton
import android.widget.TextView
import vn.kingbee.widget.R

class BeeInput : GridLayout {
    @BindView(R.id.tv_header)
    lateinit var mTvHeader: TextView

    @BindView(R.id.ib_error)
    lateinit var mIbError: ImageButton

    @BindView(R.id.et_input)
    lateinit var mEtInput: StateEditText

    constructor(context: Context) : super(context) {
        init(context, null)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init(context, attrs)
    }

    protected fun init(context: Context, attrs: AttributeSet?) {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = inflater.inflate(R.layout.view_input, this, true)

        ButterKnife.bind(view)

        if (attrs != null) {
            initAttribute(context, attrs)
        }
    }

    protected fun initAttribute(context: Context, attrs: AttributeSet) {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.BeeInput, 0, 0)

        bindData(
            typedArray.getString(R.styleable.BeeInput_headerText),
            typedArray.getString(R.styleable.BeeInput_inputText)
        )

        val drawableRight = typedArray.getDrawable(R.styleable.BeeInput_inputText_drawableRight)
        if (drawableRight != null) {
            setDrawableRight(drawableRight)
        }

        setInputType(
            typedArray.getInt(
                R.styleable.BeeInput_inputText_inputType, InputType.TYPE_CLASS_TEXT
            )
        )

        val maxLength = typedArray.getInt(R.styleable.BeeInput_inputText_maxLength, -1)
        if (maxLength != -1) {
            setInputMaxLength(maxLength)
        }
        typedArray.recycle()
    }

    protected fun bindData(header: String?, input: String?) {
        if (header != null && !header.isEmpty()) {
            mTvHeader.text = header
        }
        if (input != null && !input.isEmpty()) {
            mEtInput.setText(input)
        }
    }

    fun setOnFocusChangedListener(listener: View.OnFocusChangeListener) {
        mEtInput.onFocusChangeListener = listener
    }

    protected fun setDrawableRight(drawableRight: Drawable) {
        mEtInput.setCompoundDrawablesWithIntrinsicBounds(null, null, drawableRight, null)
    }

    fun setInputType(inputType: Int) {
        mEtInput.inputType = inputType
    }

    protected fun setInputMaxLength(maxLength: Int) {
        mEtInput.filters = arrayOf<InputFilter>(InputFilter.LengthFilter(maxLength))
    }

    fun updateView(action: ViewAction, message: CharSequence) {
        when (action) {
            ViewAction.DATA -> {
                mEtInput.setText(message)
                setErrorStatus(false)
            }

            ViewAction.ERROR -> setError(message)

            else -> setErrorStatus(false)
        }
    }

    private fun setErrorStatus(isShow: Boolean) {
        mIbError.visibility = if (isShow) View.VISIBLE else View.GONE
        mEtInput.setError(isShow)
    }

    private fun setError(message: CharSequence) {
        setErrorStatus(true)
        mIbError.tag = message
    }

    fun setOnErrorListener(listener: View.OnClickListener) {
        mIbError.setOnClickListener(listener)
    }

    fun getText(): String {
        return mEtInput.text.toString()
    }

    fun setText(text: String) {
        mEtInput.setText(text)
    }

    fun getInput(): EditText? {
        return mEtInput
    }
}