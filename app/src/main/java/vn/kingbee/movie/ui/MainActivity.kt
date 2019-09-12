package vn.kingbee.movie.ui

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ScrollView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import butterknife.BindView
import butterknife.ButterKnife
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar
import vn.kingbee.movie.model.Movie
import vn.kingbee.movie.model.SortHelper
import vn.kingbee.widget.R
import javax.inject.Inject
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import butterknife.OnClick
import butterknife.Optional
import com.google.android.material.floatingactionbutton.FloatingActionButton
import vn.kingbee.application.MyApp
import vn.kingbee.movie.data.FavoritesService
import vn.kingbee.movie.ui.detail.MovieDetailActivity
import vn.kingbee.movie.ui.detail.MovieDetailFragment
import vn.kingbee.movie.ui.grid.FavoritesGridFragment
import vn.kingbee.movie.ui.grid.MoviesGridFragment
import vn.kingbee.movie.util.OnItemSelectedListener
import java.util.*

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener,
    OnItemSelectedListener {

    @BindView(R.id.drawer_layout)
    lateinit var drawerLayout: DrawerLayout

    @BindView(R.id.navigation_view)
    lateinit var navigationView: NavigationView

    @BindView(R.id.coordinator_layout)
    lateinit var coordinatorLayout: CoordinatorLayout

    @BindView(R.id.toolbar)
    lateinit var toolbar: Toolbar

    @JvmField
    @BindView(R.id.movie_detail_container)
    var movieDetailContainer: ScrollView? = null

    @JvmField
    @BindView(R.id.fab)
    var fab: FloatingActionButton? = null

    @Inject
    lateinit var sortHelper: SortHelper
    @Inject
    lateinit var favoritesService: FavoritesService

    private var twoPaneMode: Boolean? = null
    private var selectedMovie: Movie? = null
    private var selectedNavigationItem: Int = 0

    private val broadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val action = intent?.action
            if (action == SortingDialogFragment.BROADCAST_SORT_PREFERENCE_CHANGED) {
                hideMovieDetailContainer()
                updateTitle()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        ButterKnife.bind(this)

        MyApp.getInstance().networkComponent.inject(this)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.movies_grid_container, MoviesGridFragment.create())
                .commit()
        }
        twoPaneMode = movieDetailContainer != null
        if (twoPaneMode != null && twoPaneMode!! && selectedMovie == null) {
            movieDetailContainer?.visibility = View.GONE
        }
        setupToolbar()
        setupNavigationDrawer()
        setupNavigationView()
        setupFab()
    }

    override fun onResume() {
        super.onResume()
        val intentFilter = IntentFilter(SortingDialogFragment.BROADCAST_SORT_PREFERENCE_CHANGED)
        LocalBroadcastManager.getInstance(this).registerReceiver(broadcastReceiver, intentFilter)
        updateTitle()
    }

    override fun onPause() {
        super.onPause()
        LocalBroadcastManager.getInstance(this).unregisterReceiver(broadcastReceiver)
    }

    private fun updateTitle() {
        if (selectedNavigationItem == 0) {
            val sortTitles = resources.getStringArray(R.array.pref_sort_by_labels)
            val currentSortIndex = sortHelper.getSortByPreference().ordinal
            val title = sortTitles[currentSortIndex][0].toString().toUpperCase(Locale.US) +
                    sortTitles[currentSortIndex].substring(1)
            setTitle(title)
        } else if (selectedNavigationItem == 1) {
            title = resources.getString(R.string.favorites_grid_title)
        }
    }

    private fun setupFab() {
        if (fab != null) {
            if (twoPaneMode != null && twoPaneMode!! && selectedMovie != null) {
                if (favoritesService.isFavorite(selectedMovie!!)) {
                    fab?.setImageResource(R.mipmap.ic_favorite_white)
                } else {
                    fab?.setImageResource(R.mipmap.ic_favorite_white_border)
                }
                fab?.show()
            } else {
                fab?.hide()
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelable(SELECTED_MOVIE_KEY, selectedMovie)
        outState.putInt(SELECTED_NAVIGATION_ITEM_KEY, selectedNavigationItem)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle?) {
        super.onRestoreInstanceState(savedInstanceState)
        if (savedInstanceState != null) {
            selectedMovie = savedInstanceState.getParcelable(SELECTED_MOVIE_KEY)
            selectedNavigationItem = savedInstanceState.getInt(SELECTED_NAVIGATION_ITEM_KEY)
            val menu = navigationView.menu
            menu.getItem(selectedNavigationItem).isChecked = true
        }
    }

    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawers()
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.activity_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        val id = item?.itemId
        if (id == R.id.action_settings) {
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onItemSelected(movie: Movie) {
        if (twoPaneMode != null && twoPaneMode!! && movieDetailContainer != null) {
            movieDetailContainer?.visibility = View.VISIBLE
            selectedMovie = movie
            supportFragmentManager.beginTransaction()
                .replace(R.id.movie_detail_container, MovieDetailFragment.create(movie))
                .commit()
            setupFab()
        } else {
            MovieDetailActivity.start(this, movie)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        item.isCheckable = true
        when (item.itemId) {
            R.id.drawer_item_explore -> {
                if (selectedNavigationItem != 0) {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.movies_grid_container, MoviesGridFragment.create())
                        .commit()
                    selectedNavigationItem = 0
                    hideMovieDetailContainer()
                }
                drawerLayout.closeDrawers()
                updateTitle()
                return true
            }

            R.id.drawer_item_favorites -> {
                if (selectedNavigationItem != 1) {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.movies_grid_container, FavoritesGridFragment.create())
                        .commit()
                    selectedNavigationItem = 1
                    hideMovieDetailContainer()
                }
                drawerLayout.closeDrawers()
                updateTitle()
                return true
            }
            else -> return false
        }

    }

    @Optional
    @OnClick(R.id.fab)
    fun onFabClicked() {
        if (selectedMovie != null) {
            if (favoritesService.isFavorite(selectedMovie!!)) {
                favoritesService.removeFromFavorites(selectedMovie!!)
                showSnackbar(R.string.message_removed_from_favorites)
                if (selectedNavigationItem == 1) {
                    hideMovieDetailContainer()
                }
            } else {
                favoritesService.addToFavorites(selectedMovie!!)
                showSnackbar(R.string.message_added_to_favorites)
            }
        }

        setupFab()
    }

    private fun showSnackbar(message: String) {
        Snackbar.make(coordinatorLayout, message, Snackbar.LENGTH_LONG).show()
    }

    private fun showSnackbar(@StringRes messageResourceId: Int) {
        showSnackbar(getString(messageResourceId))
    }

    private fun hideMovieDetailContainer() {
        selectedMovie = null
        setupFab()
        if (twoPaneMode != null && twoPaneMode!! && movieDetailContainer != null) {
            movieDetailContainer?.visibility = View.GONE
        }
    }

    private fun setupToolbar() {
        setSupportActionBar(toolbar)
        toolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.white))
    }

    private fun setupNavigationDrawer() {
        if (supportActionBar != null) {
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }
        toolbar.setNavigationIcon(R.mipmap.ic_menu)
        toolbar.setNavigationOnClickListener { _ -> drawerLayout.openDrawer(GravityCompat.START) }
    }

    private fun setupNavigationView() {
        navigationView.setNavigationItemSelectedListener(this)
    }

    companion object {
        private const val SELECTED_MOVIE_KEY = "MovieSelected"
        private const val SELECTED_NAVIGATION_ITEM_KEY = "SelectedNavigationItem"
    }
}