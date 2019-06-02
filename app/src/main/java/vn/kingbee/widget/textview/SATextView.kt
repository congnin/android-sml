package vn.kingbee.widget.textview

import android.content.Context
import android.graphics.Typeface
import androidx.appcompat.widget.AppCompatTextView
import android.util.AttributeSet
import android.view.View
import vn.kingbee.widget.R
import vn.kingbee.utils.CommonUtils

class SATextView : AppCompatTextView, View.OnClickListener {

    private var onClickListener: View.OnClickListener? = null

    private val mTypeFaceList = arrayOf(
        "Aachen_Bold.ttf",
        "helvetica_neue_medium_italic.ttf",
        "HelveticaNeue.ttf",
        "HelveticaNeue_Italic.otf",
        "HelveticaNeue_LightItalic.ttf",
        "HelveticaNeue_UltraLight.otf",
        "HelveticaNeue_Light.ttf",
        "Roboto_Regular.ttf",
        "Roboto_Medium.ttf",
        "Roboto_Light.ttf",
        "Roboto_Bold.ttf"
    )

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(attrs)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init(attrs)
    }

    private fun init(attrs: AttributeSet) {
        val fontIndex: Int
        val ta = context.obtainStyledAttributes(attrs, R.styleable.SATextView, 0, 0)
        try {
            fontIndex = ta.getInteger(R.styleable.SATextView_typeFace, -1)
        } finally {
            ta.recycle()
        }

        if (fontIndex != -1) {
            val typeFace =
                Typeface.createFromAsset(context.assets, "fonts/" + mTypeFaceList[fontIndex])
            typeface = typeFace
        }
    }

    override fun onClick(v: View) {
        if (CommonUtils.isClickAvailable() && onClickListener != null) {
            onClickListener!!.onClick(v)
        }
    }

    override fun setOnClickListener(onClickListener: View.OnClickListener?) {
        this.onClickListener = onClickListener
        super.setOnClickListener(this)
    }
}
