package vn.kingbee.widget.notification

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import vn.kingbee.widget.R

class NotificationOneLineViewHolder : OverlayWindowView.OverlayViewHolder<NotificationManagerImpl.NotificationData>, View.OnClickListener {
    internal var notificationView: OverlayWindowView<NotificationManagerImpl.NotificationData>? = null

    private var tvMessage: TextView? = null
    private var imgIcon: ImageView? = null
    private var notificationCallback: OverlayWindowView.NotificationCallback? = null

    override val layoutId: Int
        get() = TODO("not implemented") //To change initializer of created properties use File | Settings | File Templates.

    override fun initView(view: View) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun updateData(data: NotificationManagerImpl.NotificationData?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun setCallback(callback: OverlayWindowView.NotificationCallback?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun setNotificationView(notificationView: OverlayWindowView<NotificationManagerImpl.NotificationData>) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onClick(v: View?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}