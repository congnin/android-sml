package vn.kingbee.injection.module

import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import vn.kingbee.data.setting.repository.SettingRepositoryImpl
import vn.kingbee.data.setting.service.SettingService
import vn.kingbee.domain.setting.repository.SettingRepository
import vn.kingbee.domain.setting.usecase.SettingUseCase
import vn.kingbee.domain.setting.usecase.SettingUseCaseImpl
import javax.inject.Singleton

@Module
class RepositoryModule {
    @Provides
    @Singleton
    fun provideSettingService(retrofit: Retrofit): SettingService {
        return retrofit.create(SettingService::class.java)
    }

    @Provides
    @Singleton
    fun provideSettingRepository(service: SettingService): SettingRepository {
        return SettingRepositoryImpl(service)
    }

    @Provides
    @Singleton
    fun provideSettingUseCase(repository: SettingRepository): SettingUseCase {
        return SettingUseCaseImpl(repository)
    }
}