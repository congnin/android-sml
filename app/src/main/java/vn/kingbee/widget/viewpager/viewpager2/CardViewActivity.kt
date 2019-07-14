package vn.kingbee.widget.viewpager.viewpager2

import android.os.Bundle
import vn.kingbee.widget.viewpager.viewpager2.cards.CardViewAdapter

open class CardViewActivity : BaseCardActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewPager.adapter = CardViewAdapter()
    }
}