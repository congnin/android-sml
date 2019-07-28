package vn.kingbee.movie.model

import android.content.SharedPreferences
import android.net.Uri
import vn.kingbee.movie.data.MoviesContract
import javax.inject.Inject

class SortHelper {

    private val sharedPreferences: SharedPreferences

    @Inject
    constructor(sharedPreferences: SharedPreferences) {
        this.sharedPreferences = sharedPreferences
    }

    fun getSortByPreference(): Sort {
        val sort = sharedPreferences.getString(PREF_SORT_BY_KEY, PREF_SORT_BY_DEFAULT_VALUE)
        return Sort.fromString(sort!!)
    }

    fun getSortMoviesUri(): Uri? {
        return when (getSortByPreference()) {
            Sort.MOST_POPULAR -> MoviesContract.MostPopularMovies.CONTENT_URI
            Sort.HIGHEST_RATED -> MoviesContract.HighestRatedMovies.CONTENT_URI
            Sort.MOST_RATED -> MoviesContract.MostRatedMovies.CONTENT_URI
        }
    }

    fun saveSortByPreference(sort: Sort): Boolean {
        val editor = sharedPreferences.edit()
        editor.putString(PREF_SORT_BY_KEY, sort.toString())
        return editor.commit()
    }

    companion object {
        private const val PREF_SORT_BY_KEY = "sortBy"
        private const val PREF_SORT_BY_DEFAULT_VALUE = "popularity.desc"
    }
}