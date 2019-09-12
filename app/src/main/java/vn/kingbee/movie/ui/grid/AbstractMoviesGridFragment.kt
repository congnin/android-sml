package vn.kingbee.movie.ui.grid

import android.annotation.SuppressLint
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.widget.RelativeLayout
import androidx.fragment.app.Fragment
import androidx.loader.app.LoaderManager
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import butterknife.BindView
import vn.kingbee.movie.util.OnItemSelectedListener
import vn.kingbee.widget.R
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.loader.content.CursorLoader
import androidx.loader.content.Loader
import androidx.recyclerview.widget.DefaultItemAnimator
import butterknife.ButterKnife
import vn.kingbee.movie.ui.ItemOffsetDecoration


abstract class AbstractMoviesGridFragment() : Fragment(), LoaderManager.LoaderCallbacks<Cursor>,
    SwipeRefreshLayout.OnRefreshListener, MoviesAdapter.OnItemClickListener {

    @BindView(R.id.swipe_layout)
    lateinit var swipeRefreshLayout: SwipeRefreshLayout
    @BindView(R.id.movies_grid)
    lateinit var recyclerView: RecyclerView
    @BindView(R.id.view_no_movies)
    lateinit var noMoviesView: RelativeLayout

    private var adapter: MoviesAdapter? = null

    private var onItemSelectedListener: OnItemSelectedListener? = null
    private var gridLayoutManager: GridLayoutManager? = null

    protected abstract fun getContentUri(): Uri

    protected abstract fun onCursorLoaded(data: Cursor?)

    protected abstract fun onRefreshAction()

    protected abstract fun onMoviesGridInitialisationFinished()

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        LoaderManager.getInstance(this).initLoader(LOADER_ID, null, this)
        super.onActivityCreated(savedInstanceState)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnItemSelectedListener) {
            onItemSelectedListener = context
        } else {
            throw IllegalStateException("The activity must implement " +
                    OnItemSelectedListener::class.java.simpleName + " interface.")
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_movies_grid, container, false)
        ButterKnife.bind(this, rootView)

        initSwipeRefreshLayout()
        recyclerView.addItemDecoration(ItemOffsetDecoration(context!!, R.dimen.movie_item_offset))
        initMoviesGrid()

        return rootView
    }

    override fun onResume() {
        super.onResume()
        updateGridLayout()
    }

    fun getOnItemSelectedListener(): OnItemSelectedListener? = onItemSelectedListener

    fun restartLoader() {
        LoaderManager.getInstance(this).restartLoader(LOADER_ID, null, this@AbstractMoviesGridFragment)
    }

    protected fun updateGridLayout() {
        if (recyclerView.adapter == null || recyclerView.adapter?.itemCount == 0) {
            recyclerView.visibility = View.GONE
            noMoviesView.visibility = View.VISIBLE
        } else {
            recyclerView.visibility = View.VISIBLE
            noMoviesView.visibility = View.GONE
        }
    }

    protected fun initMoviesGrid() {
        adapter = MoviesAdapter(context!!, null)
        adapter?.setOnItemClickListener(this)
        recyclerView.adapter = adapter
        recyclerView.itemAnimator = DefaultItemAnimator()
        val columns = resources.getInteger(R.integer.movies_columns)
        gridLayoutManager = GridLayoutManager(activity, columns)
        recyclerView.layoutManager = gridLayoutManager
        onMoviesGridInitialisationFinished()
    }

    @SuppressLint("PrivateResource")
    private fun initSwipeRefreshLayout() {
        swipeRefreshLayout.setOnRefreshListener(this)
        swipeRefreshLayout.setColorSchemeResources(R.color.primary_dark_material_dark,
            R.color.accent_material_light)
    }

    override fun onCreateLoader(id: Int, args: Bundle?): Loader<Cursor> {
        return CursorLoader(activity!!, getContentUri(), null, null, null, null)
    }

    override fun onLoadFinished(loader: Loader<Cursor>, data: Cursor?) {
        onCursorLoaded(data)
        updateGridLayout()
    }

    override fun onLoaderReset(loader: Loader<Cursor>) {
        adapter?.changeCursor(null)
        updateGridLayout()
    }

    override fun onItemClick(itemView: View, position: Int) {
        onItemSelectedListener?.onItemSelected(adapter?.getItem(position)!!)
    }

    override fun onRefresh() {
        onRefreshAction()
    }

    fun getAdapter(): MoviesAdapter? = adapter

    fun getGridLayoutManager(): GridLayoutManager? = gridLayoutManager

    companion object {
        private const val LOADER_ID = 0
    }
}