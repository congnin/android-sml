package vn.kingbee.widget.toolbarview

import android.annotation.TargetApi
import android.content.Context
import android.content.res.ColorStateList
import android.content.res.Resources
import android.graphics.drawable.Drawable
import android.os.Build
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import vn.kingbee.widget.R

class ActionButton : LinearLayout {
    private var textDisplay: String? = null
    private var textColorResId: Int = 0
    private var iconLeftResId: Int = 0
    private var colorTintResId: Int = 0
    private var backgroundResId: Int = 0
    private var paddingRight = UNUSED_FLOAT_VALUE
    private var paddingLeft = UNUSED_FLOAT_VALUE
    private var drawablePadding = UNUSED_FLOAT_VALUE
    private var minWidth = UNUSED_FLOAT_VALUE
    private var iconMarginTop = UNUSED_FLOAT_VALUE
    lateinit var textView: TextView
    lateinit var iconView: ImageView
    lateinit var actionContainer: View

    constructor(context: Context) : super(context) {
        this.init(context, null)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        this.init(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        this.init(context, attrs)
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes) {
        this.init(context, attrs)
    }

    private fun init(context: Context, attrs: AttributeSet?) {
        this.inflateView(context)
        this.initResource(context, attrs)
        this.setViewData()
        this.isActivated = true
    }

    private fun inflateView(context: Context) {
        LayoutInflater.from(context).inflate(R.layout.mashead_view_action_button, this)
        this.textView = this.findViewById<View>(R.id.tvText) as TextView
        this.iconView = this.findViewById<View>(R.id.imgIcon) as ImageView
        this.actionContainer = this.findViewById(R.id.actionContainer)
    }

    private fun initResource(context: Context, attrs: AttributeSet?) {
        val a = context.obtainStyledAttributes(attrs, R.styleable.ActionButton, UNUSED_RES_ID_VALUE, UNUSED_RES_ID_VALUE)
        this.textDisplay = a.getString(R.styleable.ActionButton_mashead_text)
        this.textColorResId = a.getResourceId(R.styleable.ActionButton_mashead_textColor, UNUSED_RES_ID_VALUE)
        this.iconLeftResId = a.getResourceId(R.styleable.ActionButton_mashead_iconLeft, UNUSED_RES_ID_VALUE)
        this.paddingRight = a.getDimension(R.styleable.ActionButton_mashead_paddingRight, UNUSED_FLOAT_VALUE)
        this.paddingLeft = a.getDimension(R.styleable.ActionButton_mashead_paddingLeft, UNUSED_FLOAT_VALUE)
        this.backgroundResId = a.getResourceId(R.styleable.ActionButton_mashead_background, UNUSED_RES_ID_VALUE)
        this.drawablePadding = a.getDimension(R.styleable.ActionButton_mashead_drawablePadding, UNUSED_FLOAT_VALUE)
        this.minWidth = a.getDimension(R.styleable.ActionButton_mashead_mindWidth, UNUSED_FLOAT_VALUE)
        this.colorTintResId = a.getResourceId(R.styleable.ActionButton_mashead_tint, UNUSED_RES_ID_VALUE)
        this.iconMarginTop = a.getDimension(R.styleable.ActionButton_mashead_iconMarginTop, UNUSED_FLOAT_VALUE)
        a.recycle()
    }

    private fun setViewData() {
        this.textView.text = this.textDisplay
        if (this.textColorResId != UNUSED_RES_ID_VALUE) {
            val colorStateList = this.getColorStateList(this.textColorResId)
            if (colorStateList != null) {
                this.textView.setTextColor(colorStateList)
            }
        }

        if (this.paddingRight > UNUSED_FLOAT_VALUE) {
            this.actionContainer.setPadding(this.actionContainer.paddingLeft, 0, this.paddingRight.toInt(), 0)
        }

        if (this.paddingLeft > UNUSED_FLOAT_VALUE) {
            this.actionContainer.setPadding(this.paddingLeft.toInt(), 0, this.actionContainer.paddingRight, 0)
        }

        if (this.backgroundResId != UNUSED_RES_ID_VALUE) {
            this.actionContainer.setBackgroundResource(this.backgroundResId)
        }

        if (this.minWidth > UNUSED_FLOAT_VALUE) {
            this.actionContainer.minimumWidth = this.minWidth.toInt()
        }

        this.initIconLeftParams()
    }

    private fun initIconLeftParams() {
        this.iconView.setImageResource(this.iconLeftResId)
        if (this.colorTintResId != UNUSED_RES_ID_VALUE) {
            val colorStateList = this.getColorStateList(this.colorTintResId)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                this.iconView.imageTintList = colorStateList
            }
        }

        if (this.drawablePadding > UNUSED_FLOAT_VALUE) {
            this.updateIconMarginRight(this.drawablePadding.toInt())
        }

        if (this.iconMarginTop > UNUSED_FLOAT_VALUE) {
            this.updateIconMarginTop(this.iconMarginTop.toInt())
        }

    }

    private fun getColorStateList(colorStateList: Int): ColorStateList? {
        try {
            return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                this.resources.getColorStateList(colorStateList, null as Resources.Theme?)
            else ContextCompat.getColorStateList(context, colorStateList)
        } catch (e: Resources.NotFoundException) {
            throw TopMasHeadException("Init top mashead error: getColorStateList with exception " + e.message)
        }

    }

    fun setText(exit: String) {
        this.textView.text = exit
    }

    fun setDrawableLeft(drawableLeft: Drawable) {
        this.iconView.setImageDrawable(drawableLeft)
    }

    fun setDrawableLeft(@DrawableRes drawableResId: Int) {
        this.iconLeftResId = drawableResId
        this.iconView.setImageResource(this.iconLeftResId)
    }

    fun setDrawablePadding(drawablePadding: Int) {
        this.drawablePadding = drawablePadding.toFloat()
        this.updateIconMarginRight(drawablePadding)
    }

    private fun updateIconMarginRight(rightMargin: Int) {
        val params = this.iconView.layoutParams as LayoutParams
        params.rightMargin = rightMargin
    }

    private fun updateIconMarginTop(marginTop: Int) {
        val params = this.iconView.layoutParams as LayoutParams
        params.topMargin = marginTop
    }

    companion object {
        private const val UNUSED_FLOAT_VALUE = -1.0f
        private const val UNUSED_RES_ID_VALUE = 0
    }
}