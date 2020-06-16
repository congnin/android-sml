package vn.kingbee.movie.api

import com.google.gson.annotations.SerializedName

class DiscoverAndSearchResponse<T> {
    @SerializedName("page")
    var page: Int? = null

    @SerializedName("results")
    var results: List<T>? = null

    @SerializedName("total_pages")
    var totalPages: Int? = null

    @SerializedName("total_results")
    var totalResults: Long? = null

    constructor(page: Int?, results: List<T>, totalPages: Int, totalResults: Long) {
        this.page = page
        this.results = results
        this.totalPages = totalPages
        this.totalResults = totalResults
    }
}