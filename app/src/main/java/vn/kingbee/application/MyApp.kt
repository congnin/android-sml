package vn.kingbee.application

import android.app.Application
import timber.log.Timber
import vn.kingbee.utils.FontHelper
import vn.kingbee.widget.BuildConfig

class MyApp : Application() {
    override fun onCreate() {
        super.onCreate()
        if (!BuildConfig.DEBUG) {
            Timber.plant(NotLoggingTree())
        } else {
            Timber.plant(Timber.DebugTree())
        }

        // setup font family
        FontHelper.initializeFontConfig()
    }

    private class NotLoggingTree : Timber.Tree() {
        override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
            // Do nothing
        }
    }
}
