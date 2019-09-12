package vn.kingbee.widget.pin.square

import android.content.Context
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.util.TypedValue
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView
import vn.kingbee.widget.R

class SquarePinItem : FrameLayout, PinItem {

    private val FONTS_ROBOTO_REGULAR_TTF = "fonts/Roboto_Medium.ttf"
    private var mTvPinNumber: TextView? = null
    private var mMain: FrameLayout? = null
    private var mDefaultColor: Drawable? = null
    private var mInactiveColor: Drawable? = null
    private var mActiveColor: Drawable? = null
    private var mErrorColor: Drawable? = null
    private var mTextBackgroundColor: Drawable? = null
    private var mCurrentHeight: Int = 0
    private var mCurrentWidth: Int = 0
    private var mCircleSize: Int = 0

    constructor(context: Context) : super(context) {
        init()
    }

    private fun init() {
        val inflater = context
            .getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        inflater.inflate(R.layout.view_pin_item_layout, this, true)
        mMain = findViewById(R.id.ln_in_contain)
        mTvPinNumber = findViewById(R.id.tv_pin_number)
        mTvPinNumber!!.includeFontPadding = false
        mTvPinNumber!!.setPadding(0, 0, 0, 0)
        mTvPinNumber!!.typeface = Typeface.createFromAsset(context.assets, FONTS_ROBOTO_REGULAR_TTF)
        mTvPinNumber!!.gravity = Gravity.CENTER
        mCurrentHeight = mTvPinNumber!!.layoutParams.height
        mCurrentWidth = mTvPinNumber!!.layoutParams.width
    }

    override fun getPinItem(): View {
        return this
    }

    override fun setLayout(layoutParams: LinearLayout.LayoutParams) {
        this.layoutParams = layoutParams
    }

    override fun setTextBackgroundColor(textBackgroundColor: Drawable) {
        this.mTextBackgroundColor = textBackgroundColor
    }

    override fun setBackgroundDefault(color: Drawable) {
        mDefaultColor = color
    }

    override fun setBackground(color: Drawable) {
        mInactiveColor = color
    }

    override fun setFocusBackground(color: Drawable) {
        mActiveColor = color
    }

    override fun setErrorColor(color: Drawable) {
        mErrorColor = color
    }

    override fun setCircleSize(circleSize: Int) {
        mCircleSize = circleSize
    }

    override fun setTextSize(size: Int) {
        mTvPinNumber!!.setTextSize(TypedValue.COMPLEX_UNIT_PX, size.toFloat())
    }

    override fun setTextColor(color: Int) {
        mTvPinNumber!!.setTextColor(color)
    }

    override fun setElevation(elevation: Int) {
        //No implement
    }

    override fun setText(text: String) {
        setCircleSize(mCurrentWidth, mCurrentHeight)
        mTvPinNumber!!.text = text
    }

    override fun activeEmptyFocus() {
        setCircleSizeAndEmptyText()
        mTvPinNumber!!.setBackgroundDrawable(null)
        mMain!!.setBackgroundDrawable(mActiveColor)
    }

    override fun setErrorStatus() {
        enableInputText()
        mTvPinNumber!!.text = ""
        mTvPinNumber!!.setBackgroundDrawable(null)
        mMain!!.setBackgroundDrawable(mErrorColor)
    }

    override fun inActiveFillFocus() {
        enableInputText()
        setCircleSizeAndEmptyText()
        mTvPinNumber!!.setBackgroundDrawable(mTextBackgroundColor)
        mMain!!.setBackgroundDrawable(mInactiveColor)
    }

    override fun setDefaultStatus() {
        enableInputText()
        mTvPinNumber!!.text = ""
        mTvPinNumber!!.setBackgroundDrawable(null)
        mMain!!.setBackgroundDrawable(mDefaultColor)
    }

    private fun setCircleSize(width: Int, height: Int) {
        val params = mTvPinNumber!!.layoutParams
        params.width = width
        params.height = height
        mTvPinNumber!!.layoutParams = params
    }

    private fun setCircleSizeAndEmptyText() {
        setCircleSize(mCircleSize, mCircleSize)
        mTvPinNumber!!.text = ""
    }

    private fun enableInputText() {
        mTvPinNumber!!.isEnabled = true
    }
}