package vn.kingbee.movie.api

import com.google.gson.annotations.SerializedName
import vn.kingbee.movie.model.MovieVideo
import java.util.ArrayList

class MovieVideosResponse {
    @SerializedName("id")
    var movieId: Long? = null

    @SerializedName("results")
    var results: List<MovieVideo>? = null

    constructor(movieId: Long?, results: List<MovieVideo>?) {
        this.movieId = movieId
        this.results = results
    }
}