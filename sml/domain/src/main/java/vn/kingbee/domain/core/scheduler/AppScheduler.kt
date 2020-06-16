package vn.kingbee.domain.core.scheduler

import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

object AppScheduler {

    var schedulerProvider: SchedulerProvider
        get() = SchedulerProviderStaticInstance.schedulerProviderInstance!!
        set(instance) {
            SchedulerProviderStaticInstance.schedulerProviderInstance = instance
        }

    fun mainThread(): Scheduler {
        return schedulerProvider.mainThread()
    }

    fun io(): Scheduler {
        return schedulerProvider.io()
    }

    interface SchedulerProvider { //NOSONAR
        fun mainThread(): Scheduler

        fun io(): Scheduler
    }

    class DefaultSchedulerProvider : SchedulerProvider {
        override fun mainThread(): Scheduler {
            return AndroidSchedulers.mainThread()
        }

        override fun io(): Scheduler {
            return Schedulers.io()
        }
    }

    private object SchedulerProviderStaticInstance {
        var schedulerProviderInstance: SchedulerProvider? = null
            get() {
                if (field == null) {
                    schedulerProviderInstance = DefaultSchedulerProvider()
                }
                return field
            }
    }//No implement
}
