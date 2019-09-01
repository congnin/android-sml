package vn.kingbee.injection.module

import dagger.Binds
import dagger.Module
import vn.kingbee.feature.splash.presenter.SplashPresenter
import vn.kingbee.feature.splash.presenter.SplashPresenterImpl
import vn.kingbee.feature.splash.view.SplashFragment
import vn.kingbee.feature.splash.view.SplashView
import vn.kingbee.injection.scope.PerView

@Module
abstract class SplashViewModule {
    @PerView
    @Binds
    abstract fun provideView(view: SplashFragment): SplashView

    @PerView
    @Binds
    abstract fun providePresenter(presenter: SplashPresenterImpl): SplashPresenter
}
