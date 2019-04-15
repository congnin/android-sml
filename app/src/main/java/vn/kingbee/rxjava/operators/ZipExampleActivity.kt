package vn.kingbee.rxjava.operators

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import io.reactivex.Observable
import io.reactivex.ObservableOnSubscribe
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.functions.BiFunction
import vn.kingbee.rxjava.model.User
import vn.kingbee.rxjava.utils.MockUpUtils
import vn.kingbee.widget.BaseActivity
import vn.kingbee.widget.R
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import vn.kingbee.application.AppConstant

class ZipExampleActivity : BaseActivity() {
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
        Observable.zip(getCricketFansObservable(),
            getFootballFansObservable(),
            BiFunction<List<User>, List<User>, List<User>> { cricketFans, footballFans -> MockUpUtils.filterUserWhoLovesBoth(cricketFans, footballFans) })
            // Run on a background thread
            .subscribeOn(Schedulers.io())
            // Be notified on the main thread
            .observeOn(AndroidSchedulers.mainThread()).subscribe(getObserver());
    }

    private fun getCricketFansObservable(): Observable<List<User>> {
        return Observable.create(ObservableOnSubscribe<List<User>> { e ->
            if (!e.isDisposed) {
                e.onNext(MockUpUtils.getUserListWhoLovesCricket())
                e.onComplete()
            }
        }).subscribeOn(Schedulers.io())
    }

    private fun getFootballFansObservable(): Observable<List<User>> {
        return Observable.create(ObservableOnSubscribe<List<User>> { e ->
            if (!e.isDisposed) {
                e.onNext(MockUpUtils.getUserListWhoLovesFootball())
                e.onComplete()
            }
        }).subscribeOn(Schedulers.io())
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
                    textView?.append(" firstname : " + user.firstName)
                    textView?.append(AppConstant.LINE_SEPARATOR)
                }
                Timber.d(" onNext : %s", t.size)
            }

            override fun onError(e: Throwable) {
                textView?.append(" onError : " + e.message)
                textView?.append(AppConstant.LINE_SEPARATOR)
                Timber.d(" onError : %s", e.message)
            }
        }

    }
}