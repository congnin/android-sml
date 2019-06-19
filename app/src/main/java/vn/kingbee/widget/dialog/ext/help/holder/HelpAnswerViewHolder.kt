package vn.kingbee.widget.dialog.ext.help.holder

import android.text.Html
import android.view.View
import android.widget.TextView
import androidx.annotation.NonNull
import butterknife.BindView
import vn.kingbee.widget.R
import vn.kingbee.widget.dialog.ext.help.adapter.HelpViewHolder
import vn.kingbee.widget.dialog.ext.help.model.HelpFAQ
import vn.kingbee.widget.dialog.ext.help.utils.HelpUtils

class HelpAnswerViewHolder(@NonNull itemView: View, @NonNull listener: HelpViewHolderListener) : HelpViewHolder(itemView, listener) {

    @BindView(R.id.help_item_answer_description)
    lateinit var mTvDescription: TextView

    init {
        mTvDescription.setOnClickListener(getHandleClickCategory())
    }

    private fun getHandleClickCategory()
            : View.OnClickListener {
        return View.OnClickListener {
            if (it.tag != null) {
                val strAnswer = it.tag as String
                val indexChild = HelpUtils.findIndexAnswerOfFAQParent(getItemList(), strAnswer)
                if (indexChild > -1 && getItemList()!!.size > indexChild) {
                    val helpFAQ = getItemList()!![indexChild]
                    handleRemoveAnswer(indexChild, helpFAQ)
                }
            }
        }
    }

    override fun bindItem(position: Int, item: Any) {
        with(item as HelpFAQ) {
            mTvDescription.tag = item.answer
            mTvDescription.text = Html.fromHtml(item.answer)
        }
    }

    private fun handleRemoveAnswer(indexChild: Int, helpFAQ: HelpFAQ) {
        val indexParent = HelpUtils.findIndexCategoryOfFAQParent(getItemList(), helpFAQ)
        var indexItemInParent = 0
        if (indexParent > -1 && getItemList()!!.size > indexParent)
            indexItemInParent = getItemList()!![indexParent].faqs!!.indexOf(helpFAQ)

        //remove item from category mFAQs
        remove(indexChild, indexParent, indexItemInParent)

        //close arrow
        if (indexChild > 1) {
            getItemList()!![indexChild - 1].isActive = false
            mListener.notifyItemChanged(indexChild - 1, getItemList()!![indexChild - 1])
        }
    }
}