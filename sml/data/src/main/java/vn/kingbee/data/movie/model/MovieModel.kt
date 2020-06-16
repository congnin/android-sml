package vn.kingbee.data.movie.model

import com.google.gson.annotations.SerializedName

class MovieModel {
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
}