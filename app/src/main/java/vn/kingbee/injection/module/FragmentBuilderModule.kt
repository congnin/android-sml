package vn.kingbee.injection.module

import dagger.Module
import dagger.android.ContributesAndroidInjector
import vn.kingbee.feature.splash.view.SplashFragment
import vn.kingbee.injection.scope.PerView

@Module
abstract class FragmentBuilderModule {
    @PerView
    @ContributesAndroidInjector(modules = [SplashViewModule::class, TimeoutModule::class])
    internal abstract fun contributeSplashFragment(): SplashFragment
}
