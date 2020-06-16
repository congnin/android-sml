package vn.kingbee.injection.module

import dagger.Binds
import dagger.Module
import dagger.Provides
import vn.kingbee.feature.splash.presenter.SplashPresenter
import vn.kingbee.feature.splash.presenter.SplashPresenterImpl

import javax.inject.Singleton

@Module
class PresenterModule {

    @Provides
    @Singleton
    fun provideSplashPresenter(presenter: SplashPresenterImpl): SplashPresenter = presenter
}
