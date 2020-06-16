package vn.kingbee.widget.dialog.ext.help.holder

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.NonNull
import butterknife.BindView
import vn.kingbee.widget.R
import vn.kingbee.widget.dialog.ext.help.adapter.HelpViewHolder
import vn.kingbee.widget.dialog.ext.help.model.HelpFAQ
import vn.kingbee.widget.dialog.ext.help.utils.HelpUtils

class HelpQuestionViewHolder(@NonNull itemView: View, @NonNull listener: HelpViewHolderListener) : HelpViewHolder(itemView, listener) {

    @BindView(R.id.help_item_view)
    lateinit var viewQuestion: View

    @BindView(R.id.help_item_title)
    lateinit var tvTitle: TextView

    @BindView(R.id.help_item_arrow)
    lateinit var ivArrow: ImageView

    @BindView(R.id.help_item_line_top)
    lateinit var divider: View

    @BindView(R.id.help_item_view_bottom)
    lateinit var viewBottom: View

    override fun bindItem(position: Int, item: Any) {
        with(item as HelpFAQ) {
            tvTitle.text = item.question
            divider.visibility = if (getIndexItemInParent(item) > 0) View.VISIBLE else View.GONE
            viewQuestion.setOnClickListener(getHandleClickQuestion(this@HelpQuestionViewHolder, item))
            ivArrow.tag = position
            viewBottom.visibility = if (item.isActive) View.GONE else View.VISIBLE
            expand(ivArrow, !item.isActive, MIN_DURATION)
        }
    }

    @NonNull
    private fun getHandleClickQuestion(titleViewHolder: HelpQuestionViewHolder, item: HelpFAQ): View.OnClickListener {
        return View.OnClickListener {
            item.isActive = !item.isActive

            viewBottom.visibility = if (item.isActive) View.GONE else View.VISIBLE

            val indexChild = getItemList()!!.indexOf(item) + 1
            val indexParent = HelpUtils.findIndexCategoryOfFAQParent(getItemList(), item)
            val indexItemInParent = getIndexItemInParent(item) + 1
            titleViewHolder.ivArrow.tag = indexChild
            if (item.isActive) {
                handleExpandQuestion(titleViewHolder, item, indexChild, indexParent, indexItemInParent)
            } else {
                handleCollapseQuestion(titleViewHolder, indexChild, indexParent, indexItemInParent)

            }
        }
    }

    private fun handleCollapseQuestion(titleViewHolder: HelpQuestionViewHolder, indexChild: Int, indexParent: Int, indexItemInParent: Int) {
        //remove item from category mFAQs
        remove(indexChild, indexParent, indexItemInParent)

        //close arrow
        expand(titleViewHolder.ivArrow, true, DURATION)
    }

    private fun handleExpandQuestion(titleViewHolder: HelpQuestionViewHolder, item: HelpFAQ, indexChild: Int, indexParent: Int, indexItemInParent: Int) {
        val itemChild = HelpFAQ(item.answer)
        //add item to category mFAQs
        add(itemChild, indexChild, indexParent, indexItemInParent)

        //animation open arrow
        expand(titleViewHolder.ivArrow, false, DURATION)
    }

    private fun getIndexItemInParent(item: HelpFAQ): Int {
        val indexParent = HelpUtils.findIndexCategoryOfFAQParent(getItemList(), item)
        var indexItemInParent = 0
        if (indexParent > -1)
            indexItemInParent = getItemList()!![indexParent].faqs!!.indexOf(item)
        return indexItemInParent
    }
}