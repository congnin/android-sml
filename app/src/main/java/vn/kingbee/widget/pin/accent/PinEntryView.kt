package vn.kingbee.widget.pin.accent

import android.content.Context
import android.os.Build
import androidx.core.content.ContextCompat
import android.text.InputType
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import vn.kingbee.widget.R
import android.view.MotionEvent
import android.view.inputmethod.InputMethodManager
import android.os.Parcelable
import android.widget.TextView
import android.graphics.Canvas
import android.graphics.Paint
import android.os.Parcel
import android.annotation.SuppressLint
import android.text.Editable
import android.text.TextWatcher
import android.view.inputmethod.EditorInfo
import android.text.InputFilter
import android.view.Gravity

class PinEntryView : ViewGroup {
    /**
     * Accent types
     */
    val ACCENT_NONE = 0
    val ACCENT_ALL = 1
    val ACCENT_CHARACTER = 2

    /**
     * Number of digits
     */
    private var digits: Int = 0

    /**
     * Input type
     */
    private var inputType: Int = 0

    /**
     * Pin digit dimensions and styles
     */
    private var digitWidth: Int = 0
    private var digitHeight: Int = 0
    private var digitBackground: Int = 0
    private var digitSpacing: Int = 0
    private var digitTextSize: Int = 0
    private var digitTextColor: Int = 0
    private var digitElevation: Int = 0

    /**
     * Accent dimensions and styles
     */
    private var accentType: Int = 0
    private var accentWidth: Int = 0
    private var accentColor: Int = 0

    /**
     * Character to use for each digit
     */
    private var mask = "*"

    /**
     * Edit text to handle input
     */
    private var editText: EditText? = null

    /**
     * Focus change listener to send focus events to
     */
    private var onFocusChangeListener: View.OnFocusChangeListener? = null

    /**
     * Pin entered listener used as a callback for when all digits have been entered
     */
    private var onPinEnteredListener: OnPinEnteredListener? = null

    /**
     * If set to false, will always draw accent color if type is CHARACTER or ALL
     * If set to true, will draw accent color only when focussed.
     */
    private var accentRequiresFocus: Boolean = false

    constructor(context: Context) : this(context, null)

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    constructor(context: Context, attrs: AttributeSet?, defStyle: Int) : super(context, attrs, defStyle) {
        // Get style information
        val array = context.obtainStyledAttributes(attrs, R.styleable.PinEntryView)
        digits = array.getInt(R.styleable.PinEntryView_numDigits, 4)
        inputType = array.getInt(R.styleable.PinEntryView_pinInputType, InputType.TYPE_CLASS_NUMBER)
        accentType = array.getInt(R.styleable.PinEntryView_accentType, ACCENT_NONE)

        // Dimensions
        val metrics = resources.displayMetrics
        digitWidth = array.getDimensionPixelSize(R.styleable.PinEntryView_digitWidth,
                TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 50F, metrics).toInt())
        digitHeight = array.getDimensionPixelSize(R.styleable.PinEntryView_digitHeight,
                TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 50f, metrics).toInt())
        digitSpacing = array.getDimensionPixelSize(R.styleable.PinEntryView_digitSpacing,
                TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 20f, metrics).toInt())
        digitTextSize = array.getDimensionPixelSize(R.styleable.PinEntryView_digitTextSize,
                TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 15f, metrics).toInt())
        accentWidth = array.getDimensionPixelSize(R.styleable.PinEntryView_accentWidth,
                TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 3f, metrics).toInt())
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            digitElevation = array.getDimensionPixelSize(R.styleable.PinEntryView_digitElevation, 0)
        }

        // Get theme to resolve defaults
        val theme = context.theme

        // Background colour, default to android:windowBackground from theme
        val background = TypedValue()
        theme.resolveAttribute(android.R.attr.windowBackground, background, true)
        digitBackground = array.getResourceId(R.styleable.PinEntryView_digitBackground,
                background.resourceId)

        // Text colour, default to android:textColorPrimary from theme
        val textColor = TypedValue()
        theme.resolveAttribute(android.R.attr.textColorPrimary, textColor, true)
        digitTextColor = array.getColor(R.styleable.PinEntryView_digitTextColor,
                if (textColor.resourceId > 0)
                    ContextCompat.getColor(context, textColor.resourceId)
                else
                    textColor.data)

        // Accent colour, default to android:colorAccent from theme
        val accentColor = TypedValue()
        theme.resolveAttribute(R.attr.colorAccent, accentColor, true)
        this.accentColor = array.getColor(R.styleable.PinEntryView_pinAccentColor,
                if (accentColor.resourceId > 0)
                    ContextCompat.getColor(context, textColor.resourceId)
                else
                    accentColor.data)

        // Mask character
        val maskCharacter = array.getString(R.styleable.PinEntryView_mask)
        if (maskCharacter != null) {
            mask = maskCharacter
        }

        // Accent shown, default to only when focused
        accentRequiresFocus = array.getBoolean(R.styleable.PinEntryView_accentRequiresFocus, true)

        // Recycle the typed array
        array.recycle()

        // Add child views
        addViews()
    }

    override fun shouldDelayChildPressedState(): Boolean {
        return false
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        // Calculate the size of the view
        val width = digitWidth * digits + digitSpacing * (digits - 1)
        setMeasuredDimension(
                width + paddingLeft + paddingRight + digitElevation * 2,
                digitHeight + paddingTop + paddingBottom + digitElevation * 2)

        val height = View.MeasureSpec.makeMeasureSpec(measuredHeight, View.MeasureSpec.EXACTLY)

        // Measure children
        for (i in 0 until childCount) {
            getChildAt(i).measure(width, height)
        }
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        // Position the text views
        for (i in 0 until digits) {
            val child = getChildAt(i)
            val left = i * digitWidth + if (i > 0) i * digitSpacing else 0
            child.layout(
                    left + paddingLeft + digitElevation,
                    paddingTop + digitElevation / 2,
                    left + paddingLeft + digitElevation + digitWidth,
                    paddingTop + digitElevation / 2 + digitHeight)
        }

        // Add the edit text as a 1px wide view to allow it to focus
        getChildAt(digits).layout(0, 0, 1, measuredHeight)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_DOWN) {
            // Make sure this view is focused
            editText?.requestFocus()

            // Show keyboard
            val inputMethodManager = context
                    .getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.showSoftInput(editText, 0)
            return true
        }
        return super.onTouchEvent(event)
    }

    override fun onSaveInstanceState(): Parcelable? {
        val parcelable = super.onSaveInstanceState()
        val savedState = SavedState(parcelable!!)
        savedState.editTextValue = editText?.text.toString()
        return savedState
    }

    override fun onRestoreInstanceState(state: Parcelable) {
        val savedState = state as SavedState
        super.onRestoreInstanceState(savedState.superState)
        editText?.setText(savedState.editTextValue)
        editText?.setSelection(savedState.editTextValue!!.length)
    }

    override fun getOnFocusChangeListener(): View.OnFocusChangeListener {
        return onFocusChangeListener!!
    }

    override fun setOnFocusChangeListener(l: View.OnFocusChangeListener) {
        onFocusChangeListener = l
    }

    /**
     * Add a TextWatcher to the EditText
     *
     * @param watcher
     */
    fun addTextChangedListener(watcher: TextWatcher) {
        editText?.addTextChangedListener(watcher)
    }

    /**
     * Remove a TextWatcher from the EditText
     *
     * @param watcher
     */
    fun removeTextChangedListener(watcher: TextWatcher) {
        editText?.removeTextChangedListener(watcher)
    }


    /**
     * Get current pin entered listener
     *
     * @return
     */
    fun getOnPinEnteredListener(): OnPinEnteredListener {
        return onPinEnteredListener!!
    }

    /**
     * Set pin entered listener
     *
     * @param onPinEnteredListener
     */
    fun setOnPinEnteredListener(onPinEnteredListener: OnPinEnteredListener) {
        this.onPinEnteredListener = onPinEnteredListener
    }

    /**
     * Get the {@link Editable} from the EditText
     *
     * @return
     */
    fun getText(): Editable {
        return editText!!.text
    }

    /**
     * Set text to the EditText
     *
     * @param text
     */
    fun setText(text: CharSequence) {
        if (text.length > digits) {
            val otherText = text.subSequence(0, digits)
            editText?.setText(otherText)
            return
        }
        editText?.setText(text)
    }

    /**
     * Clear pin input
     */
    fun clearText() {
        editText?.setText("")
    }

    fun getDigits(): Int {
        return digits
    }

    fun getInputType(): Int {
        return inputType
    }

    fun getAccentType(): Int {
        return accentType
    }

    fun getDigitWidth(): Int {
        return digitWidth
    }

    fun getDigitHeight(): Int {
        return digitHeight
    }

    fun getDigitSpacing(): Int {
        return digitSpacing
    }

    fun getDigitTextSize(): Int {
        return digitTextSize
    }

    fun getAccentWidth(): Int {
        return accentWidth
    }

    fun getDigitElevation(): Int {
        return digitElevation
    }

    fun getDigitBackground(): Int {
        return digitBackground
    }

    fun getDigitTextColor(): Int {
        return digitTextColor
    }

    fun getAccentColor(): Int {
        return accentColor
    }

    fun getMask(): String {
        return mask
    }

    fun getAccentRequiresFocus(): Boolean {
        return accentRequiresFocus
    }

    /**
     * Create views and add them to the view group
     */
    private fun addViews() {
        // Add a digit view for each digit
        for (i in 0 until digits) {
            val digitView = DigitView(context)
            digitView.width = digitWidth
            digitView.height = digitHeight
            digitView.setBackgroundResource(digitBackground)
            digitView.setTextColor(digitTextColor)
            digitView.setTextSize(TypedValue.COMPLEX_UNIT_PX, digitTextSize.toFloat())
            digitView.gravity = Gravity.CENTER
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                digitView.elevation = digitElevation.toFloat()
            }
            addView(digitView)
        }

        // Add an "invisible" edit text to handle input
        editText = EditText(context)
        editText?.setBackgroundColor(ContextCompat.getColor(context, android.R.color.transparent))
        editText?.setTextColor(ContextCompat.getColor(context,android.R.color.transparent))
        editText?.isCursorVisible = false
        editText?.filters = arrayOf<InputFilter>(InputFilter.LengthFilter(digits))
        editText?.inputType = inputType
        editText?.imeOptions = EditorInfo.IME_FLAG_NO_EXTRACT_UI
        editText?.onFocusChangeListener = OnFocusChangeListener { v, hasFocus ->
            // Update the selected state of the views
            val length = editText?.text!!.length
            for (i in 0 until digits) {
                getChildAt(i).isSelected = hasFocus && (accentType == ACCENT_ALL || accentType == ACCENT_CHARACTER && (i == length || i == digits - 1 && length == digits))
            }

            // Make sure the cursor is at the end
            editText?.setSelection(length)

            // Provide focus change events to any listener
            if (onFocusChangeListener != null) {
                onFocusChangeListener?.onFocusChange(this@PinEntryView, hasFocus)
            }
        }
        editText?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable) {
                val length = s.length
                for (i in 0 until digits) {
                    if (s.length > i) {
                        val mask = if (this@PinEntryView.mask.isEmpty())
                            s[i].toString()
                        else
                            this@PinEntryView.mask
                        (getChildAt(i) as TextView).text = mask
                    } else {
                        (getChildAt(i) as TextView).text = ""
                    }
                    if (editText!!.hasFocus()) {
                        getChildAt(i).isSelected = accentType == ACCENT_ALL || accentType == ACCENT_CHARACTER && (i == length || i == digits - 1 && length == digits)
                    }
                }

                if (length == digits && onPinEnteredListener != null) {
                    onPinEnteredListener?.onPinEntered(s.toString())
                }
            }
        })
        addView(editText)
    }

    /**
     * Save state of the view
     */
    internal class SavedState : View.BaseSavedState {
        var editTextValue: String? = null

        constructor(superState: Parcelable) : super(superState)

        private constructor(source: Parcel) : super(source) {
            editTextValue = source.readString()
        }

        override fun writeToParcel(dest: Parcel, flags: Int) {
            super.writeToParcel(dest, flags)
            dest.writeString(editTextValue)
        }

        companion object {

            @SuppressLint("ParcelCreator")
            val CREATOR: Parcelable.Creator<SavedState> = object : Parcelable.Creator<SavedState> {
                override fun createFromParcel(`in`: Parcel): SavedState {
                    return SavedState(`in`)
                }

                override fun newArray(size: Int): Array<SavedState?> {
                    return arrayOfNulls(size)
                }
            }
        }

    }

    /**
     * Custom text view that adds a coloured accent when selected
     */
    private inner class DigitView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0)
        : TextView(context, attrs, defStyleAttr) {

        /**
         * Paint used to draw accent
         */
        private val paint: Paint = Paint()

        init {
            // Setup paint to keep onDraw as lean as possible
            paint.style = Paint.Style.FILL
            paint.color = accentColor
        }

        override fun onDraw(canvas: Canvas) {
            super.onDraw(canvas)

            // If selected draw the accent
            if (isSelected || !accentRequiresFocus) {
                canvas.drawRect(0F, (height - accentWidth).toFloat(), width.toFloat(), height.toFloat(), paint)
            }
        }

    }

    interface OnPinEnteredListener {
        fun onPinEntered(pin: String)
    }
}