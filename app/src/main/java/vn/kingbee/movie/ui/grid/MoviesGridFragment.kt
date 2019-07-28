package vn.kingbee.movie.ui.grid

import android.app.SearchManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.appcompat.widget.SearchView
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.google.android.material.snackbar.Snackbar
import timber.log.Timber
import vn.kingbee.application.MyApp
import vn.kingbee.movie.api.TheMovieDbService
import vn.kingbee.movie.data.MoviesService
import vn.kingbee.movie.model.SortHelper
import vn.kingbee.movie.ui.EndlessRecyclerViewOnScrollListener
import vn.kingbee.movie.ui.SortingDialogFragment
import vn.kingbee.widget.R
import javax.inject.Inject

class MoviesGridFragment : AbstractMoviesGridFragment() {

    @Inject
    lateinit var moviesService: MoviesService
    @Inject
    lateinit var sortHelper: SortHelper
    @Inject
    lateinit var theMovieDbService: TheMovieDbService

    private var endlessRecyclerViewOnScrollListener: EndlessRecyclerViewOnScrollListener? = null
    private var searchView: SearchView? = null

    private val broadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val action = intent?.action
            if (action == MoviesService.BROADCAST_UPDATE_FINISHED) {
                if (!intent.getBooleanExtra(MoviesService.EXTRA_IS_SUCCESSFUL_UPDATED, true)) {
                    Snackbar.make(swipeRefreshLayout, R.string.error_failed_to_update_movies,
                        Snackbar.LENGTH_LONG).show()
                }
                swipeRefreshLayout.isRefreshing = false
                endlessRecyclerViewOnScrollListener?.setLoading(false)
                updateGridLayout()
            } else if (action == SortingDialogFragment.BROADCAST_SORT_PREFERENCE_CHANGED) {
                recyclerView.smoothScrollToPosition(0)
                restartLoader()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)

        MyApp.getInstance().networkComponent.inject(this)
    }

    override fun onResume() {
        super.onResume()
        val intentFilter = IntentFilter()
        intentFilter.addAction(MoviesService.BROADCAST_UPDATE_FINISHED)
        intentFilter.addAction(SortingDialogFragment.BROADCAST_SORT_PREFERENCE_CHANGED)
        LocalBroadcastManager.getInstance(activity!!).registerReceiver(broadcastReceiver, intentFilter)
        if (endlessRecyclerViewOnScrollListener != null) {
            endlessRecyclerViewOnScrollListener?.setLoading(moviesService.loading)
        }
        swipeRefreshLayout.isRefreshing = moviesService.loading
    }

    override fun onPause() {
        super.onPause()
        LocalBroadcastManager.getInstance(activity!!).unregisterReceiver(broadcastReceiver)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.fragment_movies_grid, menu)

        val searchViewMenuItem = menu.findItem(R.id.action_search)
        if (searchViewMenuItem != null) {
            searchView = searchViewMenuItem.actionView as SearchView?
            searchViewMenuItem.setOnActionExpandListener(object : MenuItem.OnActionExpandListener {
                override fun onMenuItemActionExpand(item: MenuItem?): Boolean {
                    recyclerView.adapter = null
                    initMoviesGrid()
                    restartLoader()
                    swipeRefreshLayout.isEnabled = true
                    return true
                }

                override fun onMenuItemActionCollapse(item: MenuItem?): Boolean {
                    return true
                }
            })
            setupSearchView()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        return when (id) {
            R.id.action_show_sort_by_dialog -> {
                showSortByDialog()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun getContentUri(): Uri = sortHelper.getSortMoviesUri()!!

    override fun onCursorLoaded(data: Cursor?) {
        getAdapter()?.changeCursor(data)
        if (data != null && data.moveToFirst()) {
            val tempt = data.getString(data.getColumnIndex("original_title"))
            Timber.d("$LOG_TAG $tempt")
        }
        if (data == null || data.count == 0) {
            refreshMovies()
        }
    }

    override fun onRefreshAction() {
        refreshMovies()
    }

    override fun onMoviesGridInitialisationFinished() {
        endlessRecyclerViewOnScrollListener = object : EndlessRecyclerViewOnScrollListener(getGridLayoutManager()!!) {
            override fun onLoadMore() {
                swipeRefreshLayout.isRefreshing = true
                moviesService.loadMoreMovies()
            }
        }
        recyclerView.addOnScrollListener(endlessRecyclerViewOnScrollListener!!)
    }

    private fun setupSearchView() {
        if (searchView == null) {
            Timber.e("$LOG_TAG SearchView is not initialized")
            return
        }
        val searchManager = activity?.getSystemService(Context.SEARCH_SERVICE) as SearchManager
        searchView?.setSearchableInfo(searchManager.getSearchableInfo(activity?.componentName))


    }

    private fun refreshMovies() {
        swipeRefreshLayout.isRefreshing = true
        moviesService.refreshMovies()
    }

    private fun showSortByDialog() {
        val sortingDialogFragment = SortingDialogFragment()
        sortingDialogFragment.show(fragmentManager!!, SortingDialogFragment.TAG)
    }

    companion object {
        private const val LOG_TAG = "MoviesGridFragment"
        private const val SEARCH_QUERY_DELAY_MILLIS = 400

        fun create(): MoviesGridFragment = MoviesGridFragment()
    }
}