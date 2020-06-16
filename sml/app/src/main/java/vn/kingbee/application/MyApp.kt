package vn.kingbee.application

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import androidx.fragment.app.Fragment
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import io.reactivex.Observable
import timber.log.Timber
import vn.kingbee.injection.component.AppComponent
import vn.kingbee.injection.component.DaggerAppComponent
import vn.kingbee.movie.network.NetworkComponent
import vn.kingbee.utils.FontHelper
import vn.kingbee.widget.BuildConfig
import vn.kingbee.movie.network.DaggerNetworkComponent
import vn.kingbee.rxjava.model.Events
import vn.kingbee.rxjava.rxbus.RxBus
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class MyApp : Application(), HasSupportFragmentInjector {

    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Fragment>

    lateinit var networkComponent: NetworkComponent
    lateinit var mAppComponent: AppComponent
    lateinit var bus: RxBus

    override fun onCreate() {
        super.onCreate()
        if (!BuildConfig.DEBUG) {
            Timber.plant(NotLoggingTree())
        } else {
            Timber.plant(Timber.DebugTree())
        }

        ContextSingleton.setContext(this)

        networkComponent = DaggerNetworkComponent.builder()
            .application(this)
            .build()

        mAppComponent = DaggerAppComponent.builder()
            .application(this)
            .build()

        mAppComponent.inject(this)

        // setup font family
        FontHelper.initializeFontConfig()

        bus = mAppComponent.rxBus()
    }

    override fun supportFragmentInjector(): AndroidInjector<Fragment> = dispatchingAndroidInjector

    @SuppressLint("CheckResult")
    fun sendAutoEvent() {
        Observable.timer(5, TimeUnit.SECONDS)
            .subscribe { bus.send(Events.AutoEvent()) }
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
