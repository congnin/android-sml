package vn.kingbee.widget.edittext.material

import android.content.Context
import vn.kingbee.widget.edittext.material.base.BaseMaterialEditText
import com.google.android.material.textfield.TextInputLayout
import android.util.AttributeSet
import android.view.View
import android.widget.EditText
import vn.kingbee.widget.R
import vn.kingbee.widget.keyboard.NumpadKeyboardEditText
import vn.kingbee.widget.keyboard.NumpadKeyboardType

class MaterialEditText : BaseMaterialEditText {

    private var mTextInputLayout: TextInputLayout? = null
    private var mEditText: NumpadKeyboardEditText? = null

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    override fun initView(view: View) {
        mEditText = view.findViewById(R.id.editText)
        mTextInputLayout = view.findViewById(R.id.textInputLayout)
    }

    override fun getLayout(): Int = R.layout.view_material_edittext_layout

    public override fun getEditText(): EditText = mEditText!!

    override fun getTextInputLayout(): TextInputLayout = mTextInputLayout!!

    override fun getText(): String = mEditText!!.text.toString().trim()

    fun trim() {
        mEditText?.setText(getText())
    }

    override fun setText(text: String) {
        super.setText(text)
        mEditText?.setSelection(getText().length)
    }

    fun setNumpadType(numpadType: NumpadKeyboardType) {
        mEditText?.setNumpadType(numpadType)
    }
}