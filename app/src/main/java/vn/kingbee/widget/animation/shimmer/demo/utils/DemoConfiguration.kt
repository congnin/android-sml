package vn.kingbee.widget.animation.shimmer.demo.utils

import android.support.v7.widget.RecyclerView
import android.support.annotation.StringRes
import android.support.annotation.LayoutRes
import android.support.annotation.StyleRes


class DemoConfiguration {
    @StyleRes
    var styleResource: Int = 0

    @LayoutRes
    var layoutResource: Int = 0

    @StringRes
    var titleResource: Int = 0

    var layoutManager: RecyclerView.LayoutManager? = null

    var itemDecoration: RecyclerView.ItemDecoration? = null
}