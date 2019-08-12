package vn.kingbee.injection.component

import dagger.Component
import retrofit2.Retrofit
import vn.kingbee.application.MyApp
import vn.kingbee.application.runtime.Runtime
import vn.kingbee.feature.splash.view.SplashFragment
import vn.kingbee.injection.module.AppModule
import vn.kingbee.injection.module.PresenterModule
import vn.kingbee.injection.module.RepositoryModule
import vn.kingbee.injection.module.UseCaseModule
import vn.kingbee.movie.network.NetworkModule
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        AppModule::class,
        RepositoryModule::class,
        UseCaseModule::class,
        PresenterModule::class,
        NetworkModule::class
    ]
)
interface AppComponent {
    fun runtime(): Runtime

    fun inject(application: MyApp)

    fun inject(f: SplashFragment)
}