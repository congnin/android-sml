package vn.kingbee.widget.viewpager.viewpager2

import android.os.Bundle
import com.google.android.material.tabs.TabLayout
import vn.kingbee.widget.R
import vn.kingbee.widget.viewpager.viewpager2.cards.Card
import com.google.android.material.tabs.TabLayoutMediator

class CardViewTabLayoutActivity : CardViewActivity() {

    private lateinit var tabLayout: TabLayout

    override val layoutId: Int = R.layout.activity_tablayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        tabLayout = findViewById(R.id.tabs)
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = Card.DECK[position].toString()
        }.attach()
    }
}