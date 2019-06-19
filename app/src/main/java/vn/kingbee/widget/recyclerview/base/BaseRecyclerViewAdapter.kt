package vn.kingbee.widget.recyclerview.base

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.view.View
import butterknife.ButterKnife
import android.view.LayoutInflater
import android.view.ViewGroup

abstract class BaseRecyclerViewAdapter<T> :
    RecyclerView.Adapter<BaseRecyclerViewAdapter.GenericViewHolder> {

    protected var inflater: LayoutInflater? = null

    protected var context: Context? = null

    var items: List<T>? = null

    private var mIsShowFooter = false

    constructor(context: Context, items: List<T>) {
        // Cache the LayoutInflate to avoid asking for a new one each time.
        this.inflater = LayoutInflater.from(context)
        this.items = items
        this.context = context
    }

    fun append(items: List<T>?) {
        if (items != null && items != null) {
            (items as ArrayList).addAll(items)
            notifyItemRangeInserted(this.itemCount, items.size)
        }
    }

    fun append(from: Int, items: List<T>?) {
        if (items != null && items != null) {
            (items as ArrayList).addAll(items)
            notifyItemRangeInserted(from, items?.size!!)
        }
    }

    fun append(item: T?) {
        if (items != null && item != null) {
            (items as ArrayList).add(item)
            notifyItemInserted(this.itemCount - 1)
        }
    }

    fun removeSelectedItem(position: Int) {
        if (items == null || items!!.size <= position) {
            return
        }
        (items as ArrayList).removeAt(getItemPositionWithoutHeader(position))
        this.notifyItemRemoved(position)
        // this line is very important to update all the views belows
        // the item removed will adjust accordingly
        this.notifyItemRangeChanged(position, items!!.size)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GenericViewHolder {
        if (viewType == ViewType.TYPE_CONTENT) {
            // Item
            return onCreateContentHolder(parent)
        } else if (viewType == ViewType.TYPE_HEADER) {
            // Header
            return onCreateHeaderHolder(parent)
        } else if (viewType == ViewType.TYPE_FOOTER) {
            // Footer
            return onCreateFooterHolder(parent)
        }
        val detailMessage =
            "there is no type that matches the type $viewType + make sure your using types correctly"
        throw RuntimeException(detailMessage) // NOSONAR
    }

    abstract fun onCreateFooterHolder(parent: ViewGroup): GenericViewHolder

    abstract fun onCreateHeaderHolder(parent: ViewGroup): GenericViewHolder

    abstract fun onCreateContentHolder(parent: ViewGroup): GenericViewHolder

    override fun getItemViewType(position: Int): Int {
        if (isPositionHeader(position)) {
            return ViewType.TYPE_HEADER
        } else if (isPositionFooter(position)) {
            return ViewType.TYPE_FOOTER
        }
        return ViewType.TYPE_CONTENT
    }

    override fun onBindViewHolder(holder: GenericViewHolder, position: Int) {
        holder.bindItem(position, getItem(position)!!)
    }

    protected fun isPositionHeader(position: Int): Boolean {
        return isHasHeader() && position == 0
    }

    fun isPositionFooter(position: Int): Boolean {
        val itemCount = getItemCountReal() + getItemCountHeader()
        return isHasFooter() && position == itemCount
    }

    override fun getItemCount(): Int {
        var size = getItemCountReal()
        size += getItemCountHeader()
        size += getItemCountFooter()
        return size
    }

    fun getItemCountReal(): Int {
        var size = 0
        if (items != null) {
            size = items!!.size
        }
        return size
    }

    private fun getItemCountHeader(): Int {
        return if (isHasHeader()) {
            1
        } else 0
    }

    private fun getItemCountFooter(): Int {
        return if (isHasFooter()) {
            1
        } else 0
    }

    fun getItem(position: Int): T? {
        // get real position.
        // we need get position to mapping with list
        // from header to current item.
        // so: only get without header.
        val pos = getItemPositionWithoutHeader(position)
        return if (pos >= 0 && items != null && items!!.size > pos) {
            items!!.get(pos)
        } else null
    }

    fun setItem(position: Int, t: T) {
        val pos = getItemPositionWithoutHeader(position)
        if (pos >= 0 && items != null && items!!.size > pos) {
            (items!! as ArrayList)[pos] = t
        }
    }

    protected fun getItemPositionWithoutHeader(position: Int): Int {
        //if we have header: real position need -1
        //otherwise: keep position
        return position - getItemCountHeader()
    }

    protected fun isHasHeader(): Boolean {
        return false
    }

    protected fun isHasFooter(): Boolean {
        return isShowFooter()
    }

    fun isShowFooter(): Boolean {
        return mIsShowFooter
    }

    fun setShowFooter(isShow: Boolean) {
        if (isShow == mIsShowFooter) {
            return
        }
        mIsShowFooter = isShow
        if (isShow) {
            notifyItemInserted(itemCount)
        } else {
            notifyItemRemoved(itemCount)
        }
    }

    abstract class GenericViewHolder(itemView: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView) {
        init {
            ButterKnife.bind(this@GenericViewHolder, itemView)
        }

        abstract fun bindItem(position: Int, item: Any)
    }

    object ViewType {
        const val TYPE_HEADER = 0x01
        const val TYPE_CONTENT = 0x02
        const val TYPE_FOOTER = 0x03
    }
}