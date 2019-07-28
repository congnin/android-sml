package vn.kingbee.movie.network

import dagger.Component
import vn.kingbee.injection.module.AppModule
import vn.kingbee.movie.ui.MainActivity
import vn.kingbee.movie.ui.SortingDialogFragment
import vn.kingbee.movie.ui.grid.MoviesGridFragment
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class, NetworkModule::class])
interface NetworkComponent {
    fun inject(moviesGridFragment: MoviesGridFragment)

    fun inject(mainActivity: MainActivity)

    fun inject(sortingDialogFragment: SortingDialogFragment)
}