package vn.kingbee.widget.recyclerview.location

import android.content.Context
import android.view.ViewGroup
import vn.kingbee.model.Province
import vn.kingbee.model.Town
import vn.kingbee.widget.R
import vn.kingbee.widget.recyclerview.base.BaseRecyclerViewAdapter

class TownListAdapter : BaseRecyclerViewAdapter<Town> {

    private var mListener: TownListAdapterListener? = null

    constructor(context: Context, items: List<Town>, listener: TownListAdapterListener)
            : super(context, items) {
        mListener = listener
    }

    override fun onCreateFooterHolder(parent: ViewGroup): GenericViewHolder {
        throw UnsupportedOperationException()
    }

    override fun onCreateHeaderHolder(parent: ViewGroup): GenericViewHolder {
        throw UnsupportedOperationException()
    }

    override fun onCreateContentHolder(parent: ViewGroup): GenericViewHolder {
        val view = inflater!!.inflate(R.layout.location_list_item, parent, false)
        return TownViewHolder(context!!, view, mListener!!)
    }

    interface TownListAdapterListener {
        fun onTownClick(town: Town)
    }
}