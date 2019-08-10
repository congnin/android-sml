package vn.kingbee.injection.module;

import dagger.Module;
import dagger.Provides;
import vn.kingbee.feature.splash.presenter.SplashPresenter;
import vn.kingbee.feature.splash.presenter.SplashPresenterImpl;

import javax.inject.Singleton;

@Module
public class PresenterModule {
    @Provides
    @Singleton
    SplashPresenter provideSplashPresenter(SplashPresenterImpl impl) {
        return impl;
    }
}
