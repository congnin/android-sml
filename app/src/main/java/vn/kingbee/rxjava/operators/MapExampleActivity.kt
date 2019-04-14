package vn.kingbee.rxjava.operators

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import vn.kingbee.application.AppConstant
import vn.kingbee.rxjava.model.ApiUser
import vn.kingbee.rxjava.model.User
import vn.kingbee.rxjava.utils.MockUpUtils
import vn.kingbee.widget.BaseActivity
import vn.kingbee.widget.R


class MapExampleActivity : BaseActivity() {
    var btn: Button? = null
    var textView: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_example)
        btn = findViewById(R.id.btn)
        textView = findViewById(R.id.textView)

        btn?.setOnClickListener {
            doSomeWork()
        }
    }

    private fun doSomeWork() {
        getObservable()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .map { MockUpUtils.convertApiUserListToUserList(it) }
            .subscribe(getObserver())
    }

    private fun getObservable(): Observable<List<ApiUser>> {
        return Observable.create {
            if (!it.isDisposed) {
                it.onNext(MockUpUtils.getApiUserList())
                it.onComplete()
            }
        }
    }

    private fun getObserver(): Observer<List<User>> {
        return object : Observer<List<User>> {
            override fun onComplete() {
                textView?.append(" onComplete")
                textView?.append(AppConstant.LINE_SEPARATOR)
                Timber.d(" onComplete")
            }

            override fun onSubscribe(d: Disposable) {
                Timber.d(" onSubscribe : %s", d.isDisposed)
            }

            override fun onNext(t: List<User>) {
                textView?.append(" onNext")
                textView?.append(AppConstant.LINE_SEPARATOR)
                for (user in t) {
                    textView?.append(" firstName : " + user.firstName)
                    textView?.append(AppConstant.LINE_SEPARATOR)
                }
                Timber.d(" onNext : %s", t.size)
            }

            override fun onError(e: Throwable) {
                textView?.append(" onError : " + e.message)
                textView?.append(AppConstant.LINE_SEPARATOR)
                Timber.d( " onError : %s", e.message)
            }
        }
    }

}