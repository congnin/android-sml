package vn.kingbee.widget.dialog.ext.help;

public enum HelpMode {
    DEFAULT_SCREEN("default_screen"),
    HOME_SCREEN("home_screen"),
    LOGIN_SCREEN("login_screen"),
    LOGIN_ID_CELLPHONE_SCREEN("login_id_cellphone_screen"),
    LOGIN_FINGER_SCREEN("login_finger_screen");
    private final String mName;

    HelpMode(String name) {
        mName = name;
    }

    public static HelpMode getScreenFromName(String name) {
        for (HelpMode c : HelpMode.values()) {
            if (c.getName().equalsIgnoreCase(name))
                return c;
        }
        return DEFAULT_SCREEN;
    }

    public String getName() {
        return mName;
    }
}
