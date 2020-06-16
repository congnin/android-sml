package vn.kingbee.widget.recyclerview.help

import android.content.Context
import android.view.ViewGroup
import vn.kingbee.widget.R
import vn.kingbee.widget.recyclerview.base.BaseRecyclerViewAdapter

class HelpVideoAdapter : BaseRecyclerViewAdapter<HelpVideo> {

    private var mListener: HelpVideoClickListener? = null

    constructor(context: Context, items: List<HelpVideo>, listener: HelpVideoClickListener) : super(
        context, items
    ) {
        mListener = listener
    }

    override fun onCreateFooterHolder(parent: ViewGroup): GenericViewHolder {
        throw UnsupportedOperationException()
    }

    override fun onCreateHeaderHolder(parent: ViewGroup): GenericViewHolder {
        throw UnsupportedOperationException()
    }

    override fun onCreateContentHolder(parent: ViewGroup): GenericViewHolder {
        val view = inflater!!.inflate(R.layout.view_help_video_item, parent, false)
        return HelpVideoViewHolder(context!!, view, mListener!!)
    }

    interface HelpVideoClickListener {
        fun onVideoClick(path: String)
    }
}