package vn.kingbee.widget.dialog.ext.help.holder

import vn.kingbee.widget.dialog.ext.help.adapter.HelpViewHolder
import android.view.View
import android.widget.ImageView
import butterknife.BindView
import android.widget.TextView
import vn.kingbee.widget.R
import vn.kingbee.widget.dialog.ext.help.model.HelpFAQ

class HelpCategoryViewHolder(itemView: View, listener: HelpViewHolderListener) : HelpViewHolder(itemView, listener) {
    @BindView(R.id.help_item_category_view)
    lateinit var viewCategory: View

    @BindView(R.id.help_item_category_title)
    lateinit var tvCategory: TextView

    @BindView(R.id.help_item_category_arrow)
    lateinit var ivArrow: ImageView

    @BindView(R.id.help_item_line_top)
    lateinit var dividerTop: View

    @BindView(R.id.help_item_category_view_top)
    lateinit var viewTop: View

    @BindView(R.id.help_item_category_view_bottom)
    lateinit var viewBottom: View

    override fun bindItem(position: Int, item: Any) {
        with(item as HelpFAQ) {
            tvCategory.text = item.category
            viewTop.setOnClickListener(getOnclickNoAction())
            viewCategory.setOnClickListener(getHandleClickCategory(this@HelpCategoryViewHolder, item))
            dividerTop.visibility = if (position != 0) View.VISIBLE else View.INVISIBLE
            viewBottom.visibility = if (item.isActive) View.GONE else View.VISIBLE
            ivArrow.tag = position
            setExpanded(ivArrow, item.isActive)
        }
    }

    private fun getHandleClickCategory(categoryViewHolder: HelpCategoryViewHolder, item: HelpFAQ)
            : View.OnClickListener {
        return View.OnClickListener {
            item.isActive = !item.isActive
            viewBottom.visibility = if (item.isActive) View.GONE else View.VISIBLE
            val indexChild = getItemList()!!.indexOf(item) + 1
            categoryViewHolder.ivArrow.tag = indexChild
            if (item.isActive) {
                handleExpandCategory(categoryViewHolder, item, indexChild)
            } else {
                handleCollapseCategory(categoryViewHolder, item, indexChild)
            }
        }
    }

    private fun handleCollapseCategory(categoryViewHolder: HelpCategoryViewHolder, item: HelpFAQ, indexChild: Int) {
        // add count expand of item
        if (item.faqs != null && item.faqs!!.isNotEmpty()) {
            for (i in item.faqs!!.size - 1 downTo 0) {
                (getItemList() as ArrayList).removeAt(indexChild + i)
                mListener.notifyItemRemoved(indexChild + i)
            }
        }


        //close arrow
        expand(categoryViewHolder.ivArrow, true, DURATION)
    }

    private fun handleExpandCategory(categoryViewHolder: HelpCategoryViewHolder, item: HelpFAQ, indexChild: Int) {
        for (i in 0 until item.faqs!!.size) {
            val itemChild = item.faqs!![i]
            (getItemList() as ArrayList).add(indexChild + i, itemChild)

            mListener.notifyItemInserted(indexChild + i)
        }

        //animation open arrow
        expand(categoryViewHolder.ivArrow, false, DURATION)
    }
}