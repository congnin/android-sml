package vn.kingbee.movie.model

import android.annotation.SuppressLint
import com.google.gson.annotations.SerializedName
import android.os.Parcel
import vn.kingbee.movie.data.MoviesContract
import android.content.ContentValues
import android.database.Cursor
import android.os.Parcelable

class Movie : Parcelable {

    @SerializedName("id")
    var id: Long = 0

    @SerializedName("original_title")
    var originalTitle: String? = null

    @SerializedName("overview")
    var overview: String? = null

    @SerializedName("release_date")
    var releaseDate: String? = null

    @SerializedName("poster_path")
    var posterPath: String? = null

    @SerializedName("popularity")
    var popularity: Double = 0.toDouble()

    @SerializedName("title")
    var title: String? = null

    @SerializedName("vote_average")
    var averageVote: Double = 0.toDouble()

    @SerializedName("vote_count")
    var voteCount: Long = 0

    @SerializedName("backdrop_path")
    var backdropPath: String? = null

    constructor(id: Long, title: String) {
        this.id = id
        this.title = title
    }

    protected constructor(`in`: Parcel) {
        this.id = `in`.readLong()
        this.originalTitle = `in`.readString()
        this.overview = `in`.readString()
        this.releaseDate = `in`.readString()
        this.posterPath = `in`.readString()
        this.popularity = `in`.readDouble()
        this.title = `in`.readString()
        this.averageVote = `in`.readDouble()
        this.voteCount = `in`.readLong()
        this.backdropPath = `in`.readString()
    }

    override fun toString(): String {
        return "[" + this.id + ", " + this.title + "]"
    }

    //CHECKSTYLE:OFF
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false

        val movie = other as Movie?

        if (id != movie!!.id) return false
        if (movie.popularity.compareTo(popularity) != 0) return false
        if (movie.averageVote.compareTo(averageVote) != 0) return false
        if (voteCount != movie.voteCount) return false
        if (if (originalTitle != null) originalTitle != movie.originalTitle else movie.originalTitle != null)
            return false
        if (if (overview != null) overview != movie.overview else movie.overview != null) return false
        if (if (releaseDate != null) releaseDate != movie.releaseDate else movie.releaseDate != null) return false
        if (if (posterPath != null) posterPath != movie.posterPath else movie.posterPath != null) return false
        if (if (title != null) title != movie.title else movie.title != null) return false
        return if (backdropPath != null) backdropPath == movie.backdropPath else movie.backdropPath == null

    }

    override fun hashCode(): Int {
        var result: Int
        var temp: Long
        result = (id xor id.ushr(32)).toInt()
        result = 31 * result + if (originalTitle != null) originalTitle!!.hashCode() else 0
        result = 31 * result + if (overview != null) overview!!.hashCode() else 0
        result = 31 * result + if (releaseDate != null) releaseDate!!.hashCode() else 0
        result = 31 * result + if (posterPath != null) posterPath!!.hashCode() else 0
        temp = java.lang.Double.doubleToLongBits(popularity)
        result = 31 * result + (temp xor temp.ushr(32)).toInt()
        result = 31 * result + if (title != null) title!!.hashCode() else 0
        temp = java.lang.Double.doubleToLongBits(averageVote)
        result = 31 * result + (temp xor temp.ushr(32)).toInt()
        result = 31 * result + (voteCount xor voteCount.ushr(32)).toInt()
        result = 31 * result + if (backdropPath != null) backdropPath!!.hashCode() else 0
        return result
    }
    //CHECKSTYLE:ON

    fun toContentValues(): ContentValues {
        val values = ContentValues()
        values.put(MoviesContract.MovieEntry._ID, id)
        values.put(MoviesContract.MovieEntry.COLUMN_ORIGINAL_TITLE, originalTitle)
        values.put(MoviesContract.MovieEntry.COLUMN_OVERVIEW, overview)
        values.put(MoviesContract.MovieEntry.COLUMN_RELEASE_DATE, releaseDate)
        values.put(MoviesContract.MovieEntry.COLUMN_POSTER_PATH, posterPath)
        values.put(MoviesContract.MovieEntry.COLUMN_POPULARITY, popularity)
        values.put(MoviesContract.MovieEntry.COLUMN_TITLE, title)
        values.put(MoviesContract.MovieEntry.COLUMN_AVERAGE_VOTE, averageVote)
        values.put(MoviesContract.MovieEntry.COLUMN_VOTE_COUNT, voteCount)
        values.put(MoviesContract.MovieEntry.COLUMN_BACKDROP_PATH, backdropPath)
        return values
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeLong(this.id)
        dest.writeString(this.originalTitle)
        dest.writeString(this.overview)
        dest.writeString(this.releaseDate)
        dest.writeString(this.posterPath)
        dest.writeDouble(this.popularity)
        dest.writeString(this.title)
        dest.writeDouble(this.averageVote)
        dest.writeLong(this.voteCount)
        dest.writeString(this.backdropPath)
    }

    companion object {

        @JvmField
        val CREATOR: Parcelable.Creator<Movie> = object : Parcelable.Creator<Movie> {
            override fun createFromParcel(source: Parcel): Movie {
                return Movie(source)
            }

            override fun newArray(size: Int): Array<Movie?> {
                return arrayOfNulls(size)
            }
        }

        fun fromCursor(cursor: Cursor): Movie {
            val id = cursor.getLong(cursor.getColumnIndex(MoviesContract.MovieEntry._ID))
            val title = cursor.getString(cursor.getColumnIndex(MoviesContract.MovieEntry.COLUMN_TITLE))
            val movie = Movie(id, title)
            movie.originalTitle = cursor.getString(cursor.getColumnIndex(MoviesContract.MovieEntry.COLUMN_ORIGINAL_TITLE))
            movie.overview = cursor.getString(cursor.getColumnIndex(MoviesContract.MovieEntry.COLUMN_OVERVIEW))
            movie.releaseDate = cursor.getString(cursor.getColumnIndex(MoviesContract.MovieEntry.COLUMN_RELEASE_DATE))
            movie.posterPath = cursor.getString(cursor.getColumnIndex(MoviesContract.MovieEntry.COLUMN_POSTER_PATH))
            movie.popularity = cursor.getDouble(cursor.getColumnIndex(MoviesContract.MovieEntry.COLUMN_POPULARITY))
            movie.averageVote = cursor.getDouble(cursor.getColumnIndex(MoviesContract.MovieEntry.COLUMN_AVERAGE_VOTE))
            movie.voteCount = cursor.getLong(cursor.getColumnIndex(MoviesContract.MovieEntry.COLUMN_VOTE_COUNT))
            movie.backdropPath = cursor.getString(cursor.getColumnIndex(MoviesContract.MovieEntry.COLUMN_BACKDROP_PATH))
            return movie
        }
    }
}