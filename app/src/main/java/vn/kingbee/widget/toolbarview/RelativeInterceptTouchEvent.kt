package vn.kingbee.widget.toolbarview

import android.annotation.TargetApi
import android.content.Context
import android.os.Build
import android.util.AttributeSet
import android.view.MotionEvent
import android.widget.RelativeLayout

class RelativeInterceptTouchEvent : RelativeLayout {
    private var isInterceptTouchEvent = false

    constructor(context: Context) : super(context) {}

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {}

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {}

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes) {
    }

    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        return this.isInterceptTouchEvent
    }

    fun setInterceptTouchEvent(isInterceptTouchEvent: Boolean) {
        this.isInterceptTouchEvent = isInterceptTouchEvent
    }
}