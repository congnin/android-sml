package vn.kingbee.widget.edittext.state

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.EditText
import android.widget.TextView
import vn.kingbee.widget.R

class StateEditText : EditText {
    companion object {
        private val STATE_ERROR = intArrayOf(R.attr.state_error)
    }

    private var isError = false

    constructor(context: Context): super(context)

    constructor(context: Context, attrs: AttributeSet): super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int): super(context, attrs, defStyleAttr)

    override fun onCreateDrawableState(extraSpace: Int): IntArray {
        val drawableState = super.onCreateDrawableState(extraSpace + 1)
        if (this.isError) {
            View.mergeDrawableStates(drawableState, STATE_ERROR)
        }
        return drawableState
    }

    fun isError(): Boolean {
        return isError
    }

    fun setError(error: Boolean) {
        isError = error
        refreshDrawableState()
    }

    override fun setText(text: CharSequence, type: TextView.BufferType) {
        super.setText(text, type)
        setSelection(getText().length)
    }
}