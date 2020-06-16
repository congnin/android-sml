package vn.kingbee.widget.notification

import android.app.Activity
import android.content.Context
import android.os.Handler
import androidx.annotation.LayoutRes
import androidx.annotation.StyleRes
import android.view.*
import android.view.animation.AnimationUtils
import android.widget.FrameLayout
import vn.kingbee.widget.R

class OverlayWindowView<T>(context: Context) : FrameLayout(context) {

    private val mHandler = Handler()

    private var styleAnimationEnterResId: Int = 0
    private var styleAnimationExitResId: Int = 0
    private var windowGravity: Int = 0
    private var windowHeight: Int = 0
    private var windowWidth: Int = 0
    private var autoDismissDuration: Long = 0
    private var enableAutoDismiss: Boolean = false
    private var marginTop: Int = 0
    private var viewHolder: OverlayViewHolder<T>? = null
    private var data: T? = null
    lateinit var mWindow: Window
    private var isAddToWindow = false

    var isShowing: Boolean = false

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
        isAddToWindow = true
        super.onAttachedToWindow()
        resetAutoDismiss()
    }

    fun removeFromWindow() {
        if (windowToken != null) {
            val animation = AnimationUtils.loadAnimation(context, styleAnimationExitResId)
            this.startAnimation(animation)
            (mWindow.decorView as ViewGroup).removeView(this)
        }
    }

    fun show() {
        if (!isShowing && isHostActivityRunning()) {
            (mWindow.decorView as ViewGroup).addView(this, this.getWindowLayoutParams())
            val animation = AnimationUtils.loadAnimation(context, styleAnimationEnterResId)
            startAnimation(animation)
        }
    }

    private fun isHostActivityRunning(): Boolean {
        var activity: Activity? = null
        if (context is Activity) {
            activity = context as Activity
        } else if (context is ContextThemeWrapper) {
            activity = (context as ContextThemeWrapper).baseContext as Activity
        }

        return activity != null && !activity.isFinishing
    }

    private fun getWindowLayoutParams(): LayoutParams {
        val params = LayoutParams(windowWidth, windowHeight, windowGravity)
        params.topMargin = this.marginTop
        return params
    }

    fun dismiss() {
        mHandler.removeCallbacksAndMessages(null)
        removeFromWindow()
    }

    /**
     * BUILDER CLASS
     * Gives us a builder utility class with a fluent API for easily configuring Notification views
     */
    class Builder<T> {
        lateinit var mWindow: Window
        private var callback: NotificationCallback? = null
        private var viewHolder: OverlayViewHolder<T>? = null
        private var viewHolderData: T? = null

        // Default values
        private var styleAnimationEnter: Int = R.anim.notification_enter
        private var styleAnimationExit: Int = R.anim.notification_exit
        private var windowGravity = Gravity.TOP or Gravity.CENTER_HORIZONTAL
        private var windowHeight = WindowManager.LayoutParams.WRAP_CONTENT
        private var windowWidth = WindowManager.LayoutParams.WRAP_CONTENT
        private var autoDismissDuration: Long = 5000
        private var enableAutoDismiss = true
        private var marginTop: Int = 0

        constructor(window: Window) {
            this.mWindow = window
        }

        fun windowGravity(gravity: Int): Builder<T> {
            this.windowGravity = gravity
            return this
        }

        fun animationEnter(@StyleRes styleAnimationResId: Int): Builder<T> {
            this.styleAnimationEnter = styleAnimationResId
            return this
        }

        fun animationExit(@StyleRes styleAnimationResId: Int): Builder<T> {
            this.styleAnimationExit = styleAnimationResId
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
            val notificationView = OverlayWindowView<T>(mWindow.context)
            notificationView.mWindow = this.mWindow
            notificationView.windowGravity = windowGravity
            notificationView.styleAnimationEnterResId = this.styleAnimationEnter
            notificationView.styleAnimationExitResId = this.styleAnimationExit
            notificationView.windowWidth = windowWidth
            notificationView.windowHeight = windowHeight
            notificationView.enableAutoDismiss = enableAutoDismiss
            notificationView.autoDismissDuration = autoDismissDuration
            notificationView.marginTop = marginTop
            initViewHolder(notificationView, viewHolder!!, viewHolderData, callback)
            // Just clean reference callback for this builder -> viewHolder will hold it
            callback = null
            return notificationView
        }

        private fun initViewHolder(notificationView: OverlayWindowView<T>,
                                   viewHolder: OverlayViewHolder<T>,
                                   data: T?,
                                   callback: NotificationCallback?) {
            LayoutInflater.from(notificationView.context).inflate(viewHolder.layoutId, notificationView)
            viewHolder.initView(notificationView)
            viewHolder.updateData(data)
            viewHolder.setCallback(callback)
            viewHolder.setNotificationView(notificationView)
            notificationView.viewHolder = viewHolder
        }

        private fun checkException() {
            if (this.mWindow == null) {
                throw OverlayBuilderException("Context must not null")
            } else if (this.viewHolder == null) {
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

        fun withCallback(callback: NotificationCallback?): Builder<T> {
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