package vn.kingbee.movie.data

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import vn.kingbee.movie.api.DiscoverAndSearchResponse
import vn.kingbee.movie.api.TheMovieDbService
import vn.kingbee.movie.model.Movie
import vn.kingbee.movie.model.SortHelper
import javax.inject.Inject

class MoviesService {

    private val sortHelper: SortHelper
    private val context: Context
    @Volatile
    var loading = false

    private val theMovieDbService: TheMovieDbService

    @Inject
    constructor(context: Context, theMovieDbService: TheMovieDbService, sortHelper: SortHelper) {
        this.context = context.applicationContext
        this.sortHelper = sortHelper
        this.theMovieDbService = theMovieDbService
    }

    fun refreshMovies() {
        if (loading) {
            return
        }
        loading = true
        val sort = sortHelper.getSortByPreference().toString()
        callDiscoverMovies(sort, null)
    }

    fun loadMoreMovies() {
        if (loading) {
            return
        }
        loading = true
        val sort = sortHelper.getSortByPreference().toString()
        val uri = sortHelper.getSortMoviesUri() ?: return
        callDiscoverMovies(sort, getCurrentPage(uri) + 1)
    }

    private fun callDiscoverMovies(sort: String, page: Int?) {
        theMovieDbService.discoverMovies(sort, page)
            .subscribeOn(Schedulers.newThread())
            .doOnNext { discoverMoviesResponse -> clearMoviesSortTableIfNeeded(discoverMoviesResponse) }
            .doOnNext { discoverMoviesResponse -> logResponse(discoverMoviesResponse) }
            .map { discoverMoviesResponse -> discoverMoviesResponse.results }
            .flatMap { movies -> Observable.fromIterable(movies) }
            .map { movie -> saveMovie(movie) }
            .map { movieUri -> MoviesContract.MovieEntry.getIdFromUri(movieUri) }
            .doOnNext { movieId -> saveMoveReference(movieId) }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<Long> {
                override fun onSubscribe(d: Disposable) {
                    //do nothing
                }

                override fun onComplete() {
                    loading = false
                    sendUpdateFinishedBroadcast(true)
                }

                override fun onNext(t: Long) {
                    //do nothing
                }

                override fun onError(e: Throwable) {
                    loading = false
                    sendUpdateFinishedBroadcast(false)
                    Timber.d("Error: $e")
                }

            })
    }

    private fun saveMoveReference(movieId: Long) {
        val entry = ContentValues()
        entry.put(MoviesContract.COLUMN_MOVIE_ID_KEY, movieId)
        context.contentResolver.insert(sortHelper.getSortMoviesUri()!!, entry)
    }

    private fun saveMovie(movie: Movie?): Uri? {
        return context.contentResolver.insert(MoviesContract.MovieEntry.CONTENT_URI, movie?.toContentValues())
    }

    private fun logResponse(discoverMoviesResponse: DiscoverAndSearchResponse<Movie>) {
        Timber.d("$LOG_TAG page == ${discoverMoviesResponse.page} ${discoverMoviesResponse.results}")
    }

    private fun clearMoviesSortTableIfNeeded(discoverMoviesResponse: DiscoverAndSearchResponse<Movie>) {
        if (discoverMoviesResponse.page == 1) {
            context.contentResolver.delete(sortHelper.getSortMoviesUri()!!, null, null)
        }
    }

    private fun sendUpdateFinishedBroadcast(successfulUpdated: Boolean) {
        val intent = Intent(BROADCAST_UPDATE_FINISHED)
        intent.putExtra(EXTRA_IS_SUCCESSFUL_UPDATED, successfulUpdated)
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent)
    }

    private fun getCurrentPage(uri: Uri): Int {
        val movies = context.contentResolver.query(uri, null, null, null, null)

        var currentPage = 1
        if (movies != null) {
            currentPage = (movies.count - 1) / PAGE_SIZE + 1
            movies.close()
        }
        return currentPage
    }

    companion object {
        const val BROADCAST_UPDATE_FINISHED = "UpdateFinished"
        const val EXTRA_IS_SUCCESSFUL_UPDATED = "isSuccessfulUpdated"

        private const val PAGE_SIZE = 20
        private const val LOG_TAG = "MoviesService"
    }
}