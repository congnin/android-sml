package vn.kingbee.injection.module

import dagger.Module
import dagger.Provides
import vn.kingbee.application.AppConstant
import vn.kingbee.feature.service.TimeoutProcessingService
import vn.kingbee.feature.service.TimeoutProcessingServiceImpl
import vn.kingbee.injection.scope.PerView
import javax.inject.Singleton

@Module
class TimeoutModule {
    @Provides
    @PerView
    fun provideTimeoutProcessingService(): TimeoutProcessingService = TimeoutProcessingServiceImpl.Builder()
        .timeCountDown(AppConstant.APP_TIMEOUT)
        .build()
}