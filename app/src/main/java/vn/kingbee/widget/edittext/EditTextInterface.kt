package vn.kingbee.widget.edittext

interface EditTextInterface {
    fun appendCharacter(ch: CharSequence)

    fun clear()

    fun getValue(): String

    fun setValue(value: String)
}