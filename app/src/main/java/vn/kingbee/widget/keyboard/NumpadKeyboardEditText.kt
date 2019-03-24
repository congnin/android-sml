package vn.kingbee.widget.keyboard

import android.content.Context
import android.util.AttributeSet
import vn.kingbee.widget.edittext.prefixed.PrefixedEditText

class NumpadKeyboardEditText : PrefixedEditText {
    private var mNumpadType: NumpadKeyboardType? = null
    private var mShouldEnableCopy: Boolean? = false

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle)

    fun getNumpadType(): NumpadKeyboardType {
        return mNumpadType!!
    }

    fun setNumpadType(numpadType: NumpadKeyboardType) {
        mNumpadType = numpadType
    }

    //Provide an interface to enable copy & paste feature
    fun setShouldEnableCopy(canCopy: Boolean?) {
        this.isLongClickable = canCopy!!
        mShouldEnableCopy = canCopy
    }

    /**
     * The following disable copy-paste functionality in numpad keyboard edit texts *
     */
    override fun onTextContextMenuItem(id: Int): Boolean {
        if ((!mShouldEnableCopy!!)) {
            when (id) {
                android.R.id.cut, android.R.id.paste, android.R.id.copy -> return false
            }
        } else {
            super.onTextContextMenuItem(id)
        }
        return true
    }
}