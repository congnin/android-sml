package vn.kingbee.widget.layout.motion.utils

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.core.view.NestedScrollingParent2
import android.widget.FrameLayout
import androidx.annotation.NonNull
import androidx.annotation.Nullable


class TouchFrameLayout : FrameLayout, NestedScrollingParent2 {

    val motionLayout: NestedScrollingParent2
        get() = parent as NestedScrollingParent2


    constructor(@NonNull context: Context) : super(context) {}

    constructor(@NonNull context: Context, @Nullable attrs: AttributeSet) : super(context, attrs) {}

    constructor(@NonNull context: Context, @Nullable attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {}

    override fun onStartNestedScroll(@NonNull child: View, @NonNull target: View, axes: Int, type: Int): Boolean {
        return motionLayout.onStartNestedScroll(child, target, axes, type)
    }

    override fun onNestedScrollAccepted(@NonNull child: View, @NonNull target: View, axes: Int, type: Int) {
        motionLayout.onNestedScrollAccepted(child, target, axes, type)
    }

    override fun onStopNestedScroll(@NonNull target: View, type: Int) {
        motionLayout.onStopNestedScroll(target, type)
    }

    override fun onNestedScroll(@NonNull target: View, dxConsumed: Int, dyConsumed: Int, dxUnconsumed: Int, dyUnconsumed: Int, type: Int) {
        motionLayout.onNestedScroll(target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed, type)
    }

    override fun onNestedPreScroll(@NonNull target: View, dx: Int, dy: Int, @NonNull consumed: IntArray, type: Int) {
        motionLayout.onNestedPreScroll(target, dx, dy, consumed, type)
    }
}