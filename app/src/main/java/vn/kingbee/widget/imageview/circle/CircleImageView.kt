package vn.kingbee.widget.imageview.circle

import android.content.Context
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import android.util.AttributeSet
import android.widget.ImageView
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import vn.kingbee.widget.R

open class CircleImageView : ImageView {
    private val mDrawableRect: RectF
    private val mBorderRect: RectF
    private val mShaderMatrix: Matrix
    private val mBitmapPaint: Paint?
    private val mBorderPaint: Paint
    private val mFillPaint: Paint
    private var mBorderColor: Int = 0
    private var mBorderWidth: Int = 0
    private var mFillColor: Int = 0
    private var mBitmap: Bitmap? = null
    private var mBitmapShader: BitmapShader? = null
    private var mBitmapWidth: Int = 0
    private var mBitmapHeight: Int = 0
    private var mDrawableRadius: Float = 0.toFloat()
    private var mBorderRadius: Float = 0.toFloat()
    private var mColorFilter: ColorFilter? = null
    private var mReady: Boolean = false
    private var mSetupPending: Boolean = false
    private var mBorderOverlay: Boolean = false
    var isDisableCircularTransformation: Boolean = false
        set(disableCircularTransformation) {
            if (this.isDisableCircularTransformation != disableCircularTransformation) {
                field = disableCircularTransformation
                this.initializeBitmap()
            }
        }

    var borderColor: Int
        get() = this.mBorderColor
        set(@ColorInt borderColor) {
            if (borderColor != this.mBorderColor) {
                this.mBorderColor = borderColor
                this.mBorderPaint.color = this.mBorderColor
                this.invalidate()
            }
        }


    var fillColor: Int
        @Deprecated("")
        get() = this.mFillColor
        @Deprecated("")
        set(@ColorInt fillColor) {
            if (fillColor != this.mFillColor) {
                this.mFillColor = fillColor
                this.mFillPaint.color = fillColor
                this.invalidate()
            }
        }

    var borderWidth: Int
        get() = this.mBorderWidth
        set(borderWidth) {
            if (borderWidth != this.mBorderWidth) {
                this.mBorderWidth = borderWidth
                this.setup()
            }
        }

    var isBorderOverlay: Boolean
        get() = this.mBorderOverlay
        set(borderOverlay) {
            if (borderOverlay != this.mBorderOverlay) {
                this.mBorderOverlay = borderOverlay
                this.setup()
            }
        }

    constructor(context: Context) : super(context) {
        this.mDrawableRect = RectF()
        this.mBorderRect = RectF()
        this.mShaderMatrix = Matrix()
        this.mBitmapPaint = Paint()
        this.mBorderPaint = Paint()
        this.mFillPaint = Paint()
        this.mBorderColor = DEFAULT_BORDER_COLOR
        this.mBorderWidth = DEFAULT_BORDER_WIDTH
        this.mFillColor = DEFAULT_FILL_COLOR
        this.init()
    }

    @JvmOverloads
    constructor(context: Context, attrs: AttributeSet, defStyle: Int = 0) : super(context, attrs, defStyle) {
        this.mDrawableRect = RectF()
        this.mBorderRect = RectF()
        this.mShaderMatrix = Matrix()
        this.mBitmapPaint = Paint()
        this.mBorderPaint = Paint()
        this.mFillPaint = Paint()
        this.mBorderColor = DEFAULT_BORDER_COLOR
        this.mBorderWidth = DEFAULT_BORDER_WIDTH
        this.mFillColor = DEFAULT_FILL_COLOR
        val a = context.obtainStyledAttributes(attrs, R.styleable.CircleImageView, defStyle, 0)
        this.mBorderWidth = a.getDimensionPixelSize(R.styleable.CircleImageView_civ_border_width, DEFAULT_BORDER_WIDTH)
        this.mBorderColor = a.getColor(R.styleable.CircleImageView_civ_border_color, DEFAULT_BORDER_COLOR)
        this.mBorderOverlay = a.getBoolean(R.styleable.CircleImageView_civ_border_overlay, DEFAULT_BORDER_OVERLAY)
        this.mFillColor = a.getColor(R.styleable.CircleImageView_civ_fill_color, DEFAULT_FILL_COLOR)
        a.recycle()
        this.init()
    }

    private fun init() {
        super.setScaleType(SCALE_TYPE)
        this.mReady = true
        if (this.mSetupPending) {
            this.setup()
            this.mSetupPending = false
        }
    }

    override fun getScaleType(): ScaleType {
        return SCALE_TYPE
    }

    override fun setScaleType(scaleType: ScaleType) {
        require(scaleType == SCALE_TYPE) { String.format("ScaleType %s not supported.", scaleType) }
    }

    override fun setAdjustViewBounds(adjustViewBounds: Boolean) {
        require(!adjustViewBounds) { "adjustViewBounds not supported." }
    }

    override fun onDraw(canvas: Canvas) {
        if (this.isDisableCircularTransformation) {
            super.onDraw(canvas)
        } else if (this.mBitmap != null) {
            if (this.mFillColor != DEFAULT_FILL_COLOR) {
                canvas.drawCircle(this.mDrawableRect.centerX(), this.mDrawableRect.centerY(), this.mDrawableRadius, this.mFillPaint)
            }

            canvas.drawCircle(this.mDrawableRect.centerX(), this.mDrawableRect.centerY(), this.mDrawableRadius, this.mBitmapPaint!!)
            if (this.mBorderWidth > DEFAULT_BORDER_WIDTH) {
                canvas.drawCircle(this.mBorderRect.centerX(), this.mBorderRect.centerY(), this.mBorderRadius, this.mBorderPaint)
            }
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        this.setup()
    }

    override fun setPadding(left: Int, top: Int, right: Int, bottom: Int) {
        super.setPadding(left, top, right, bottom)
        this.setup()
    }

    override fun setPaddingRelative(start: Int, top: Int, end: Int, bottom: Int) {
        super.setPaddingRelative(start, top, end, bottom)
        this.setup()
    }

    @Deprecated("")
    fun setBorderColorResource(@ColorRes borderColorRes: Int) {
        this.borderColor = this.context.resources.getColor(borderColorRes)
    }

    @Deprecated("")
    fun setFillColorResource(@ColorRes fillColorRes: Int) {
        this.fillColor = this.context.resources.getColor(fillColorRes)
    }

    override fun setImageBitmap(bm: Bitmap) {
        super.setImageBitmap(bm)
        this.initializeBitmap()
    }

    override fun setImageDrawable(drawable: Drawable?) {
        super.setImageDrawable(drawable)
        this.initializeBitmap()
    }

    override fun setImageResource(@DrawableRes resId: Int) {
        super.setImageResource(resId)
        this.initializeBitmap()
    }

    override fun setImageURI(uri: Uri?) {
        super.setImageURI(uri)
        this.initializeBitmap()
    }

    override fun setColorFilter(cf: ColorFilter) {
        if (cf !== this.mColorFilter) {
            this.mColorFilter = cf
            this.applyColorFilter()
            this.invalidate()
        }
    }

    override fun getColorFilter(): ColorFilter? {
        return this.mColorFilter
    }

    private fun applyColorFilter() {
        if (this.mBitmapPaint != null) {
            this.mBitmapPaint.colorFilter = this.mColorFilter
        }
    }

    private fun getBitmapFromDrawable(drawable: Drawable?): Bitmap? {
        return if (drawable == null) {
            null
        } else if (drawable is BitmapDrawable) {
            drawable.bitmap
        } else {
            try {
                val bitmap: Bitmap
                if (drawable is ColorDrawable) {
                    bitmap = Bitmap.createBitmap(COLORDRAWABLE_DIMENSION, COLORDRAWABLE_DIMENSION, BITMAP_CONFIG)
                } else {
                    bitmap = Bitmap.createBitmap(drawable.intrinsicWidth, drawable.intrinsicHeight, BITMAP_CONFIG)
                }

                val canvas = Canvas(bitmap)
                drawable.setBounds(0, 0, canvas.width, canvas.height)
                drawable.draw(canvas)
                bitmap
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }

        }
    }

    private fun initializeBitmap() {
        if (this.isDisableCircularTransformation) {
            this.mBitmap = null
        } else {
            this.mBitmap = this.getBitmapFromDrawable(this.drawable)
        }

        this.setup()
    }

    private fun setup() {
        if (!this.mReady) {
            this.mSetupPending = true
        } else if (this.width != 0 || this.height != 0) {
            if (this.mBitmap == null) {
                this.invalidate()
            } else {
                this.mBitmapShader = BitmapShader(this.mBitmap!!, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)
                this.mBitmapPaint!!.isAntiAlias = true
                this.mBitmapPaint.shader = this.mBitmapShader
                this.mBorderPaint.style = Paint.Style.STROKE
                this.mBorderPaint.isAntiAlias = true
                this.mBorderPaint.color = this.mBorderColor
                this.mBorderPaint.strokeWidth = this.mBorderWidth.toFloat()
                this.mFillPaint.style = Paint.Style.FILL
                this.mFillPaint.isAntiAlias = true
                this.mFillPaint.color = this.mFillColor
                this.mBitmapHeight = this.mBitmap!!.height
                this.mBitmapWidth = this.mBitmap!!.width
                this.mBorderRect.set(this.calculateBounds())
                this.mBorderRadius = Math.min((this.mBorderRect.height() - this.mBorderWidth.toFloat()) / 2.0f, (this.mBorderRect.width() - this.mBorderWidth.toFloat()) / 2.0f)
                this.mDrawableRect.set(this.mBorderRect)
                if (!this.mBorderOverlay && this.mBorderWidth > 0) {
                    this.mDrawableRect.inset(this.mBorderWidth.toFloat() - 1.0f, this.mBorderWidth.toFloat() - 1.0f)
                }

                this.mDrawableRadius = Math.min(this.mDrawableRect.height() / 2.0f, this.mDrawableRect.width() / 2.0f)
                this.applyColorFilter()
                this.updateShaderMatrix()
                this.invalidate()
            }
        }
    }

    private fun calculateBounds(): RectF {
        val availableWidth = this.width - this.paddingLeft - this.paddingRight
        val availableHeight = this.height - this.paddingTop - this.paddingBottom
        val sideLength = Math.min(availableWidth, availableHeight)
        val left = this.paddingLeft.toFloat() + (availableWidth - sideLength).toFloat() / 2.0f
        val top = this.paddingTop.toFloat() + (availableHeight - sideLength).toFloat() / 2.0f
        return RectF(left, top, left + sideLength.toFloat(), top + sideLength.toFloat())
    }

    private fun updateShaderMatrix() {
        var dx = 0.0f
        var dy = 0.0f
        this.mShaderMatrix.set(null as Matrix?)
        val scale: Float
        if (this.mBitmapWidth.toFloat() * this.mDrawableRect.height() > this.mDrawableRect.width() * this.mBitmapHeight.toFloat()) {
            scale = this.mDrawableRect.height() / this.mBitmapHeight.toFloat()
            dx = (this.mDrawableRect.width() - this.mBitmapWidth.toFloat() * scale) * 0.5f
        } else {
            scale = this.mDrawableRect.width() / this.mBitmapWidth.toFloat()
            dy = (this.mDrawableRect.height() - this.mBitmapHeight.toFloat() * scale) * 0.5f
        }

        this.mShaderMatrix.setScale(scale, scale)
        this.mShaderMatrix.postTranslate((dx + 0.5f).toInt().toFloat() + this.mDrawableRect.left, (dy + 0.5f).toInt().toFloat() + this.mDrawableRect.top)
        this.mBitmapShader!!.setLocalMatrix(this.mShaderMatrix)
    }

    companion object {
        private val SCALE_TYPE: ScaleType = ScaleType.CENTER_CROP
        private val BITMAP_CONFIG: Bitmap.Config = Bitmap.Config.ARGB_8888
        private const val COLORDRAWABLE_DIMENSION = 2
        private const val DEFAULT_BORDER_WIDTH = 0
        private const val DEFAULT_BORDER_COLOR = -16777216
        private const val DEFAULT_FILL_COLOR = 0
        private const val DEFAULT_BORDER_OVERLAY = false

    }
}