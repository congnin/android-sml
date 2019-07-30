package vn.kingbee.movie.ui.detail

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import vn.kingbee.movie.api.TheMovieDbService
import javax.inject.Inject
import androidx.recyclerview.widget.RecyclerView
import butterknife.BindView
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import butterknife.ButterKnife
import vn.kingbee.application.MyApp
import vn.kingbee.movie.model.Movie
import vn.kingbee.widget.R
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.Glide
import java.util.*
import android.util.DisplayMetrics
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.trello.rxlifecycle3.components.support.RxFragment
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import vn.kingbee.movie.api.MovieVideosResponse
import vn.kingbee.movie.model.MovieReview
import vn.kingbee.movie.model.MovieVideo
import vn.kingbee.movie.ui.ItemOffsetDecoration
import vn.kingbee.movie.ui.grid.MoviesAdapter

class MovieDetailFragment : RxFragment() {
    @BindView(R.id.image_movie_detail_poster)
    lateinit var movieImagePoster: ImageView
    @BindView(R.id.text_movie_original_title)
    lateinit var movieOriginalTitle: TextView
    @BindView(R.id.text_movie_user_rating)
    lateinit var movieUserRating: TextView
    @BindView(R.id.text_movie_release_date)
    lateinit var movieReleaseDate: TextView
    @BindView(R.id.text_movie_overview)
    lateinit var movieOverview: TextView
    @BindView(R.id.card_movie_detail)
    lateinit var cardMovieDetail: CardView
    @BindView(R.id.card_movie_overview)
    lateinit var cardMovieOverview: CardView

    @BindView(R.id.card_movie_videos)
    lateinit var cardMovieVideos: CardView
    @BindView(R.id.movie_videos)
    lateinit var movieVideos: RecyclerView

    @BindView(R.id.card_movie_reviews)
    lateinit var cardMovieReviews: CardView
    @BindView(R.id.movie_reviews)
    lateinit var movieReviews: RecyclerView

    @Inject
    lateinit var theMovieDbService: TheMovieDbService

    private var movie: Movie? = null
    private var videosAdapter: MovieVideosAdapter? = null
    private var reviewsAdapter: MovieReviewsAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            movie = arguments?.getParcelable(ARG_MOVIE)
        }

        MyApp.getInstance().networkComponent.inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_movie_detail, container, false)
        ButterKnife.bind(this, rootView)
        initViews()
        initVideosList()
        initReviewsList()
        setupCardElevation()
        return rootView
    }

    override fun onResume() {
        super.onResume()
        if (videosAdapter?.itemCount == 0) {
            loadMovieVideos()
        }
        if (reviewsAdapter?.itemCount == 0) {
            loadMovieReviews()
        }
        updateMovieVideosCard()
        updateMovieReviewsCard()
    }

    private fun setupCardElevation() {
        setupCardElevation(cardMovieDetail)
        setupCardElevation(cardMovieVideos)
        setupCardElevation(cardMovieOverview)
        setupCardElevation(cardMovieReviews)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        if (videosAdapter?.itemCount != 0) {
            outState.putParcelableArrayList(MOVIE_VIDEOS_KEY, videosAdapter?.getMovieVideos() as ArrayList<out Parcelable>)
        }
        if (reviewsAdapter?.itemCount != 0) {
            outState.putParcelableArrayList(MOVIE_REVIEWS_KEY, reviewsAdapter?.getMovieReviews() as ArrayList<out Parcelable>)
        }
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        if (savedInstanceState != null) {
            videosAdapter?.setMovieVideos(savedInstanceState.getParcelableArrayList(MOVIE_VIDEOS_KEY))
            reviewsAdapter?.setMovieReviews(savedInstanceState.getParcelableArrayList(MOVIE_REVIEWS_KEY))
        }
    }

    private fun updateMovieVideosCard() {
        if (videosAdapter == null || videosAdapter?.itemCount == 0) {
            cardMovieVideos.visibility = View.GONE
        } else {
            cardMovieVideos.visibility = View.VISIBLE
        }
    }

    private fun updateMovieReviewsCard() {
        if (reviewsAdapter == null || reviewsAdapter?.itemCount == 0) {
            cardMovieReviews.visibility = View.GONE
        } else {
            cardMovieReviews.visibility = View.VISIBLE
        }
    }

    private fun setupCardElevation(view: View) {
        ViewCompat.setElevation(view,
            convertDpToPixel(resources.getInteger(R.integer.movie_detail_content_elevation_in_dp).toFloat()))
    }

    private fun loadMovieVideos() {
        theMovieDbService.getMovieVideos(movie!!.id)
            .compose(bindToLifecycle())
            .subscribeOn(Schedulers.newThread())
            .map { response -> response.results }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<List<MovieVideo>?> {
                override fun onComplete() {
                    //do nothing
                }

                override fun onSubscribe(d: Disposable) {
                    //do nothing
                }

                override fun onNext(t: List<MovieVideo>) {
                    videosAdapter?.setMovieVideos(t)
                    updateMovieVideosCard()
                }

                override fun onError(e: Throwable) {
                    Timber.e("$LOG_TAG ${e.message}")
                    updateMovieVideosCard()
                }
            })
    }

    private fun loadMovieReviews() {
        theMovieDbService.getMovieReviews(movie!!.id)
            .compose(bindToLifecycle())
            .subscribeOn(Schedulers.newThread())
            .map { response -> response.results }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<List<MovieReview>?> {
                override fun onComplete() {
                    //do nothing
                }

                override fun onSubscribe(d: Disposable) {
                    //do nothing
                }

                override fun onNext(t: List<MovieReview>) {
                    reviewsAdapter?.setMovieReviews(t)
                    updateMovieReviewsCard()
                }

                override fun onError(e: Throwable) {
                    Timber.e("$LOG_TAG ${e.message}")
                    updateMovieReviewsCard()
                }
            })
    }

    private fun initViews() {
        Glide.with(this)
            .load(POSTER_IMAGE_BASE_URL + POSTER_IMAGE_SIZE + movie?.posterPath)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(movieImagePoster)
        movieOriginalTitle.text = movie?.originalTitle
        movieUserRating.text = String.format(Locale.US, "%.1f", movie?.averageVote)
        movieUserRating.setTextColor(getRatingColor(movie?.averageVote!!))
        val releaseDate = String.format(getString(R.string.movie_detail_release_date),
            movie?.releaseDate)
        movieReleaseDate.text = releaseDate
        movieOverview.text = movie?.overview
    }

    @ColorInt
    private fun getRatingColor(averageVote: Double): Int {
        return when {
            averageVote >= VOTE_PERFECT -> ContextCompat.getColor(context!!, R.color.vote_perfect)
            averageVote >= VOTE_GOOD -> ContextCompat.getColor(context!!, R.color.vote_good)
            averageVote >= VOTE_NORMAL -> ContextCompat.getColor(context!!, R.color.vote_normal)
            else -> ContextCompat.getColor(context!!, R.color.vote_bad)
        }
    }

    private fun initVideosList() {
        videosAdapter = MovieVideosAdapter(context!!)
        videosAdapter?.setOnItemClickListener(object : MoviesAdapter.OnItemClickListener {
            override fun onItemClick(itemView: View, position: Int) {
                onMovieVideoClicked(position)
            }
        })
        movieVideos.adapter = videosAdapter
        movieVideos.itemAnimator = DefaultItemAnimator()
        movieVideos.addItemDecoration(ItemOffsetDecoration(activity!!, R.dimen.movie_item_offset))
        val layoutManager = LinearLayoutManager(context,
            LinearLayoutManager.HORIZONTAL, false)
        movieVideos.layoutManager = layoutManager
    }

    private fun initReviewsList() {
        reviewsAdapter = MovieReviewsAdapter()
        reviewsAdapter?.setOnItemClickListener(object : MoviesAdapter.OnItemClickListener {
            override fun onItemClick(itemView: View, position: Int) {
                onMovieReviewClicked(position)
            }

        })
        movieReviews.adapter = reviewsAdapter
        movieReviews.itemAnimator = DefaultItemAnimator()
        val layoutManager = LinearLayoutManager(context)
        movieReviews.layoutManager = layoutManager
    }

    private fun onMovieVideoClicked(position: Int) {
        val video = videosAdapter?.getItem(position)
        if (video != null && video.isYoutubeVideo) {
            val intent = Intent(Intent.ACTION_VIEW,
                Uri.parse("http://www.youtube.com/watch?v=" + video.key))
            startActivity(intent)
        }
    }

    private fun onMovieReviewClicked(position: Int) {
        val review = reviewsAdapter?.getItem(position);
        if (review?.reviewUrl != null) {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(review.reviewUrl));
            startActivity(intent);
        }
    }

    private fun convertDpToPixel(dp: Float): Float {
        val metrics = resources.displayMetrics
        return dp * (metrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)
    }

    companion object {
        private const val POSTER_IMAGE_BASE_URL = "https://image.tmdb.org/t/p/"
        private const val POSTER_IMAGE_SIZE = "w780"

        private const val ARG_MOVIE = "ArgMovie"
        private const val MOVIE_VIDEOS_KEY = "MovieVideos"
        private const val MOVIE_REVIEWS_KEY = "MovieReviews"
        private const val LOG_TAG = "MovieDetailFragment"

        private const val VOTE_PERFECT = 9.0
        private const val VOTE_GOOD = 7.0
        private const val VOTE_NORMAL = 5.0

        fun create(movie: Movie?): MovieDetailFragment {
            val fragment = MovieDetailFragment()
            val args = Bundle()
            args.putParcelable(ARG_MOVIE, movie)
            fragment.arguments = args
            return fragment
        }
    }
}