package vn.kingbee.widget.animation.shimmer.recyclerview

import android.annotation.SuppressLint
import androidx.recyclerview.widget.RecyclerView
import android.os.Build
import android.graphics.drawable.Drawable
import vn.kingbee.widget.animation.shimmer.ShimmerLayout
import android.view.ViewGroup
import android.view.LayoutInflater
import vn.kingbee.widget.R

class ShimmerViewHolder : androidx.recyclerview.widget.RecyclerView.ViewHolder {
    private var mShimmerLayout: ShimmerLayout

    constructor(inflater: LayoutInflater, parent: ViewGroup, innerViewResId: Int)
            : super(inflater.inflate(R.layout.viewholder_shimmer, parent, false)) {

        mShimmerLayout = itemView as ShimmerLayout

        inflater.inflate(innerViewResId, mShimmerLayout, true)
    }

    fun setShimmerAngle(angle: Int) {
        mShimmerLayout.setShimmerAngle(angle)
    }

    fun setShimmerColor(color: Int) {
        mShimmerLayout.setShimmerColor(color)
    }

    fun setShimmerMaskWidth(maskWidth: Float) {
        mShimmerLayout.setMaskWidth(maskWidth)
    }

    fun setShimmerViewHolderBackground(viewHolderBackground: Drawable?) {
        if (viewHolderBackground != null) {
            setBackground(viewHolderBackground)
        }
    }

    fun setShimmerAnimationDuration(duration: Int) {
        mShimmerLayout.setShimmerAnimationDuration(duration)
    }

    fun setAnimationReversed(animationReversed: Boolean) {
        mShimmerLayout.setAnimationReversed(animationReversed)
    }

    fun bind() {
        mShimmerLayout.startShimmerAnimation()
    }

    @SuppressLint("ObsoleteSdkInt")
    private fun setBackground(background: Drawable) {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN) {
            mShimmerLayout.background = background
        } else {
            mShimmerLayout.setBackgroundDrawable(background)
        }
    }
}