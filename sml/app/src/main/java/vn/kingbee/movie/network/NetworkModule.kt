package vn.kingbee.movie.network

import android.app.Application
import android.content.SharedPreferences
import dagger.Module
import dagger.Provides
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import vn.kingbee.application.Named.OK_HTTP_CLIENT_NORMAL
import vn.kingbee.application.Named.RETROFIT_MOVIE
import vn.kingbee.movie.api.TheMovieDbService
import vn.kingbee.movie.data.MoviesService
import vn.kingbee.movie.model.SortHelper
import vn.kingbee.widget.BuildConfig
import java.util.concurrent.TimeUnit
import javax.inject.Singleton
import vn.kingbee.movie.data.FavoritesService
import javax.inject.Named

@Module
class NetworkModule {

    @Provides
    @Singleton
    fun provideOkHttpCache(application: Application): Cache {
        return Cache(application.cacheDir, CACHE_SIZE)
    }

    @Provides
    @Singleton
    @Named(OK_HTTP_CLIENT_NORMAL)
    fun providesOkHttpClient(cache: Cache): OkHttpClient {

        val builder = OkHttpClient.Builder()
            .addInterceptor(AuthorizationInterceptor())
            .connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)
            .writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS)
            .readTimeout(TIMEOUT, TimeUnit.SECONDS)
            .cache(cache)

        if (BuildConfig.DEBUG) {
            val loggingInterceptor = HttpLoggingInterceptor()
            loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
            builder.addInterceptor(loggingInterceptor)
        }
        return builder.build()
    }

    @Provides
    @Singleton
    @Named(RETROFIT_MOVIE)
    fun providesRetrofit(@Named(OK_HTTP_CLIENT_NORMAL) okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_MOVIE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .client(okHttpClient)
            .build()
    }

    @Provides
    @Singleton
    fun providesSortHelper(sharedPreferences: SharedPreferences): SortHelper {
        return SortHelper(sharedPreferences)
    }

    @Provides
    @Singleton
    fun providesTheMovieDbService(@Named(RETROFIT_MOVIE) retrofit: Retrofit): TheMovieDbService {
        return retrofit.create(TheMovieDbService::class.java)
    }

    @Provides
    @Singleton
    fun providesMoviesService(application: Application, theMovieDbService: TheMovieDbService,
                              sortHelper: SortHelper): MoviesService {
        return MoviesService(application.applicationContext, theMovieDbService, sortHelper)
    }

    @Provides
    @Singleton
    fun providesFavoritesService(application: Application): FavoritesService {
        return FavoritesService(application.applicationContext)
    }

    companion object {
        private const val BASE_MOVIE_URL = "http://api.themoviedb.org/3/"
        const val BASE_KIOSK_URL = "https://10.224.44.141:8243/kiosk-ic/1.0.0/"
        private const val CACHE_SIZE = (10 * 1024 * 1024).toLong()    // 10 MB
        private const val CONNECT_TIMEOUT = 15L
        private const val WRITE_TIMEOUT = 60L
        private const val TIMEOUT = 60L
    }
}