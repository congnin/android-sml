package vn.kingbee.widget.recyclerview.location

import android.content.Context
import android.view.ViewGroup
import vn.kingbee.model.Province
import vn.kingbee.widget.R
import vn.kingbee.widget.recyclerview.base.BaseRecyclerViewAdapter

class ProvinceListAdapter : BaseRecyclerViewAdapter<Province> {

    private var mListener: ProvinceListAdapterListener? = null

    constructor(context: Context, items: List<Province>, listener: ProvinceListAdapterListener)
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
        return ProvinceViewHolder(context!!, view, mListener!!)
    }

    interface ProvinceListAdapterListener {
        fun onProvinceClick(province: Province)
    }
}