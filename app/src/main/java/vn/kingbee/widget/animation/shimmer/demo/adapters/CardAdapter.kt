package vn.kingbee.widget.animation.shimmer.demo.adapters

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import vn.kingbee.widget.animation.shimmer.demo.models.ItemCard
import vn.kingbee.widget.animation.shimmer.demo.utils.BaseUtils
import vn.kingbee.widget.animation.shimmer.demo.viewholders.ItemHolder


class CardAdapter : RecyclerView.Adapter<ItemHolder>() {
    private var mCards: List<ItemCard> = ArrayList()
    private var mType = BaseUtils.TYPE_LIST

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        return ItemHolder.newInstance(parent, mType)
    }

    override fun onBindViewHolder(holder: ItemHolder, position: Int) {
        holder.bind(mCards[position])
    }

    override fun getItemCount(): Int {
        return mCards.size
    }

    fun setCards(cards: List<ItemCard>?) {
        if (cards == null) {
            return
        }

        mCards = cards
    }

    fun setType(type: Int) {
        this.mType = type
    }
}