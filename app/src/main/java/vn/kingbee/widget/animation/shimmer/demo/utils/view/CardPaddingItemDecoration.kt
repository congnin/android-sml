package vn.kingbee.widget.animation.shimmer.demo.utils.view

import android.content.Context
import android.graphics.Rect
import android.support.v7.widget.RecyclerView
import android.util.TypedValue
import android.view.View


class CardPaddingItemDecoration : RecyclerView.ItemDecoration {
    private val paddingBetweenItems: Int

    constructor(context: Context) {
        this.paddingBetweenItems = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP, 8f, context.getResources().getDisplayMetrics()
        ).toInt()
    }

    override fun getItemOffsets(outRect: Rect,
                                view: View,
                                parent: RecyclerView,
                                state: RecyclerView.State) {
        outRect.set(0, 0, 0, paddingBetweenItems)
    }
}