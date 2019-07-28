package vn.kingbee.movie.ui

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
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
import vn.kingbee.application.MyApp
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

    @Inject
    lateinit var sortHelper: SortHelper

    private var twoPaneMode: Boolean? = null
    private var selectedMovie: Movie? = null
    private var selectedNavigationItem: Int? = null

    private val broadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val action = intent?.action

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
        setupToolbar()
        setupNavigationDrawer()
        setupNavigationView()
        setupFab()
    }

    override fun onResume() {
        super.onResume()
        updateTitle()
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
//        if (fab != null) {
//            if (twoPaneMode != null && twoPaneMode!! && selectedMovie != null) {
//
//            } else {
//                fab?.hide()
//            }
//        }
    }

    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawers()
        } else {
            super.onBackPressed()
        }
    }

    override fun onItemSelected(movie: Movie) {

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
                drawerLayout.closeDrawers()
                updateTitle()
                return true
            }
            else -> return false
        }

    }

    private fun showSnackbar(message: String) {
        Snackbar.make(coordinatorLayout, message, Snackbar.LENGTH_LONG).show()
    }

    private fun showSnackbar(@StringRes messageResourceId: Int) {
        showSnackbar(getString(messageResourceId))
    }

    private fun hideMovieDetailContainer() {

    }

    private fun setupToolbar() {
        setSupportActionBar(toolbar)
    }

    private fun setupNavigationDrawer() {
        if (supportActionBar != null) {
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }
        toolbar.setNavigationIcon(R.mipmap.ic_menu)
        toolbar.setNavigationOnClickListener { view -> drawerLayout.openDrawer(GravityCompat.START) }
    }

    private fun setupNavigationView() {
        navigationView.setNavigationItemSelectedListener(this)
    }

    companion object {
        private const val SELECTED_MOVIE_KEY = "MovieSelected"
        private const val SELECTED_NAVIGATION_ITEM_KEY = "SelectedNavigationItem"
    }
}