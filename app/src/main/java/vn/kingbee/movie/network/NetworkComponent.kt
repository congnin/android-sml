package vn.kingbee.movie.network

import android.app.Application
import dagger.BindsInstance
import dagger.Component
import dagger.android.support.AndroidSupportInjectionModule
import vn.kingbee.application.MyApp
import vn.kingbee.injection.module.AppModule
import vn.kingbee.movie.ui.MainActivity
import vn.kingbee.movie.ui.SortingDialogFragment
import vn.kingbee.movie.ui.detail.MovieDetailActivity
import vn.kingbee.movie.ui.detail.MovieDetailFragment
import vn.kingbee.movie.ui.grid.MoviesGridFragment
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        AndroidSupportInjectionModule::class,
        AppModule::class,
        NetworkModule::class]
)
interface NetworkComponent {
    fun inject(moviesGridFragment: MoviesGridFragment)

    fun inject(mainActivity: MainActivity)

    fun inject(sortingDialogFragment: SortingDialogFragment)

    fun inject(detailActivity: MovieDetailActivity)

    fun inject(detailFragment: MovieDetailFragment)

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: MyApp): Builder

        fun build(): NetworkComponent
    }
}