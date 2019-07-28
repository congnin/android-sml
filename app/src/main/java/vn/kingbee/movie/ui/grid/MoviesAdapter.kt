package vn.kingbee.movie.ui.grid

import android.annotation.SuppressLint
import android.content.Context
import android.database.Cursor
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import butterknife.BindView
import butterknife.ButterKnife
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import vn.kingbee.movie.model.Movie
import vn.kingbee.movie.util.CursorRecyclerViewAdapter
import vn.kingbee.widget.R

class MoviesAdapter : CursorRecyclerViewAdapter<MoviesAdapter.MovieGridItemViewHolder> {

    private val context: Context
    private var onItemClickListener: OnItemClickListener? = null

    constructor(context: Context, cursor: Cursor?) : super(cursor) {
        this.context = context
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.onItemClickListener = listener
    }

    @SuppressLint("PrivateResource")
    override fun onBindViewHolder(viewHolder: MovieGridItemViewHolder, cursor: Cursor?) {
        if (cursor != null) {
            val movie = Movie.fromCursor(cursor)
            viewHolder.moviePoster.contentDescription = movie.title
            Glide.with(context)
                .load(POSTER_IMAGE_BASE_URL + POSTER_IMAGE_SIZE + movie.posterPath)
                .placeholder(ColorDrawable(ContextCompat.getColor(context, R.color.accent_material_light)))
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .fitCenter()
                .into(viewHolder.moviePoster)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieGridItemViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.grid_item_movie, parent, false)
        return MovieGridItemViewHolder(itemView, onItemClickListener)
    }

    fun getItem(position: Int): Movie? {
        val cursor = getCursor() ?: return null
        if (position < 0 || position > cursor.count) {
            return null
        }
        cursor.moveToFirst()
        for (i in 0 until position) {
            cursor.moveToNext()
        }
        return Movie.fromCursor(cursor)
    }

    inner class MovieGridItemViewHolder : RecyclerView.ViewHolder, View.OnClickListener {
        @BindView(R.id.image_movie_poster)
        lateinit var moviePoster: ImageView

        private var onItemClickListener: OnItemClickListener? = null

        constructor(itemView: View, onItemClickListener: OnItemClickListener?) : super(itemView) {
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

    interface OnItemClickListener {
        fun onItemClick(itemView: View, position: Int)
    }

    companion object {
        private const val POSTER_IMAGE_BASE_URL = "https://image.tmdb.org/t/p/"
        private const val POSTER_IMAGE_SIZE = "w780"
    }
}