package vn.kingbee.injection.module

import android.app.Application
import android.content.Context
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import vn.kingbee.application.MyApp
import javax.inject.Singleton

@Module
class AppModule {
    private val application: Application

    constructor(application: MyApp) {
        this.application = application
    }

    @Provides
    @Singleton
    fun providesApplication(): Application {
        return application
    }

    @Provides
    @Singleton
    fun provideGson(): Gson {
        return GsonBuilder().serializeNulls().create()
    }
}