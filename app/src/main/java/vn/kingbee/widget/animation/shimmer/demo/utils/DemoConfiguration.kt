package vn.kingbee.widget.animation.shimmer.demo.utils

import androidx.recyclerview.widget.RecyclerView
import androidx.annotation.StringRes
import androidx.annotation.LayoutRes
import androidx.annotation.StyleRes


class DemoConfiguration {
    @StyleRes
    var styleResource: Int = 0

    @LayoutRes
    var layoutResource: Int = 0

    @StringRes
    var titleResource: Int = 0

    var layoutManager: androidx.recyclerview.widget.RecyclerView.LayoutManager? = null

    var itemDecoration: androidx.recyclerview.widget.RecyclerView.ItemDecoration? = null
}