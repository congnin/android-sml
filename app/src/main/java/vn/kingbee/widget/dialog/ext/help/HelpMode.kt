package vn.kingbee.widget.dialog.ext.help

enum class HelpMode private constructor(name: String) {
    DEFAULT_SCREEN("default_screen"),
    HOME_SCREEN("home_screen"),
    LOGIN_SCREEN("login_screen"),
    LOGIN_ID_CELLPHONE_SCREEN("login_id_cellphone_screen"),
    LOGIN_FINGER_SCREEN("login_finger_screen");


    companion object {

        fun getScreenFromName(name: String): HelpMode {
            for (c in HelpMode.values()) {
                if (c.name.equals(name, ignoreCase = true))
                    return c
            }
            return DEFAULT_SCREEN
        }
    }
}