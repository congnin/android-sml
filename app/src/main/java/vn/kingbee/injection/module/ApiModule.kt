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
import vn.kingbee.application.Named.MOCK_CONTENT_REPOSITORY
import javax.inject.Named
import javax.inject.Singleton

@Module
class ApiModule {
    @Provides
    @Singleton
    @Named(END_POINT)
    fun provideBaseUrl(): String =
        "https://dev-ext-api-gtw-1222053746.eu-west-1.elb.amazonaws.com:443/"

    private fun getMockApiConfiguration(): MockApiConfiguration {
        return MockApiConfiguration.Builder()
            .endpointConfigPath("mock_api/endpointconfigData.json")
            .apiPath("mock_api/api-config")
            .scenarioPath("mock_api/scenarios")
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
    fun provideMockOkHttpClient(@Named(CONTENT_REPOSITORY_MOCK_ASSET) contentRepository: ContentRepository,
                                mockContractor: MockContractor): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(FakeInterceptor(contentRepository, mockContractor))
            .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(@Named(END_POINT) endpoint: String,
                        okHttpClient: OkHttpClient): Retrofit {
        val gson = GsonBuilder().setLenient().create()

        return Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl(endpoint)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
    }
}