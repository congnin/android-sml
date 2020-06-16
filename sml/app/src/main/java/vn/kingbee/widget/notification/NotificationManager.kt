package vn.kingbee.widget.notification

import vn.kingbee.widget.notification.enums.NotificationType
import vn.kingbee.widget.notification.enums.NotificationViewType

/**
 * Notification manager with custom notification data T
 *
 * @param <T> generic Notification data for bind view
</T> */
interface NotificationManager<T> {
    /**
     * Show notification with msg and notification type [NotificationType]
     *
     * @param msg  raw message
     * @param type [NotificationType]
     */
    fun show(msg: String, type: NotificationType)

    /**
     * Show notification with msg and notification type [NotificationType] and
     * type of view [NotificationViewType]
     *
     * @param msg      raw message
     * @param type     [NotificationType]
     * @param viewType [NotificationViewType]
     */
    fun show(msg: String, type: NotificationType, viewType: NotificationViewType)

    /**
     * Show notification with msg, notification type [NotificationType], and view clicked callback [OverlayWindowView.NotificationCallback]
     *
     * @param msg      raw message
     * @param type     [NotificationType]
     * @param callback [OverlayWindowView.NotificationCallback]
     */
    fun show(msg: String, type: NotificationType, callback: OverlayWindowView.NotificationCallback?)

    /**
     * Show notification with msg, notification type [NotificationType], and view clicked callback [OverlayWindowView.NotificationCallback]
     * and type of view [NotificationViewType]
     *
     * @param msg      raw message
     * @param type     [NotificationType]
     * @param viewType [NotificationViewType]
     * @param callback [OverlayWindowView.NotificationCallback]
     */
    fun show(msg: String, type: NotificationType, viewType: NotificationViewType, callback: OverlayWindowView.NotificationCallback?)


    /**
     * Show toast with custom [<]
     *
     * @param customViewHolder
     */
    fun show(data: T, customViewHolder: OverlayWindowView.OverlayViewHolder<T>)

    /**
     * Involve this method to hide latest notification view
     */
    fun hide()
}