package vn.kingbee.application

import android.app.Application
import android.content.Context
import timber.log.Timber
import vn.kingbee.movie.network.NetworkComponent
import vn.kingbee.utils.FontHelper
import vn.kingbee.widget.BuildConfig
import vn.kingbee.movie.network.NetworkModule
import vn.kingbee.injection.module.AppModule
import vn.kingbee.movie.network.DaggerNetworkComponent


class MyApp : Application() {

    lateinit var networkComponent: NetworkComponent

    override fun onCreate() {
        super.onCreate()
        if (!BuildConfig.DEBUG) {
            Timber.plant(NotLoggingTree())
        } else {
            Timber.plant(Timber.DebugTree())
        }

        ContextSingleton.setContext(this)
        networkComponent = DaggerNetworkComponent.builder()
            .appModule(AppModule(this))
            .networkModule(NetworkModule())
            .build()

        // setup font family
        FontHelper.initializeFontConfig()
    }

    private class NotLoggingTree : Timber.Tree() {
        override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
            // Do nothing
        }
    }

    companion object {
        fun getInstance(): MyApp {
            return ContextSingleton.context!!
        }
    }

    internal object ContextSingleton {
        var context: MyApp? = null
            private set

        fun setContext(context: Context) {
            this.context = context as MyApp
        }
    }
}
