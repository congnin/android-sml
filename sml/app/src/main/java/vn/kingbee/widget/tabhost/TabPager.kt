package vn.kingbee.widget.tabhost

import android.annotation.TargetApi
import android.content.Context
import android.os.Build
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.fragment.app.FragmentManager
import androidx.viewpager.widget.ViewPager
import butterknife.BindView
import butterknife.ButterKnife
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.menu_view_tab_pager.view.*
import vn.kingbee.widget.R
import vn.kingbee.widget.viewpager.NoSwipeViewPager


class TabPager : FrameLayout {
    @BindView(R.id.tabLayout)
    lateinit var labLayout: TabLayout
    @BindView(R.id.viewPager)
    lateinit var viewPager: NoSwipeViewPager

    private var mAdapter: TabPagerAdapter? = null

    constructor(context: Context) : super(context) {
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init(context)
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int, defStyleRes: Int)
            : super(context, attrs, defStyleAttr, defStyleRes) {
        init(context)
    }

    private fun init(context: Context) {
        LayoutInflater.from(context).inflate(R.layout.menu_view_tab_pager, this, true)
        ButterKnife.bind(this, this)
    }

    fun initTab(fragmentManager: FragmentManager, fragmentList: List<TabPagerAdapter.TabPagerItem>) {
        mAdapter = TabPagerAdapter(fragmentManager)
        mAdapter?.initList(fragmentList)
        viewPager.adapter = mAdapter
        tabLayout.setupWithViewPager(viewPager)
        initViewsFirstTime()
    }

    private fun initViewsFirstTime() {
        for (i in 0 until tabLayout.tabCount) {
            val tab = tabLayout.getTabAt(i)
            if (tab != null) {
                val relativeLayout = LayoutInflater.from(context).inflate(R.layout.menu_view_custom_tab_item, tabLayout, false)

                //set tab click
                relativeLayout.setOnClickListener { v ->
                    if (i != tabLayout.selectedTabPosition) {
                        viewPager.currentItem = i
                    }
                }

                tab.customView = relativeLayout
                if (i == 0) {
                    relativeLayout.isSelected = true
                    tab.select()
                }
            }
        }

        //add listener on page selected
        viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {
                //nothing
            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
                //nothing
            }

            override fun onPageSelected(position: Int) {
                // update ui on page selected
                val fragment = mAdapter?.getItem(position)
                if (fragment != null) {

                }
            }

        })
    }
}