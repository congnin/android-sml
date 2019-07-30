package vn.kingbee.movie.ui.detail

import android.content.Context
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
import vn.kingbee.movie.model.MovieVideo
import vn.kingbee.movie.ui.grid.MoviesAdapter
import vn.kingbee.widget.R

class MovieVideosAdapter : RecyclerView.Adapter<MovieVideosAdapter.MovieVideoViewHolder> {

    private val context: Context

    private var moviesVideos: List<MovieVideo>? = null
    private var onItemClickListener: MoviesAdapter.OnItemClickListener? = null

    constructor(context: Context) {
        this.context = context
        moviesVideos = ArrayList()
    }

    fun setMovieVideos(moviesVideos: List<MovieVideo>?) {
        this.moviesVideos = moviesVideos
        notifyDataSetChanged()
    }

    fun setOnItemClickListener(listener: MoviesAdapter.OnItemClickListener?) {
        this.onItemClickListener = listener
    }

    fun getMovieVideos(): List<MovieVideo>? = moviesVideos

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieVideoViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.list_item_movie_video, parent, false)
        return MovieVideoViewHolder(itemView, onItemClickListener)
    }

    override fun onBindViewHolder(holder: MovieVideoViewHolder, position: Int) {
        if (moviesVideos == null) {
            return
        }

        val video = moviesVideos?.get(position)
        if (video != null && video.isYoutubeVideo) {
            Glide.with(context)
                .load(String.format(YOUTUBE_THUMBNAIL, video.key))
                .placeholder(ColorDrawable(ContextCompat.getColor(context, R.color.accent_material_light)))
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                .centerCrop()
                .into(holder.movieVideoThumbnail);
        }
    }

    override fun getItemCount(): Int {
        if (moviesVideos == null) {
            return 0
        }
        return moviesVideos?.size!!
    }

    fun getItem(position: Int): MovieVideo? {
        if (moviesVideos == null || position < 0 || position > moviesVideos?.size!!) {
            return null
        }
        return moviesVideos?.get(position)
    }

    inner class MovieVideoViewHolder : RecyclerView.ViewHolder, View.OnClickListener {
        @BindView(R.id.movie_video_thumbnail)
        lateinit var movieVideoThumbnail: ImageView

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

    companion object {
        private const val YOUTUBE_THUMBNAIL = "https://img.youtube.com/vi/%s/mqdefault.jpg"
    }
}