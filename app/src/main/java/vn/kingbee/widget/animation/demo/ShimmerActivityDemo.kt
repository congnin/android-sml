package vn.kingbee.widget.animation.demo

import android.os.Bundle
import vn.kingbee.widget.BaseActivity
import vn.kingbee.widget.R
import vn.kingbee.widget.animation.shimmer.ShimmerLayout

class ShimmerActivityDemo : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shimmer_layout_demo)
        val shimmerLayout = findViewById<ShimmerLayout>(R.id.shimmer_layout)
        shimmerLayout.startShimmerAnimation()
    }
}