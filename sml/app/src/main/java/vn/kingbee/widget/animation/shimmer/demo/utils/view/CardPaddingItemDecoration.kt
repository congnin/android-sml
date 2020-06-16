package vn.kingbee.widget.animation.shimmer.demo.utils.view

import android.content.Context
import android.graphics.Rect
import androidx.recyclerview.widget.RecyclerView
import android.util.TypedValue
import android.view.View


class CardPaddingItemDecoration : androidx.recyclerview.widget.RecyclerView.ItemDecoration {
    private val paddingBetweenItems: Int

    constructor(context: Context) {
        this.paddingBetweenItems = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP, 8f, context.getResources().getDisplayMetrics()
        ).toInt()
    }

    override fun getItemOffsets(outRect: Rect,
                                view: View,
                                parent: androidx.recyclerview.widget.RecyclerView,
                                state: androidx.recyclerview.widget.RecyclerView.State) {
        outRect.set(0, 0, 0, paddingBetweenItems)
    }
}