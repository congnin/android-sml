package vn.kingbee.movie.data

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class MoviesDbHelper(context: Context)
    : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_SCHEMA_VERSION) {

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(MoviesContract.MovieEntry.SQL_CREATE_TABLE)
        db.execSQL(MoviesContract.MostPopularMovies.SQL_CREATE_TABLE);
        db.execSQL(MoviesContract.HighestRatedMovies.SQL_CREATE_TABLE);
        db.execSQL(MoviesContract.MostRatedMovies.SQL_CREATE_TABLE);
        db.execSQL(MoviesContract.Favorites.SQL_CREATE_TABLE);
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL(SQL_DROP_TABLE_IF_EXISTS + MoviesContract.MovieEntry.TABLE_NAME);
        db.execSQL(SQL_DROP_TABLE_IF_EXISTS + MoviesContract.MostPopularMovies.TABLE_NAME);
        db.execSQL(SQL_DROP_TABLE_IF_EXISTS + MoviesContract.HighestRatedMovies.TABLE_NAME);
        db.execSQL(SQL_DROP_TABLE_IF_EXISTS + MoviesContract.MostRatedMovies.TABLE_NAME);
        db.execSQL(SQL_DROP_TABLE_IF_EXISTS + MoviesContract.Favorites.TABLE_NAME);
        onCreate(db);
    }

    companion object {
        const val DATABASE_NAME = "movies.db"
        private const val DATABASE_SCHEMA_VERSION = 2
        private const val SQL_DROP_TABLE_IF_EXISTS = "DROP TABLE IF EXISTS "
    }
}