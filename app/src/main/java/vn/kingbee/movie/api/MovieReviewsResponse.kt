package vn.kingbee.movie.api

import com.google.gson.annotations.SerializedName
import vn.kingbee.movie.model.MovieReview

class MovieReviewsResponse {
    @SerializedName("id")
    var movieId: Long = 0

    @SerializedName("page")
    var page: Int = 0

    @SerializedName("results")
    var results: List<MovieReview>? = null

    @SerializedName("total_pages")
    var totalPages: Int = 0

    constructor(movieId: Long, page: Int, results: List<MovieReview>, totalPages: Int) {
        this.movieId = movieId
        this.page = page
        this.results = results
        this.totalPages = totalPages
    }
}