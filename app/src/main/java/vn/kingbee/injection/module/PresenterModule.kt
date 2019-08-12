package vn.kingbee.injection.module

import dagger.Binds
import dagger.Module
import dagger.Provides
import vn.kingbee.feature.splash.presenter.SplashPresenter
import vn.kingbee.feature.splash.presenter.SplashPresenterImpl
import vn.kingbee.feature.splash.view.SplashFragment
import vn.kingbee.feature.splash.view.SplashView

import javax.inject.Singleton

@Module
class PresenterModule {

    @Singleton
    @Provides
    fun provideSplashPresenter(presenter: SplashPresenterImpl): SplashPresenter = presenter
}
