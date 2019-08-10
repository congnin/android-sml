package vn.kingbee.injection.module

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import vn.kingbee.application.MyApp
import vn.kingbee.application.appbus.AppBus
import vn.kingbee.application.appbus.AppBusImpl
import vn.kingbee.application.runtime.Runtime
import vn.kingbee.application.runtime.RuntimeImpl
import vn.kingbee.utils.security.ObscuredSharedPreferences
import vn.kingbee.widget.R
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
    internal fun provideObscuredSharedPreferences(): SharedPreferences {
        return ObscuredSharedPreferences(application,
            application.getSharedPreferences(
                application.getString(R.string.app_name), Context.MODE_PRIVATE)
        )
    }

    @Provides
    @Singleton
    fun provideGson(): Gson {
        return GsonBuilder().serializeNulls().setLenient().create()
    }

    @Provides
    @Singleton
    fun provideRuntime(sharedPref: SharedPreferences, gson: Gson): Runtime {
        return RuntimeImpl(sharedPref, gson)
    }

    @Provides
    @Singleton
    fun provideApplicationBus(): AppBus {
        return AppBusImpl()
    }
}