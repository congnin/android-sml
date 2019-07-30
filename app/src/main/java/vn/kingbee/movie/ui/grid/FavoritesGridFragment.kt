package vn.kingbee.movie.ui.grid

import android.database.Cursor
import android.net.Uri
import vn.kingbee.movie.data.MoviesContract

class FavoritesGridFragment : AbstractMoviesGridFragment() {
    override fun getContentUri(): Uri = MoviesContract.Favorites.CONTENT_URI

    override fun onCursorLoaded(data: Cursor?) {
        getAdapter()?.changeCursor(data)
    }

    override fun onRefreshAction() {
        swipeRefreshLayout.isRefreshing = false
    }

    override fun onMoviesGridInitialisationFinished() {
        //do nothing
    }

    companion object {
        fun create(): FavoritesGridFragment = FavoritesGridFragment()
    }
}