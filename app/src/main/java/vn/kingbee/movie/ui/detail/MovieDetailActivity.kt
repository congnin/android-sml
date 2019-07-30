package vn.kingbee.movie.ui.detail

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.DisplayMetrics
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.ViewCompat
import androidx.core.widget.NestedScrollView
import butterknife.BindView
import butterknife.ButterKnife
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.appbar.CollapsingToolbarLayout
import vn.kingbee.application.MyApp
import vn.kingbee.movie.model.Movie
import vn.kingbee.widget.R
import javax.inject.Inject
import com.google.android.material.snackbar.Snackbar
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import butterknife.OnClick
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import vn.kingbee.movie.data.FavoritesService


class MovieDetailActivity : AppCompatActivity() {

    @BindView(R.id.coordinator_layout)
    lateinit var coordinatorLayout: CoordinatorLayout
    @BindView(R.id.collapsing_toolbar_layout)
    lateinit var collapsingToolbarLayout: CollapsingToolbarLayout
    @BindView(R.id.toolbar)
    lateinit var toolbar: Toolbar
    @BindView(R.id.backdrop_image)
    lateinit var movieBackdropImage: ImageView
    @BindView(R.id.fab)
    lateinit var fab: FloatingActionButton
    @BindView(R.id.nestedScrollView)
    lateinit var nestedScrollView: NestedScrollView

    @Inject
    lateinit var favoritesService: FavoritesService

    private var movie: Movie? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie_detail)
        ButterKnife.bind(this)
        movie = intent.getParcelableExtra(ARG_MOVIE)

        MyApp.getInstance().networkComponent.inject(this)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .add(R.id.movies_grid_container, MovieDetailFragment.create(movie))
                .commit()
        }
        initToolbar()
        ViewCompat.setElevation(nestedScrollView,
            convertDpToPixel(resources.getInteger(R.integer.movie_detail_content_elevation_in_dp).toFloat()))
        ViewCompat.setElevation(fab,
            convertDpToPixel(resources.getInteger(R.integer.movie_detail_fab_elevation_in_dp).toFloat()))
        updateFab()
    }

    @OnClick(R.id.fab)
    fun onFabClicked() {
        if (favoritesService.isFavorite(movie!!)) {
            favoritesService.removeFromFavorites(movie!!)
            showSnackbar(R.string.message_removed_from_favorites)
        } else {
            favoritesService.addToFavorites(movie!!)
            showSnackbar(R.string.message_added_to_favorites)
        }
        updateFab()
    }

    private fun updateFab() {
        if (favoritesService.isFavorite(movie!!)) {
            fab.setImageResource(R.mipmap.ic_favorite_white)
        } else {
            fab.setImageResource(R.mipmap.ic_favorite_white_border)
        }
    }

    private fun initToolbar() {
        setSupportActionBar(toolbar)
        if (supportActionBar != null) {
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            supportActionBar?.setDisplayShowHomeEnabled(true)
            toolbar.setNavigationOnClickListener { _ -> onBackPressed() }
        }
        collapsingToolbarLayout.title = movie?.title
        collapsingToolbarLayout.setExpandedTitleColor(ContextCompat.getColor(this, android.R.color.transparent))
        title = ""
        Glide.with(this)
            .load(POSTER_IMAGE_BASE_URL + BACKDROP_IMAGE_SIZE + movie?.backdropPath)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(movieBackdropImage)
    }

    private fun showSnackbar(message: String) {
        Snackbar.make(coordinatorLayout, message, Snackbar.LENGTH_LONG).show()
    }

    private fun showSnackbar(@StringRes messageResourceId: Int) {
        showSnackbar(getString(messageResourceId))
    }

    private fun convertDpToPixel(dp: Float): Float {
        val metrics = resources.displayMetrics
        return dp * metrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEVICE_STABLE
    }

    companion object {
        private const val ARG_MOVIE = "argMovie"
        private const val POSTER_IMAGE_BASE_URL = "https://image.tmdb.org/t/p/"
        private const val BACKDROP_IMAGE_SIZE = "w780"

        fun start(context: Context, movie: Movie) {
            val intent = Intent(context, MovieDetailActivity::class.java)
            intent.putExtra(ARG_MOVIE, movie)
            context.startActivity(intent)
        }
    }
}