package vn.kingbee.widget.dialog.ext.help.adapter

import android.view.View
import android.view.animation.Animation
import android.view.animation.RotateAnimation
import androidx.annotation.NonNull
import vn.kingbee.widget.dialog.ext.help.model.HelpFAQ
import vn.kingbee.widget.recyclerview.base.BaseRecyclerViewAdapter

abstract class HelpViewHolder(itemView: View, listener: HelpViewHolderListener)
    : BaseRecyclerViewAdapter.GenericViewHolder(itemView) {

    protected val mListener: HelpViewHolderListener = listener

    open fun getItemList(): List<HelpFAQ>? = if(mListener.getItemList() != null) mListener.getItemList() else emptyList()

    //add answer to list
    protected fun add(itemChild: HelpFAQ, indexChild: Int, indexParent: Int, indexItemInParent: Int) {
        if (getItemList() != null && indexItemInParent > 0) {
            (getItemList()!![indexParent].faqs as ArrayList).add(indexItemInParent, itemChild)

            //add to items
            (getItemList() as ArrayList).add(indexChild, itemChild)
            mListener.notifyItemInserted(indexChild)
        }
    }

    //remove answer out of list
    protected fun remove(indexChild: Int, indexParent: Int, indexItemIntParent: Int) {
        if (getItemList() != null && indexParent > -1 && indexItemIntParent > 0) {
            (getItemList()!![indexParent].faqs as ArrayList).removeAt(indexItemIntParent)
        }

        //remove from items
        (getItemList() as ArrayList).removeAt(indexChild)
        mListener.notifyItemRemoved(indexChild)
    }

    protected fun expand(view: View, expanded: Boolean, duration: Long) {
        setExpanded(view, !expanded)
        onExpansionToggled(view, expanded, duration)
    }

    protected fun setExpanded(view: View, expanded: Boolean) {
        if (expanded) {
            view.rotation = ROTATED_POSITION
        } else {
            view.rotation = INITIAL_POSITION
        }
    }

    private fun onExpansionToggled(view: View, expanded: Boolean, duration: Long) {
        val rotateAnimation: RotateAnimation
        if (expanded) { //rotate clockwise
            rotateAnimation = RotateAnimation(ROTATED_POSITION, INITIAL_POSITION,
                RotateAnimation.RELATIVE_TO_SELF, PIVOT_VALUE,
                RotateAnimation.RELATIVE_TO_SELF, PIVOT_VALUE)
        } else { //rotate counterclockwise
            rotateAnimation = RotateAnimation(-1 * ROTATED_POSITION, INITIAL_POSITION,
                RotateAnimation.RELATIVE_TO_SELF, PIVOT_VALUE,
                RotateAnimation.RELATIVE_TO_SELF, PIVOT_VALUE)
        }
        rotateAnimation.duration = duration
        rotateAnimation.fillAfter = true
        rotateAnimation.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationRepeat(animation: Animation?) {
                //nothing
            }

            override fun onAnimationEnd(animation: Animation?) {
                if (mListener != null && !expanded && view.tag != null) {
                    mListener.onExpandItem(view.tag as Int)
                }
            }

            override fun onAnimationStart(animation: Animation?) {
                //nothing
            }
        })
        view.startAnimation(rotateAnimation)
    }

    @NonNull
    protected fun getOnclickNoAction(): View.OnClickListener {
        return View.OnClickListener {
            // do nothing: use for view temp
        }
    }

    interface HelpViewHolderListener {
        fun getItemList(): List<HelpFAQ>?
        fun notifyItemRemoved(indexChild: Int)
        fun notifyItemInserted(indexChild: Int)
        fun notifyItemChanged(i: Int, item: Any)
        fun onExpandItem(position: Int)
    }

    companion object {
        private const val PIVOT_VALUE = 0.5f
        private const val INITIAL_POSITION = 0.0f
        private const val ROTATED_POSITION = 90f
        const val DURATION = 200L
        const val MIN_DURATION = 50L
    }
}