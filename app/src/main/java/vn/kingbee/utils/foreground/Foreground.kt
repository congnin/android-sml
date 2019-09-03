package vn.kingbee.utils.foreground

import android.app.Activity
import android.app.Application
import android.content.Context
import android.os.Bundle
import android.os.Handler
import timber.log.Timber
import java.util.concurrent.CopyOnWriteArrayList

class Foreground : Application.ActivityLifecycleCallbacks {

    private var foreground: Boolean = false
    private var paused: Boolean = true
    private val mHandler = Handler()
    private val listeners: List<Listener> = CopyOnWriteArrayList()
    private var check: Runnable? = null

    fun isForeground(): Boolean = this.foreground

    fun isBackground(): Boolean = !this.foreground

    fun addListener(listener: Listener) {
        (this.listeners as CopyOnWriteArrayList).add(listener)
    }

    fun removeListener(listener: Listener) {
        (this.listeners as CopyOnWriteArrayList).remove(listener)
    }

    override fun onActivityPaused(activity: Activity?) {
        this.paused = true
        if (this.check != null) {
            this.mHandler.removeCallbacks(this.check)
        }

        this.check = Runnable {
            if (this.foreground && this.paused) {
                this.foreground = false
                Timber.i("Time out went background")
                val iterator = this.listeners.iterator()

                while (iterator.hasNext()) {
                    val l = iterator.next()
                    try {
                        l.onBecameBackground()
                    } catch (e: Exception) {
                        Timber.e(TAG, "Listener threw exception!", e)
                    }
                }
            } else {
                Timber.i("Time out still foreground")
            }
        }

        this.mHandler.postDelayed(this.check, CHECK_DELAY)
    }

    override fun onActivityResumed(activity: Activity?) {
        this.paused = false
        val wasBackground = isBackground()
        this.foreground = true
        if (this.check != null) {
            this.mHandler.removeCallbacks(this.check)
        }

        if (wasBackground) {
            Timber.i("Time out went foreground")
            val iterator = this.listeners.iterator()

            while (iterator.hasNext()) {
                val l = iterator.next()
                try {
                    l.onBecameForeground()
                } catch (e: Exception) {
                    Timber.e(TAG, "Listener threw exception!", e)
                }
            }
        } else {
            Timber.i("Time out still foreground")
        }
    }

    override fun onActivityStarted(activity: Activity?) {
        //do nothing
    }

    override fun onActivityDestroyed(activity: Activity?) {
        //do nothing
    }

    override fun onActivitySaveInstanceState(activity: Activity?, outState: Bundle?) {
        //do nothing
    }

    override fun onActivityStopped(activity: Activity?) {
        //do nothing
    }

    override fun onActivityCreated(activity: Activity?, savedInstanceState: Bundle?) {
        //do nothing
    }

    interface Listener {
        fun onBecameForeground()

        fun onBecameBackground()
    }

    companion object {
        const val CHECK_DELAY = 500L
        private val TAG = Foreground::class.java.name
        private var instance: Foreground? = null

        fun init(application: Application): Foreground {
            if (instance == null) {
                instance = Foreground()
                application.registerActivityLifecycleCallbacks(instance)
            }

            return instance!!
        }

        fun get(application: Application): Foreground {
            if (instance == null) {
                init(application)
            }
            return instance!!
        }

        fun get(ctx: Context): Foreground {
            if (instance == null) {
                val appContext = ctx.applicationContext
                if (appContext is Application) {
                    init(appContext)
                }
                throw IllegalStateException("Foreground is not initialised and cannot obtain the Application object")
            } else {
                return instance!!
            }
        }

        fun get(): Foreground {
            if (instance == null) {
                throw IllegalStateException("Foreground is not initialised - invoke at least once with parameterised init/get")
            } else {
                return instance!!
            }
        }
    }
}