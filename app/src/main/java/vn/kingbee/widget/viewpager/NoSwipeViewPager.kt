package vn.kingbee.widget.viewpager

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.viewpager.widget.ViewPager

class NoSwipeViewPager : ViewPager {
    constructor(context: Context) : super(context) {}

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {}

    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        // Never allow swiping to switch between pages
        //return super.onInterceptTouchEvent(ev);
        return false
    }

    override fun onTouchEvent(ev: MotionEvent): Boolean {
        // Never allow swiping to switch between pages
        //return super.onTouchEvent(ev);
        return false
    }
}