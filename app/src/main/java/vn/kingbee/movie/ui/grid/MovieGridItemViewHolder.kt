package vn.kingbee.movie.ui.grid

import android.view.View
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import butterknife.BindView
import butterknife.ButterKnife
import vn.kingbee.widget.R

class MovieGridItemViewHolder : RecyclerView.ViewHolder, View.OnClickListener {
    @BindView(R.id.image_movie_poster)
    lateinit var moviePoster: ImageView

    private var onItemClickListener: MoviesAdapter.OnItemClickListener? = null

    constructor(itemView: View, onItemClickListener: MoviesAdapter.OnItemClickListener?) : super(itemView) {
        ButterKnife.bind(this, itemView)
        this.onItemClickListener = onItemClickListener
        itemView.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        if (onItemClickListener != null && v != null) {
            onItemClickListener?.onItemClick(v, adapterPosition)
        }
    }
}