package vn.kingbee.injection.module

import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import vn.kingbee.application.Named.RETROFIT_KIOSK
import vn.kingbee.application.Named.RETROFIT_MOCK
import vn.kingbee.data.dataprocessing.DataProcessingRepositoryImpl
import vn.kingbee.data.setting.repository.SettingRepositoryImpl
import vn.kingbee.data.setting.service.SettingService
import vn.kingbee.data.token.repository.TokenRepositoryImpl
import vn.kingbee.data.token.service.TokenService
import vn.kingbee.domain.dataprocess.AppBus
import vn.kingbee.domain.dataprocess.DataProcessingRepository
import vn.kingbee.domain.dataprocess.Runtime
import vn.kingbee.domain.setting.repository.SettingRepository
import vn.kingbee.domain.token.repository.TokenRepository
import javax.inject.Named
import javax.inject.Singleton

@Module
class RepositoryModule {

    @Provides
    @Singleton
    fun provideDataProcessing(runtime: Runtime,
                              appBus: AppBus): DataProcessingRepository =
        DataProcessingRepositoryImpl(runtime, appBus)

    @Provides
    @Singleton
    fun provideSettingService(@Named(RETROFIT_KIOSK) retrofit: Retrofit): SettingService {
        return retrofit.create(SettingService::class.java)
    }

    @Provides
    @Singleton
    fun provideSettingRepository(service: SettingService): SettingRepository {
        return SettingRepositoryImpl(service)
    }

    @Provides
    @Singleton
    fun provideTokenService(@Named(RETROFIT_KIOSK) retrofit: Retrofit): TokenService {
        return retrofit.create(TokenService::class.java)
    }

    @Provides
    @Singleton
    fun provideTokenRepository(service: TokenService): TokenRepository {
        return TokenRepositoryImpl(service)
    }
}