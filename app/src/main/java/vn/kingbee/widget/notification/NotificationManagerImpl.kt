package vn.kingbee.widget.notification

import android.os.Handler
import androidx.annotation.DrawableRes
import android.view.View
import android.view.Window
import vn.kingbee.widget.R
import vn.kingbee.widget.notification.enums.NotificationInstanceType
import vn.kingbee.widget.notification.enums.NotificationType
import vn.kingbee.widget.notification.enums.NotificationViewType

class NotificationManagerImpl : NotificationManager<NotificationManagerImpl.NotificationData>,
    View.OnClickListener {

    protected var builder: OverlayWindowView.Builder<NotificationData>
    protected var notificationView: OverlayWindowView<NotificationData>? = null
    protected var notificationData: NotificationData

    constructor(window: Window) {
        this.builder = this.getDefaultBuilder(window)
        notificationData = NotificationData()
    }

    protected fun getDefaultBuilder(window: Window): OverlayWindowView.Builder<NotificationData> {
        return OverlayWindowView.Builder<NotificationData>(window)
            .withData(notificationData)
            .withMarginTop(window.context.resources.getDimension(R.dimen.default_margin_huge).toInt())
            .withViewHolder(getNotificationViewHolder(NotificationViewType.ONE_LINE))
    }

    protected fun getNotificationViewHolder(viewType: NotificationViewType)
            : OverlayWindowView.OverlayViewHolder<NotificationData> {
        return when (viewType) {
            NotificationViewType.ONE_LINE -> NotificationOneLineViewHolder()
            NotificationViewType.MULTI_LINE_WITH_MORE_ACTION -> NotificationMultiLineViewHolder()
        }
    }

    /**
     * Show notification with NotificationInstanceType
     *
     * @param msg  message to show
     * @param mode {@link NotificationInstanceType}
     *             - UPDATE_DATA: Show notification with update data for current notification view if any, otherwise create a new one
     *             - NEW_INSTANCE: Show notification with new instance
     *             - SINGLE_INSTANCE: Show new notification and dismiss other if any
     */
    fun showNotification(msg: String, mode: NotificationInstanceType, viewType: NotificationViewType) {
        notificationData.message = msg
        when (mode) {
            NotificationInstanceType.UPDATE_DATA -> showWithUpdateData(msg)
            NotificationInstanceType.NEW_INSTANCE -> showWithNewInstance(msg, viewType)
            NotificationInstanceType.SINGLE_INSTANCE -> showWithSingleInstance(notificationData,
                getNotificationViewHolder(viewType), null)
        }
    }

    /**
     * Show notification with update data for current notification view if any, otherwise create a new one
     *
     * @param msg to show
     */
    fun showWithUpdateData(msg: String) {
        notificationData.message = msg
        if (notificationView == null) {
            notificationView = builder.withData(notificationData).show()
        } else {
            notificationView?.onUpdateData(notificationData)
        }
    }

    /**
     * Show new notification and dismiss other if any
     *
     * @param notificationData
     * @param callback
     */
    fun showWithSingleInstance(notificationData: NotificationData,
                               viewHolder: OverlayWindowView.OverlayViewHolder<NotificationData>,
                               callback: OverlayWindowView.NotificationCallback?) {
        if (notificationView != null) {
            notificationView?.dismiss()
        }
        notificationView = builder.withData(notificationData)
            .withViewHolder(viewHolder)
            .withCallback(callback)
            .show()
    }

    /**
     * Show notification with new instance
     *
     * @param msg
     */
    fun showWithNewInstance(msg: String, viewType: NotificationViewType) {
        notificationData.message = msg
        notificationView = builder.withData(notificationData)
            .withViewHolder(getNotificationViewHolder(viewType))
            .show()
    }

    override fun show(msg: String, type: NotificationType) {
        show(msg, type, NotificationViewType.ONE_LINE, null)
    }

    override fun show(msg: String, type: NotificationType, viewType: NotificationViewType) {
        show(msg, type, viewType, null)
    }

    override fun show(msg: String, type: NotificationType, callback: OverlayWindowView.NotificationCallback?) {
        handler.post {
            kotlin.run {
                when (type) {
                    NotificationType.GOOD -> notificationData.resIcon = R.drawable.ic_check_circle_good
                    NotificationType.ERROR -> notificationData.resIcon = R.mipmap.ic_circle_error
                    NotificationType.INFO -> notificationData.resIcon = R.drawable.ic_circle_info
                }
                notificationData.message = msg
                showWithSingleInstance(notificationData,
                    getNotificationViewHolder(NotificationViewType.ONE_LINE), callback)

            }
        }
    }

    override fun show(msg: String, type: NotificationType, viewType: NotificationViewType, callback: OverlayWindowView.NotificationCallback?) {
        handler.post {
            kotlin.run {
                when (type) {
                    NotificationType.GOOD -> notificationData.resIcon = R.drawable.ic_check_circle_good
                    NotificationType.ERROR -> notificationData.resIcon = R.mipmap.ic_circle_error
                    NotificationType.INFO -> notificationData.resIcon = R.drawable.ic_circle_info
                }
                notificationData.message = msg
                showWithSingleInstance(notificationData, getNotificationViewHolder(viewType), callback)

            }
        }
    }

    override fun show(data: NotificationData, customViewHolder: OverlayWindowView.OverlayViewHolder<NotificationData>) {
        this.notificationData = data
        showWithSingleInstance(notificationData, customViewHolder, null)
    }

    override fun hide() {
        if (notificationView != null) {
            handler.post {
                kotlin.run {
                    if (notificationView != null) {
                        notificationView?.dismiss()
                    }
                }
            }
        }
    }

    override fun onClick(v: View?) {
        if (v != null && v.id == R.id.btn_notify_dismiss && notificationView != null) {
            notificationView?.dismiss()
        }
    }

    class NotificationData {
        @DrawableRes
        var resIcon: Int = 0
        var message: String? = null // NOSONAR -> just want access this field public
    }

    companion object {
        val handler = Handler()
    }
}