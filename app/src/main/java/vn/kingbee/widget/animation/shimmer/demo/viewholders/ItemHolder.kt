package vn.kingbee.widget.animation.shimmer.demo.viewholders

import android.support.v7.widget.RecyclerView
import vn.kingbee.widget.animation.shimmer.demo.models.ItemCard
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import vn.kingbee.widget.R
import vn.kingbee.widget.animation.shimmer.demo.utils.BaseUtils.Companion.TYPE_GRID
import vn.kingbee.widget.animation.shimmer.demo.utils.BaseUtils.Companion.TYPE_LIST
import vn.kingbee.widget.animation.shimmer.demo.utils.BaseUtils.Companion.TYPE_SECOND_GRID
import vn.kingbee.widget.animation.shimmer.demo.utils.BaseUtils.Companion.TYPE_SECOND_LIST

class ItemHolder : RecyclerView.ViewHolder {
    private var mTitleView: TextView
    private var mDescView: TextView
    private var mThumbnailView: ImageView
    private var mSummaryView: TextView

    constructor(itemView: View): super(itemView) {
        mTitleView = itemView.findViewById(R.id.card_title)
        mDescView = itemView.findViewById(R.id.card_subtitle)
        mSummaryView = itemView.findViewById(R.id.card_summary)
        mThumbnailView = itemView.findViewById(R.id.card_image)
    }

    fun bind(card: ItemCard) {
        mTitleView.text = card.title
        mDescView.text = card.description
        mSummaryView.text = card.summaryText

        Glide.with(itemView.context).load(card.thumbnailUrl).into(mThumbnailView)
    }

    companion object {
        fun newInstance(container: ViewGroup, type: Int): ItemHolder {
            val root = LayoutInflater.from(container.context).inflate(
                getLayoutResourceId(type), container, false
            )

            return ItemHolder(root)
        }

        private fun getLayoutResourceId(type: Int): Int {
            val selectedLayoutResource: Int
            when (type) {
                TYPE_LIST -> selectedLayoutResource = R.layout.layout_news_card
                TYPE_SECOND_LIST -> selectedLayoutResource = R.layout.layout_second_news_card
                TYPE_GRID, TYPE_SECOND_GRID -> selectedLayoutResource = R.layout.layout_ecom_item
                else -> selectedLayoutResource = 0
            }

            return selectedLayoutResource
        }
    }
}