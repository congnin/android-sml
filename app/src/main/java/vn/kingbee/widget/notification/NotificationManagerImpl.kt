package vn.kingbee.widget.notification

import android.support.annotation.DrawableRes

class NotificationManagerImpl {

    class NotificationData {
        @DrawableRes
        var resIcon: Int = 0
        var message: String? = null // NOSONAR -> just want access this field public
    }
}