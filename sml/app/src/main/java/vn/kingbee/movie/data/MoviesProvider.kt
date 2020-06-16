package vn.kingbee.movie.data

import android.content.ContentProvider
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.database.sqlite.SQLiteQueryBuilder
import android.net.Uri
import kotlin.collections.HashSet
import java.util.Arrays.asList
import android.database.SQLException
import android.database.sqlite.SQLiteDatabase


class MoviesProvider : ContentProvider() {

    lateinit var dbHelper: MoviesDbHelper

    override fun onCreate(): Boolean {
        dbHelper = MoviesDbHelper(context!!)
        return true
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        val db = dbHelper.writableDatabase
        val matcher = URI_MATCHER.match(uri)
        val returnUri: Uri
        val id: Long
        when (matcher) {
            MOVIES -> {
                id = db.insertWithOnConflict(MoviesContract.MovieEntry.TABLE_NAME, null,
                    values, SQLiteDatabase.CONFLICT_REPLACE)
                if (id > 0) {
                    returnUri = MoviesContract.MovieEntry.buildMovieUri(id)
                } else {
                    throw SQLException(FAILED_TO_INSERT_ROW_INTO + uri)
                }
            }
            MOST_POPULAR_MOVIES -> {
                id = db.insert(MoviesContract.MostPopularMovies.TABLE_NAME, null, values)
                if (id > 0) {
                    returnUri = MoviesContract.MostPopularMovies.CONTENT_URI
                } else {
                    throw SQLException(FAILED_TO_INSERT_ROW_INTO + uri)
                }
            }
            HIGHEST_RATED_MOVIES -> {
                id = db.insert(MoviesContract.HighestRatedMovies.TABLE_NAME, null, values)
                if (id > 0) {
                    returnUri = MoviesContract.HighestRatedMovies.CONTENT_URI
                } else {
                    throw SQLException(FAILED_TO_INSERT_ROW_INTO + uri)
                }
            }
            MOST_RATED_MOVIES -> {
                id = db.insert(MoviesContract.MostRatedMovies.TABLE_NAME, null, values)
                if (id > 0) {
                    returnUri = MoviesContract.MostRatedMovies.CONTENT_URI
                } else {
                    throw SQLException(FAILED_TO_INSERT_ROW_INTO + uri)
                }
            }
            FAVORITES -> {
                id = db.insert(MoviesContract.Favorites.TABLE_NAME, null, values)
                if (id > 0) {
                    returnUri = MoviesContract.Favorites.CONTENT_URI
                } else {
                    throw SQLException(FAILED_TO_INSERT_ROW_INTO + uri)
                }
            }
            else -> throw UnsupportedOperationException("Unknown uri: $uri")
        }
        context!!.contentResolver.notifyChange(uri, null)
        return returnUri
    }

    override fun query(uri: Uri, projection: Array<String>?,
                       selection: String?, selectionArgs: Array<String>?,
                       sortOrder: String?): Cursor? {
        val match = URI_MATCHER.match(uri)
        val cursor: Cursor
        checkColumns(projection)
        when (match) {
            MOVIES -> cursor = dbHelper.readableDatabase.query(
                MoviesContract.MovieEntry.TABLE_NAME,
                projection, selection, selectionArgs, null, null, sortOrder)
            MOVIE_BY_ID -> cursor = getMovieById(uri, projection, sortOrder)
            MOST_POPULAR_MOVIES -> cursor = getMoviesFromReferenceTable(
                MoviesContract.MostPopularMovies.TABLE_NAME,
                projection, selection, selectionArgs, sortOrder)
            HIGHEST_RATED_MOVIES -> cursor = getMoviesFromReferenceTable(
                MoviesContract.HighestRatedMovies.TABLE_NAME,
                projection, selection, selectionArgs, sortOrder)
            MOST_RATED_MOVIES -> cursor = getMoviesFromReferenceTable(
                MoviesContract.MostRatedMovies.TABLE_NAME,
                projection, selection, selectionArgs, sortOrder)
            FAVORITES -> cursor = getMoviesFromReferenceTable(
                MoviesContract.Favorites.TABLE_NAME,
                projection, selection, selectionArgs, sortOrder)
            else -> return null
        }
        cursor.setNotificationUri(context?.contentResolver, uri)
        return cursor
    }

    override fun update(uri: Uri, values: ContentValues?, selection: String?, selectionArgs: Array<String>?): Int {
        val db = dbHelper.writableDatabase
        val matcher = URI_MATCHER.match(uri)
        val rowsUpdated: Int
        when (matcher) {
            MOVIES -> rowsUpdated = db.update(MoviesContract.MovieEntry.TABLE_NAME, values,
                selection, selectionArgs)
            else -> throw UnsupportedOperationException("Unknown uri: $uri")
        }
        if (rowsUpdated != 0) {
            context?.contentResolver?.notifyChange(uri, null)
        }
        return rowsUpdated
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?): Int {
        val db = dbHelper.writableDatabase
        val matcher = URI_MATCHER.match(uri)
        val rowsDeleted: Int
        when (matcher) {
            MOVIES -> rowsDeleted = db.delete(MoviesContract.MovieEntry.TABLE_NAME,
                selection, selectionArgs)
            MOVIE_BY_ID -> {
                val id = MoviesContract.MovieEntry.getIdFromUri(uri)
                rowsDeleted = db.delete(MoviesContract.MovieEntry.TABLE_NAME,
                    MOVIE_ID_SELECTION, arrayOf(id.toString()))
            }
            MOST_POPULAR_MOVIES -> rowsDeleted = db.delete(MoviesContract.MostPopularMovies.TABLE_NAME,
                selection, selectionArgs)
            HIGHEST_RATED_MOVIES -> rowsDeleted = db.delete(MoviesContract.HighestRatedMovies.TABLE_NAME,
                selection, selectionArgs)
            MOST_RATED_MOVIES -> rowsDeleted = db.delete(MoviesContract.MostRatedMovies.TABLE_NAME,
                selection, selectionArgs)
            FAVORITES -> rowsDeleted = db.delete(MoviesContract.Favorites.TABLE_NAME,
                selection, selectionArgs)
            else -> throw UnsupportedOperationException("Unknown uri: $uri")
        }
        if (rowsDeleted != 0) {
            context?.contentResolver?.notifyChange(uri, null)
        }
        return rowsDeleted
    }

    override fun shutdown() {
        dbHelper.close()
        super.shutdown()
    }

    override fun bulkInsert(uri: Uri, values: Array<ContentValues>): Int {
        val db = dbHelper.writableDatabase
        when (URI_MATCHER.match(uri)) {
            MOVIES -> {
                db.beginTransaction()
                var returnCount = 0
                try {
                    for (value in values) {
                        val id = db.insertWithOnConflict(MoviesContract.MovieEntry.TABLE_NAME,
                            null, value, SQLiteDatabase.CONFLICT_REPLACE)
                        if (id != -1L) {
                            returnCount++
                        }
                    }
                    db.setTransactionSuccessful()
                } finally {
                    db.endTransaction()
                }
                context?.contentResolver?.notifyChange(uri, null)
                return returnCount
            }
            else -> return super.bulkInsert(uri, values)
        }
    }

    private fun getMovieById(uri: Uri?, projection: Array<String>?, sortOrder: String?): Cursor {
        val id = MoviesContract.MovieEntry.getIdFromUri(uri)
        val selection = MOVIE_ID_SELECTION
        val selectionArgs = arrayOf(id.toString())
        return dbHelper.readableDatabase.query(
            MoviesContract.MovieEntry.TABLE_NAME,
            projection, selection, selectionArgs, null, null, sortOrder)
    }

    override fun getType(uri: Uri?): String? {
        return when (URI_MATCHER.match(uri)) {
            MOVIES -> MoviesContract.MovieEntry.CONTENT_DIR_TYPE
            MOVIE_BY_ID -> MoviesContract.MovieEntry.CONTENT_ITEM_TYPE
            MOST_POPULAR_MOVIES -> MoviesContract.MostPopularMovies.CONTENT_DIR_TYPE
            HIGHEST_RATED_MOVIES -> MoviesContract.HighestRatedMovies.CONTENT_DIR_TYPE
            MOST_RATED_MOVIES -> MoviesContract.MostRatedMovies.CONTENT_DIR_TYPE
            FAVORITES -> MoviesContract.Favorites.CONTENT_DIR_TYPE
            else -> null
        }
    }

    private fun getMoviesFromReferenceTable(tableName: String, projection: Array<String>?, selection: String?,
                                            selectionArgs: Array<String>?, sortOrder: String?): Cursor {
        val sqLiteQueryBuilder = SQLiteQueryBuilder()

        // tableName INNER JOIN movies ON tableName.movie_id = movies._id
        sqLiteQueryBuilder.tables =
            tableName + " INNER JOIN " + MoviesContract.MovieEntry.TABLE_NAME +
                    " ON " + tableName + "." + MoviesContract.COLUMN_MOVIE_ID_KEY +
                    " = " + MoviesContract.MovieEntry.TABLE_NAME + "." + MoviesContract.MovieEntry._ID

        return sqLiteQueryBuilder.query(dbHelper.readableDatabase,
            projection, selection, selectionArgs, null, null, sortOrder)
    }

    private fun checkColumns(projection: Array<String>?) {
        if (projection != null) {
            val availableColumns = HashSet(asList(
                MoviesContract.MovieEntry.getColumns()))
            val requestedColumns = HashSet(asList(projection))
            if (!availableColumns.containsAll(requestedColumns)) {
                throw IllegalArgumentException("Unknown columns in projection.")
            }
        }
    }

    companion object {
        const val MOVIES = 100
        const val MOVIE_BY_ID = 101
        const val MOST_POPULAR_MOVIES = 201
        const val HIGHEST_RATED_MOVIES = 202
        const val MOST_RATED_MOVIES = 203
        const val FAVORITES = 300

        private val URI_MATCHER = buildUriMatcher()
        private const val FAILED_TO_INSERT_ROW_INTO = "Failed to insert row into "

        // movies._id = ?
        private const val MOVIE_ID_SELECTION =
            MoviesContract.MovieEntry.TABLE_NAME + "." + MoviesContract.MovieEntry._ID + " = ? "

        private fun buildUriMatcher(): UriMatcher {
            val uriMatcher = UriMatcher(UriMatcher.NO_MATCH)
            val authority = MoviesContract.CONTENT_AUTHORITY

            uriMatcher.addURI(authority, MoviesContract.PATH_MOVIES, MOVIES)
            uriMatcher.addURI(authority, MoviesContract.PATH_MOVIES + "/#", MOVIE_BY_ID)

            uriMatcher.addURI(authority, MoviesContract.PATH_MOVIES + "/" +
                    MoviesContract.PATH_MOST_POPULAR, MOST_POPULAR_MOVIES)
            uriMatcher.addURI(authority, MoviesContract.PATH_MOVIES + "/" +
                    MoviesContract.PATH_HIGHEST_RATED, HIGHEST_RATED_MOVIES)
            uriMatcher.addURI(authority, MoviesContract.PATH_MOVIES + "/" +
                    MoviesContract.PATH_MOST_RATED, MOST_RATED_MOVIES)

            uriMatcher.addURI(authority, MoviesContract.PATH_MOVIES + "/" +
                    MoviesContract.PATH_FAVORITES, FAVORITES)

            return uriMatcher
        }
    }
}