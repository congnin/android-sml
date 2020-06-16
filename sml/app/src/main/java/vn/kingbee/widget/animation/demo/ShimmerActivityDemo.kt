package vn.kingbee.widget.animation.demo

import android.os.Bundle
import vn.kingbee.widget.BaseActivity
import vn.kingbee.widget.R
import vn.kingbee.widget.animation.shimmer.ShimmerLayout
import android.content.Intent
import android.widget.Button
import vn.kingbee.widget.animation.shimmer.demo.utils.BaseUtils


class ShimmerActivityDemo : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shimmer_layout_demo)
        val shimmerLayout = findViewById<ShimmerLayout>(R.id.shimmer_layout)
        shimmerLayout.startShimmerAnimation()

        val firstListDemoButton = findViewById<Button>(R.id.list_demo_button)
        val firstGridDemoButton = findViewById<Button>(R.id.grid_demo_button)

        createClickListener(firstListDemoButton, BaseUtils.TYPE_LIST)
        createClickListener(firstGridDemoButton, BaseUtils.TYPE_GRID)

        val secondListDemoButton = findViewById<Button>(R.id.list_second_demo_button)
        val secondGridDemoButton = findViewById<Button>(R.id.grid_second_demo_button)

        createClickListener(secondListDemoButton, BaseUtils.TYPE_SECOND_LIST)
        createClickListener(secondGridDemoButton, BaseUtils.TYPE_SECOND_GRID)
    }

    private fun createClickListener(button: Button, demoType: Int) {
        button.setOnClickListener { startDemo(demoType) }
    }

    private fun startDemo(demoType: Int) {
        val intent = Intent(this, DemoTypeActivity::class.java)
        intent.putExtra(DemoTypeActivity.EXTRA_TYPE, demoType)
        startActivity(intent)
    }
}