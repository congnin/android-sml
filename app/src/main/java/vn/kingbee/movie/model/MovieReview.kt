package vn.kingbee.movie.model

import android.annotation.SuppressLint
import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName


class MovieReview : Parcelable {

    @SerializedName("id")
    var reviewId: String? = null
        private set

    @SerializedName("author")
    var author: String? = null

    @SerializedName("url")
    var reviewUrl: String? = null

    @SerializedName("content")
    var content: String? = null

    constructor(reviewId: String) {
        this.reviewId = reviewId
    }

    protected constructor(`in`: Parcel) {
        this.reviewId = `in`.readString()
        this.author = `in`.readString()
        this.reviewUrl = `in`.readString()
        this.content = `in`.readString()
    }

    //CHECKSTYLE:OFF
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false

        val review = other as MovieReview?

        if (if (reviewId != null) reviewId != review!!.reviewId else review!!.reviewId != null) return false
        if (if (author != null) author != review.author else review.author != null) return false
        if (if (reviewUrl != null) reviewUrl != review.reviewUrl else review.reviewUrl != null) return false
        return if (content != null) content == review.content else review.content == null

    }

    override fun hashCode(): Int {
        var result = if (reviewId != null) reviewId!!.hashCode() else 0
        result = 31 * result + if (author != null) author!!.hashCode() else 0
        result = 31 * result + if (reviewUrl != null) reviewUrl!!.hashCode() else 0
        result = 31 * result + if (content != null) content!!.hashCode() else 0
        return result
    }
    //CHECKSTYLE:ON

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(this.reviewId)
        dest.writeString(this.author)
        dest.writeString(this.reviewUrl)
        dest.writeString(this.content)
    }

    companion object {

        @SuppressLint("ParcelCreator")
        val CREATOR: Parcelable.Creator<MovieReview> = object : Parcelable.Creator<MovieReview> {
            override fun createFromParcel(source: Parcel): MovieReview {
                return MovieReview(source)
            }

            override fun newArray(size: Int): Array<MovieReview?> {
                return arrayOfNulls(size)
            }
        }
    }
}