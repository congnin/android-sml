package vn.kingbee.movie.api

import com.google.gson.annotations.SerializedName
import vn.kingbee.movie.model.MovieVideo

class MovieVideosResponse(@field:SerializedName("id")
                          val movieId: Long, @field:SerializedName("results")
                          val results: ArrayList<MovieVideo>)