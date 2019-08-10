package vn.kingbee.injection.module

import dagger.Module
import dagger.Provides
import vn.kingbee.domain.setting.repository.SettingRepository
import vn.kingbee.domain.setting.usecase.SettingUseCase
import vn.kingbee.domain.setting.usecase.SettingUseCaseImpl
import vn.kingbee.domain.token.repository.TokenRepository
import vn.kingbee.domain.token.usecase.TokenUseCase
import vn.kingbee.domain.token.usecase.TokenUseCaseImpl
import javax.inject.Singleton

@Module
class UseCaseModule {
    @Provides
    @Singleton
    fun provideSettingUseCase(repository: SettingRepository): SettingUseCase {
        return SettingUseCaseImpl(repository)
    }

    @Provides
    @Singleton
    fun provideTokenUseCase(repository: TokenRepository): TokenUseCase {
        return TokenUseCaseImpl(repository)
    }
}