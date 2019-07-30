package vn.kingbee.movie.ui.grid

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import vn.kingbee.movie.model.Movie
import vn.kingbee.widget.R

class MoviesSearchAdapter(private val context: Context, items: List<Movie>?) : ArrayRecyclerViewAdapter<Movie, MovieGridItemViewHolder>(items) {
    private var onItemClickListener: MoviesAdapter.OnItemClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieGridItemViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.grid_item_movie, parent, false)
        return MovieGridItemViewHolder(itemView, onItemClickListener)
    }

    @SuppressLint("PrivateResource")
    override fun onBindViewHolder(holder: MovieGridItemViewHolder, position: Int) {

        val movie = getItems().get(position)
        holder.moviePoster.contentDescription = movie.title
        Glide.with(context)
            .load(POSTER_IMAGE_BASE_URL + POSTER_IMAGE_SIZE + movie.posterPath)
            .placeholder(ColorDrawable(ContextCompat.getColor(context, R.color.accent_material_light)))
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .fitCenter()
            .into(holder.moviePoster)
    }

    fun setOnItemClickListener(listener: MoviesAdapter.OnItemClickListener) {
        this.onItemClickListener = listener
    }

    companion object {

        private const val POSTER_IMAGE_BASE_URL = "https://image.tmdb.org/t/p/"
        private const val POSTER_IMAGE_SIZE = "w780"
    }
}