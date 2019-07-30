package vn.kingbee.movie.data

import android.content.ContentValues
import android.content.Context
import vn.kingbee.movie.model.Movie
import javax.inject.Inject

class FavoritesService {
    private val context: Context

    @Inject
    constructor(context: Context) {
        this.context = context.applicationContext
    }

    fun addToFavorites(movie: Movie) {
        context.contentResolver.insert(MoviesContract.MovieEntry.CONTENT_URI, movie.toContentValues())
        val contentValues = ContentValues()
        contentValues.put(MoviesContract.COLUMN_MOVIE_ID_KEY, movie.id)
        context.contentResolver.insert(MoviesContract.Favorites.CONTENT_URI, contentValues)
    }

    fun removeFromFavorites(movie: Movie) {
        context.contentResolver.delete(
            MoviesContract.Favorites.CONTENT_URI,
            MoviesContract.COLUMN_MOVIE_ID_KEY + " = " + movie.id,
            null
        )
    }

    fun isFavorite(movie: Movie): Boolean {
        var favorite = false
        val cursor = context.contentResolver.query(
            MoviesContract.Favorites.CONTENT_URI,
            null,
            MoviesContract.COLUMN_MOVIE_ID_KEY + " = " + movie.id,
            null,
            null
        )
        if (cursor != null) {
            favorite = cursor.count != 0
            cursor.close()
        }
        return favorite
    }
}