package vn.kingbee.widget.notification

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import vn.kingbee.widget.R

class NotificationMultiLineViewHolder : OverlayWindowView.OverlayViewHolder<NotificationManagerImpl.NotificationData>,
    View.OnClickListener {
    private var notificationView: OverlayWindowView<NotificationManagerImpl.NotificationData>? = null

    lateinit var tvMessage: TextView
    lateinit var imgIcon: ImageView
    private var notificationCallback: OverlayWindowView.NotificationCallback? = null

    override val layoutId: Int
        get() = R.layout.view_notification_multi_line

    override fun initView(view: View) {
        tvMessage = view.findViewById(R.id.tv_line_message)
        imgIcon = view.findViewById(R.id.img_notify_icon)
        val notifyDismiss = view.findViewById<TextView>(R.id.tv_notify_dismiss)
        val notifyAdd = view.findViewById<TextView>(R.id.tv_notify_add)

        notifyDismiss.setOnClickListener(this)
        notifyAdd.setOnClickListener(this)
    }

    override fun updateData(data: NotificationManagerImpl.NotificationData?) {
        if (data != null) {
            tvMessage.text = data.message
            imgIcon.setImageResource(data.resIcon)
        }
    }

    override fun setCallback(callback: OverlayWindowView.NotificationCallback?) {
        notificationCallback = callback
    }

    override fun setNotificationView(notificationView: OverlayWindowView<NotificationManagerImpl.NotificationData>) {
        this.notificationView = notificationView
    }

    override fun onClick(v: View?) {
        if (notificationCallback != null && v != null) {
            notificationCallback?.onViewClicked(v)
        }
        if (notificationView != null && v != null && v.id == R.id.tv_notify_dismiss) {
            notificationView?.dismiss()
        }
    }
}