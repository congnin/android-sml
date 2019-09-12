package vn.kingbee.widget.recyclerview.location

import android.content.Context
import android.view.View
import android.widget.TextView
import butterknife.BindView
import butterknife.OnClick
import vn.kingbee.model.Province
import vn.kingbee.model.Town
import vn.kingbee.widget.R
import vn.kingbee.widget.recyclerview.base.BaseRecyclerViewAdapter

class TownViewHolder : BaseRecyclerViewAdapter.GenericViewHolder {
    @BindView(R.id.rl_location_item)
    lateinit var mView: View
    @BindView(R.id.text_view_location_item)
    lateinit var locationItem: TextView
    @BindView(R.id.separator_view)
    lateinit var separator: View

    private var mContext: Context
    private var mListener: TownListAdapter.TownListAdapterListener

    constructor(context: Context,
                itemView: View,
                listener: TownListAdapter.TownListAdapterListener) : super(itemView) {
        mContext = context
        mListener = listener
    }

    override fun bindItem(position: Int, item: Any) {
        if (position == LAST_COMMON_TOWN_INDEX) {
            separator.visibility = View.GONE
        } else {
            separator.visibility = View.VISIBLE
        }
        item as Town
        mView.tag = item
        locationItem.text = item.mName?.capitalize()
    }

    @OnClick(R.id.rl_location_item)
    fun onClick(view: View) {
        mListener.onTownClick(view.tag as Town)
    }

    companion object {
        private const val LAST_COMMON_TOWN_INDEX = 32
    }


}