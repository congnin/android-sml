package vn.kingbee.widget.notification

import android.content.Context
import android.graphics.PixelFormat
import android.os.Handler
import android.support.annotation.LayoutRes
import android.support.annotation.StyleRes
import android.view.*
import android.widget.FrameLayout
import vn.kingbee.widget.R

class OverlayWindowView<T>(context: Context) : FrameLayout(context) {

    private val mHandler = Handler()

    private var styleAnimationResId: Int = 0
    private var windowGravity: Int = 0
    private var windowFlags: Int = 0
    private var windowType: Int = 0
    private var windowHeight: Int = 0
    private var windowWidth: Int = 0
    private var autoDismissDuration: Long = 0
    private var enableAutoDismiss: Boolean = false
    private var marginTop: Int = 0
    private var viewHolder: OverlayViewHolder<T>? = null
    private var data: T? = null // NOSONAR -> this field will use later

    private var windowManager: WindowManager? = null
    private var isShowing: Boolean = false

    private val windowManger: ViewManager?
        get() {
            if (windowManager == null) {
                windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
            }
            return windowManager
        }

    private val windowLayoutParams: ViewGroup.LayoutParams
        get() {
            val windowParams = WindowManager.LayoutParams()
            windowParams.gravity = windowGravity
            windowParams.height = windowHeight
            windowParams.width = windowWidth
            windowParams.format = PixelFormat.TRANSLUCENT
            windowParams.flags = windowFlags
            windowParams.type = windowType
            windowParams.windowAnimations = styleAnimationResId
            windowParams.y = marginTop
            return windowParams
        }

    fun onUpdateData(data: T) {
        this.data = data
        viewHolder!!.updateData(data)
        if (isShowing) {
            resetAutoDismiss()
        } else {
            show()
        }
    }

    fun resetAutoDismiss() {
        if (enableAutoDismiss) {
            mHandler.removeCallbacksAndMessages(null)
            mHandler.postDelayed({ removeFromWindow() }, autoDismissDuration)
        }
    }

    override fun onDetachedFromWindow() {
        isShowing = false
        super.onDetachedFromWindow()
        mHandler.removeCallbacksAndMessages(null)
    }

    override fun onAttachedToWindow() {
        isShowing = true
        super.onAttachedToWindow()
        resetAutoDismiss()
    }

    fun removeFromWindow() {
        if (windowToken != null) {
            windowManger!!.removeView(this)
        }
        onCleanData()
    }

    private fun onCleanData() {
        // Clean viewHolderData here
        windowManager = null
    }

    fun show() {
        if (!isShowing) {
            windowManger!!.addView(this, windowLayoutParams)
        }
    }

    fun dismiss() {
        mHandler.removeCallbacksAndMessages(null)
        removeFromWindow()
    }

    /**
     * BUILDER CLASS
     * Gives us a builder utility class with a fluent API for easily configuring Notification views
     */
    class Builder<T>(private val context: Context?) {

        private var callback: NotificationCallback? = null
        private var viewHolder: OverlayViewHolder<T>? = null
        private var viewHolderData: T? = null

        // Default values
        private var styleAnimationResId = R.style.NotificationAnim
        private var windowGravity = Gravity.TOP or Gravity.CENTER_HORIZONTAL
        private var windowFlags = (WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                or WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                or WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN
                or WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL)
        private var windowType = WindowManager.LayoutParams.TYPE_TOAST
        private var windowHeight = WindowManager.LayoutParams.WRAP_CONTENT
        private var windowWidth = WindowManager.LayoutParams.WRAP_CONTENT
        private var autoDismissDuration: Long = 5000
        private var enableAutoDismiss = true
        private var marginTop: Int = 0

        fun windowGravity(gravity: Int): Builder<T> {
            this.windowGravity = gravity
            return this
        }

        fun animationStyle(@StyleRes styleAnimationResId: Int): Builder<T> {
            this.styleAnimationResId = styleAnimationResId
            return this
        }

        fun windowFlags(windowFlags: Int): Builder<T> {
            this.windowFlags = windowFlags
            return this
        }

        fun windowWidth(windowWidth: Int): Builder<T> {
            this.windowWidth = windowWidth
            return this
        }

        fun windowHeight(windowHeight: Int): Builder<T> {
            this.windowHeight = windowHeight
            return this
        }

        fun windowType(windowType: Int): Builder<T> {
            this.windowType = windowType
            return this
        }

        fun autoDismissDuration(autoDismissDuration: Long): Builder<T> {
            this.enableAutoDismiss = true
            this.autoDismissDuration = autoDismissDuration
            return this
        }

        fun enableAutoDismiss(enableAutoDismiss: Boolean): Builder<T> {
            this.enableAutoDismiss = enableAutoDismiss
            return this
        }

        fun withData(data: T): Builder<T> {
            this.viewHolderData = data
            return this
        }

        fun build(): OverlayWindowView<T> {
            checkException()
            val notificationView = OverlayWindowView<T>(context!!)
            notificationView.windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
            notificationView.windowGravity = windowGravity
            notificationView.styleAnimationResId = styleAnimationResId
            notificationView.windowFlags = windowFlags
            notificationView.windowWidth = windowWidth
            notificationView.windowHeight = windowHeight
            notificationView.windowType = windowType
            notificationView.enableAutoDismiss = enableAutoDismiss
            notificationView.autoDismissDuration = autoDismissDuration
            notificationView.marginTop = marginTop
            initViewHolder(notificationView, viewHolder!!, viewHolderData, callback)
            // Just clean reference callback for this builder -> viewHolder will hold it
            callback = null
            return notificationView
        }

        private fun initViewHolder(notificationView: OverlayWindowView<T>, viewHolder: OverlayViewHolder<T>, data: T?, callback: NotificationCallback?) {
            LayoutInflater.from(notificationView.context).inflate(viewHolder.layoutId, notificationView)
            viewHolder.initView(notificationView)
            viewHolder.updateData(data)
            viewHolder.setCallback(callback)
            viewHolder.setNotificationView(notificationView)
            notificationView.viewHolder = viewHolder
        }

        private fun checkException() {
            if (context == null) {
                throw OverlayBuilderException("Context must not null")
            }
            if (viewHolder == null) {
                throw OverlayBuilderException("View holder must not null")
            }
        }

        fun show(): OverlayWindowView<T> {
            val overlayWindowView = build()
            overlayWindowView.show()
            return overlayWindowView
        }

        fun withViewHolder(viewHolder: OverlayViewHolder<T>): Builder<T> {
            this.viewHolder = viewHolder
            return this
        }

        fun withCallback(callback: NotificationCallback): Builder<T> {
            this.callback = callback
            return this
        }

        fun withMarginTop(marginTop: Int): Builder<T> {
            this.marginTop = marginTop
            return this
        }

        private class OverlayBuilderException(message: String) : RuntimeException(message)
    }


    interface OverlayViewHolder<T> {
        @get:LayoutRes
        val layoutId: Int

        /**
         * Notify for this viewHolder to attach to new view
         *
         * @param view
         */
        fun initView(view: View)

        /**
         * Update current data for next show
         *
         * @param data
         */
        fun updateData(data: T?)

        /**
         * View clicked callback
         *
         * @param callback
         */
        fun setCallback(callback: NotificationCallback?)

        /**
         * Set a new notificationView when viewHolder is single instance
         *
         * @param notificationView
         */
        fun setNotificationView(notificationView: OverlayWindowView<T>)
    }

    interface NotificationCallback {
        fun onViewClicked(view: View)
    }
}