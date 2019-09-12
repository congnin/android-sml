package vn.kingbee.feature.base.callback

import io.reactivex.observers.DisposableObserver
import okhttp3.Headers
import retrofit2.HttpException
import vn.kingbee.feature.base.fragment.BaseFragmentBehavior
import vn.kingbee.widget.base.fragment.BaseView
import java.io.EOFException
import java.io.IOException
import java.lang.ref.WeakReference
import java.net.SocketTimeoutException

abstract class CallbackWrapper<T> : DisposableObserver<T>, IBaseError {
    private val weakReference: WeakReference<BaseFragmentBehavior>

    constructor(view: BaseFragmentBehavior) {
        this.weakReference = WeakReference(view)

    }

    protected abstract fun onSuccess(t: T)

    override fun onNext(t: T) {
        onSuccess(t)
    }

    override fun onError(e: Throwable) {
        val view = weakReference.get()
        view?.hideProgressDialog()

        if(e is HttpException) {

        }
    }
}