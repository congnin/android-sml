package vn.kingbee.widget.dialog

enum class ButtonType {
    POSITIVE(0), NEGATIVE(1), NEUTRAL(2), CLOSE(3);

    private val mId: Int

    constructor(id: Int) {
        this.mId = id
    }

    fun getId(): Int {
        return mId
    }
}