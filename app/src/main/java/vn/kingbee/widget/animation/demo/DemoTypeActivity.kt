package vn.kingbee.widget.animation.demo

import vn.kingbee.widget.animation.shimmer.demo.utils.BaseUtils
import vn.kingbee.widget.animation.shimmer.demo.adapters.CardAdapter
import androidx.recyclerview.widget.RecyclerView
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import vn.kingbee.widget.BaseActivity
import vn.kingbee.widget.R
import vn.kingbee.widget.animation.shimmer.recyclerview.ShimmerRecyclerView

class DemoTypeActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_TYPE = "type"
    }

    private var shimmerRecycler: ShimmerRecyclerView? = null
    private var mAdapter: CardAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val type = getType()

        val layoutManager: androidx.recyclerview.widget.RecyclerView.LayoutManager?

        val demoConfiguration = BaseUtils.getDemoConfiguration(type, this)
        setTheme(demoConfiguration!!.styleResource)
        setContentView(demoConfiguration.layoutResource)
        layoutManager = demoConfiguration.layoutManager
        setTitle(demoConfiguration.titleResource)

        shimmerRecycler = findViewById(R.id.shimmer_recycler_view)

        if (demoConfiguration.itemDecoration != null) {
            shimmerRecycler!!.addItemDecoration(demoConfiguration.itemDecoration!!)
        }

        mAdapter = CardAdapter()
        mAdapter!!.setType(type)

        shimmerRecycler!!.layoutManager = layoutManager
        shimmerRecycler!!.adapter = mAdapter
        shimmerRecycler!!.showShimmerAdapter()

        shimmerRecycler!!.postDelayed({ loadCards() }, 3000)
    }

    private fun loadCards() {
        val type = getType()

        mAdapter!!.setCards(BaseUtils.getCards(resources, type))
        shimmerRecycler!!.hideShimmerAdapter()
    }

    private fun getType(): Int {
        return intent.getIntExtra(EXTRA_TYPE, BaseUtils.TYPE_LIST)
    }
}