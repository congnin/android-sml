package vn.kingbee.rxjava.operators

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import vn.kingbee.widget.BaseActivity
import vn.kingbee.widget.R
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import vn.kingbee.application.AppConstant
import java.util.concurrent.TimeUnit

class IntervalExampleActivity : BaseActivity() {
    var btn: Button? = null
    var textView: TextView? = null
    private val disposables = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_example)
        btn = findViewById(R.id.btn)
        textView = findViewById(R.id.textView)

        btn?.setOnClickListener {
            doSomeWork()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        disposables.clear() // clearing it : do not emit after destroy
    }

    private fun doSomeWork() {
        disposables.add(getObservable()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(getObserver()))
    }

    private fun getObservable() : Observable<out Long> =
            Observable.interval(0, 2, TimeUnit.SECONDS).take(60)

    private fun getObserver() : DisposableObserver<Long> {
        return object : DisposableObserver<Long>() {
            override fun onComplete() {
                textView?.append(" onComplete")
                textView?.append(AppConstant.LINE_SEPARATOR)
                Timber.d( " onComplete")
            }

            override fun onNext(value: Long) {
                textView?.append(" onNext : value : $value")
                textView?.append(AppConstant.LINE_SEPARATOR)
                Timber.d( " onNext : value : %s", value)
            }

            override fun onError(e: Throwable) {
                textView?.append(" onError : " + e.message)
                textView?.append(AppConstant.LINE_SEPARATOR)
                Timber.d( " onError : %s", e.message)
            }

        }
    }
}