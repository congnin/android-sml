package vn.kingbee.injection.module

import android.content.Context
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import vn.kingbee.achilles.interceptor.FakeInterceptor
import vn.kingbee.achilles.model.MockApiConfiguration
import vn.kingbee.achilles.repository.AssetRepository
import vn.kingbee.achilles.repository.ContentRepository
import vn.kingbee.achilles.repository.ExternalStorageRepository
import vn.kingbee.achilles.service.MockBroadCastController
import vn.kingbee.achilles.service.MockContractor
import vn.kingbee.achilles.service.UserInteractConfig
import vn.kingbee.application.CachedPath
import vn.kingbee.application.Named.CONTENT_REPOSITORY_MOCK_ASSET
import vn.kingbee.application.Named.CONTENT_REPOSITORY_MOCK_SD_CARD
import vn.kingbee.application.Named.END_POINT
import vn.kingbee.application.Named.OK_HTTP_CLIENT_MOCK
import vn.kingbee.application.Named.RETROFIT_MOCK
import javax.inject.Named
import javax.inject.Singleton

@Module
class ApiModule {
    @Provides
    @Singleton
    @Named(END_POINT)
    fun provideBaseUrl(): String =
        "https://smldev.com:3887/"

    private fun getMockApiConfiguration(): MockApiConfiguration {
        return MockApiConfiguration.Builder()
            .endpointConfigPath("/endpointconfigData.json")
            .apiPath("/api-config")
            .scenarioPath("/scenarios")
            .scenarioName("All_Success_Flows")
            .build();
    }

    @Provides
    @Singleton
    @Named(CONTENT_REPOSITORY_MOCK_ASSET)
    fun provideAssetContentRepository(context: Context): ContentRepository {
        return AssetRepository(context, getMockApiConfiguration())
    }

    @Provides
    @Singleton
    @Named(CONTENT_REPOSITORY_MOCK_SD_CARD)
    fun provideSdCardContentRepository(context: Context): ContentRepository {
        return ExternalStorageRepository(context, getMockApiConfiguration(), CachedPath.MOCK_SD_CARD_FOLDER_PATH)
    }

    @Provides
    @Singleton
    fun provideMockController(context: Context, config: UserInteractConfig): MockContractor {
        return MockBroadCastController(context, config)
    }

    @Provides
    @Singleton
    fun provideUserInteractConfig(): UserInteractConfig = object : UserInteractConfig {
        override val isEnableUserInteractFeature: Boolean
            get() = true
    }

    @Provides
    @Singleton
    @Named(OK_HTTP_CLIENT_MOCK)
    fun provideMockOkHttpClient(@Named(CONTENT_REPOSITORY_MOCK_ASSET) contentRepository: ContentRepository,
                                mockContractor: MockContractor): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(FakeInterceptor(contentRepository, mockContractor))
            .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            .build()
    }

    @Provides
    @Singleton
    @Named(RETROFIT_MOCK)
    fun provideRetrofit(@Named(END_POINT) endpoint: String,
                        @Named(OK_HTTP_CLIENT_MOCK) okHttpClient: OkHttpClient): Retrofit {
        val gson = GsonBuilder().setLenient().create()

        return Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl(endpoint)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
    }
}