package vn.kingbee.movie.ui.detail

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import butterknife.ButterKnife
import android.widget.TextView
import butterknife.BindView
import vn.kingbee.movie.ui.grid.MoviesAdapter
import vn.kingbee.widget.R
import vn.kingbee.movie.model.MovieReview
import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup

class MovieReviewsAdapter : RecyclerView.Adapter<MovieReviewsAdapter.MovieReviewViewHolder> {

    private var movieReviews: List<MovieReview>? = null
    private var onItemClickListener: MoviesAdapter.OnItemClickListener? = null

    constructor() {
        movieReviews = ArrayList()
    }

    fun setMovieReviews(movieReviews: List<MovieReview>?) {
        this.movieReviews = movieReviews
        notifyDataSetChanged()
    }

    fun setOnItemClickListener(listener: MoviesAdapter.OnItemClickListener?) {
        this.onItemClickListener = listener
    }

    fun getMovieReviews(): List<MovieReview>? {
        return movieReviews
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieReviewViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_item_movie_review, parent, false)
        return MovieReviewViewHolder(itemView, onItemClickListener!!)
    }

    @SuppressLint("PrivateResource")
    override fun onBindViewHolder(holder: MovieReviewViewHolder, position: Int) {
        if (movieReviews == null) {
            return
        }
        val review = movieReviews!![position]
        holder.content.text = review.content
        holder.author.text = review.author
    }

    override fun getItemCount(): Int {
        return if (movieReviews == null) {
            0
        } else movieReviews!!.size
    }

    fun getItem(position: Int): MovieReview? {
        return if (movieReviews == null || position < 0 || position > movieReviews!!.size) {
            null
        } else movieReviews!![position]
    }

    inner class MovieReviewViewHolder : RecyclerView.ViewHolder, View.OnClickListener {
        @BindView(R.id.text_movie_review_content)
        lateinit var content: TextView
        @BindView(R.id.text_movie_review_author)
        lateinit var author: TextView

        private val onItemClickListener: MoviesAdapter.OnItemClickListener?

        constructor(itemView: View, onItemClickListener: MoviesAdapter.OnItemClickListener) : super(itemView) {

            ButterKnife.bind(this, itemView)
            this.onItemClickListener = onItemClickListener
            itemView.setOnClickListener(this)
        }

        override fun onClick(view: View) {
            if (onItemClickListener != null) {
                onItemClickListener.onItemClick(view, adapterPosition)
            }
        }
    }
}