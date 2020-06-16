package vn.kingbee.movie.api

import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import vn.kingbee.movie.model.Movie

interface TheMovieDbService {
    @GET("movie/{id}/videos")
    fun getMovieVideos(@Path("id") movieId: Long): Observable<MovieVideosResponse>

    @GET("movie/{id}/reviews")
    fun getMovieReviews(@Path("id") movieId: Long): Observable<MovieReviewsResponse>

    @GET("discover/movie")
    fun discoverMovies(@Query("sort_by") sortBy: String,
                       @Query("page") page: Int?): Observable<DiscoverAndSearchResponse<Movie>>

    @GET("search/movie")
    fun searchMovies(@Query("query") query: String,
                     @Query("page") page: Int?): Observable<DiscoverAndSearchResponse<Movie>>
}