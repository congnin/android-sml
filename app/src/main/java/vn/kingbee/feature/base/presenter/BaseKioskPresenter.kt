package vn.kingbee.feature.base.presenter

import io.reactivex.disposables.Disposable
import vn.kingbee.domain.dataprocess.AppBus
import vn.kingbee.domain.dataprocess.Runtime
import vn.kingbee.feature.base.fragment.BaseFragmentBehavior
import java.lang.ref.WeakReference

open class BaseKioskPresenter<T : BaseFragmentBehavior> : IBasePresenter<T> {
    protected var runtime: Runtime

    protected var appBus: AppBus

    @Volatile
    private var mView: WeakReference<T>? = null
    private var mViewHashCode: Int? = null
    protected var subscriptions: Set<Disposable>? = null

    constructor(runtime: Runtime, appBus: AppBus) {
        this.runtime = runtime
        this.appBus = appBus
    }

    override fun setView(view: T) {
        val previousView = this.mView

        if (previousView != null) {
            this.mView = null
        }
        this.mView = WeakReference(view)
        mViewHashCode = mView?.get().hashCode()
    }

    override fun getView(): T? {
        if (mView != null) {
            return mView!!.get()
        }
        return null
    }

    override fun resume() {
        //do nothing
    }

    override fun pause() {
        //do nothing
    }

    override fun destroy() {
        destroyAllSubscription()

        val previousView = mView
        if (previousView == mView) {
            this.mView = null
        } else {
            throw IllegalStateException("Unexpected view! previousView = $previousView, view to unbind = $mView")
        }
    }

    override fun updateUI() {
        if (mView != null && mView?.get() != null) {
            mView!!.get()?.viewLoaded()
        }
    }

    override fun destroy(viewHashCode: Int) {
        if (viewHashCode == mViewHashCode) {
            destroy()
        }
    }

    protected fun addOneSubscription(d: Disposable) {
        if (this.subscriptions == null) {
            this.subscriptions = HashSet()
        }

        (this.subscriptions as HashSet).add(d)
    }

    protected fun destroyAllSubscription() {
        if (this.subscriptions != null) {
            val iterator = subscriptions?.iterator()

            while (iterator != null && iterator.hasNext()) {
                val subscription = iterator.next()
                if (!subscription.isDisposed) {
                    subscription.dispose()
                }
            }
        }
    }
}