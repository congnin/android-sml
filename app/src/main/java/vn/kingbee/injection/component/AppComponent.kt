package vn.kingbee.injection.component

import dagger.BindsInstance
import dagger.Component
import dagger.android.support.AndroidSupportInjectionModule
import vn.kingbee.application.MyApp
import vn.kingbee.domain.dataprocess.Runtime
import vn.kingbee.feature.splash.view.SplashFragment
import vn.kingbee.injection.module.*
import vn.kingbee.movie.network.NetworkModule
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        AndroidSupportInjectionModule::class,
        AppModule::class,
        RepositoryModule::class,
        UseCaseModule::class,
        NetworkModule::class,
        FragmentBuilderModule::class
    ]
)
interface AppComponent {

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: MyApp): Builder

        fun build(): AppComponent
    }

    fun runtime(): Runtime

    fun inject(application: MyApp)

//    fun inject(f: SplashFragment)
}