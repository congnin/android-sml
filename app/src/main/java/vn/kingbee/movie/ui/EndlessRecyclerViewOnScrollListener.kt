package vn.kingbee.movie.ui

import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.GridLayoutManager


abstract class EndlessRecyclerViewOnScrollListener(private val gridLayoutManager: GridLayoutManager) : RecyclerView.OnScrollListener() {
    private var loading = false

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)

        val totalItemCount = gridLayoutManager.itemCount
        val lastVisibleItemPosition = gridLayoutManager.findLastVisibleItemPosition()

        val endHasBeenReached = lastVisibleItemPosition + VISIBLE_THRESHOLD >= totalItemCount
        if (!loading && totalItemCount > 0 && endHasBeenReached) {
            loading = true
            onLoadMore()
        }
    }

    fun setLoading(loading: Boolean) {
        this.loading = loading
    }

    abstract fun onLoadMore()

    companion object {

        private const val VISIBLE_THRESHOLD = 5
    }
}