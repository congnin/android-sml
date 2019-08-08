package vn.kingbee.feature.base

import io.reactivex.disposables.Disposable
import vn.kingbee.application.appbus.AppBus
import vn.kingbee.application.runtime.Runtime
import java.lang.ref.WeakReference

class BasePresenter<T : BaseFragmentBehavior> : IBasePresenter<T> {
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
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun pause() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun destroy() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun updateUI() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun destroy(viewHashCode: Long) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}