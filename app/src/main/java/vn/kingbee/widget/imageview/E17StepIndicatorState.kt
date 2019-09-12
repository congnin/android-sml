package vn.kingbee.widget.imageview

enum class E17StepIndicatorState private constructor(val id: Int) {
    IDENTITY(0),
    FINGERPRINTS(1),
    CELLPHONE_NUMBER(2),
    VERIFY(3),
    ADDRESS(4),
    SECURITY(5),
    CARD(6);


    companion object {

        fun getTypeFromId(id: Int): E17StepIndicatorState? {
            for (c in E17StepIndicatorState.values()) {
                if (c.id == id)
                    return c
            }
            return null
        }
    }
}