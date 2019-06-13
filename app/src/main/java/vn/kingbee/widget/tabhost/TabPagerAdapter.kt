package vn.kingbee.widget.tabhost

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

class TabPagerAdapter(fragmentManager: FragmentManager) : FragmentPagerAdapter(fragmentManager) {
    private var mTabItem: List<TabPagerItem> = ArrayList()

    fun initList(fragmentList: List<TabPagerItem>) {
        (mTabItem as ArrayList).addAll(fragmentList)
    }

    override fun getItem(position: Int): Fragment = mTabItem[position].fragment

    override fun getCount(): Int = mTabItem.size

    override fun getPageTitle(position: Int): CharSequence? = mTabItem[position].title

    class TabPagerItem(internal var fragment: Fragment, internal var title: String)
}