package vn.kingbee.rxjava.operators

import android.os.Bundle
import android.os.SystemClock
import android.widget.Button
import vn.kingbee.widget.BaseActivity
import io.reactivex.disposables.CompositeDisposable
import android.widget.TextView
import io.reactivex.Observable
import vn.kingbee.widget.R
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import vn.kingbee.application.AppConstant

class DisposableExampleActivity : BaseActivity() {
    var btn: Button? = null
    var textView: TextView? = null
    private val disposables = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_example)
        btn = findViewById(R.id.btn)
        textView = findViewById(R.id.textView)

        btn?.setOnClickListener { doSomeWork() }
    }

    override fun onDestroy() {
        super.onDestroy()
        disposables.clear()
    }

    private fun doSomeWork() {
        disposables.add(sampleObservable()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(object : DisposableObserver<String>() {
                override fun onComplete() {
                    textView?.append(" onComplete")
                    textView?.append(AppConstant.LINE_SEPARATOR)
                    Timber.d( " onComplete")
                }

                override fun onNext(value: String) {
                    textView?.append(" onNext : value : $value");
                    textView?.append(AppConstant.LINE_SEPARATOR);
                    Timber.d( " onNext value : %s", value);
                }

                override fun onError(e: Throwable) {
                    textView?.append(" onError : " + e.message)
                    textView?.append(AppConstant.LINE_SEPARATOR)
                    Timber.d( " onError : %s", e.message)
                }

            }))
    }

    private fun sampleObservable(): Observable<String> {
        return Observable.defer {
            // Do some long running operation
            SystemClock.sleep(2000)
            Observable.just("one", "two", "three", "four", "five")
        }
    }
}