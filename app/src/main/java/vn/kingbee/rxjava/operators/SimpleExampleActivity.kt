package vn.kingbee.rxjava.operators

import android.os.Bundle
import android.widget.Button
import vn.kingbee.widget.BaseActivity
import vn.kingbee.widget.R
import android.widget.TextView
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.functions.BiFunction
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import vn.kingbee.application.AppConstant

class SimpleExampleActivity : BaseActivity() {
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
            // Run on a background thread
            .subscribeOn(Schedulers.io())
            // Be notified on the main thread
            .observeOn(AndroidSchedulers.mainThread()).subscribe(getObserver())
    }

    private fun getObservable(): Observable<String> =
        Observable.fromArray("janishar ali anwar then flattens the emissions from all of those into a single Observable")
            .flatMap { word -> Observable.fromArray(word.split(" ")) }
            .zipWith(Observable.range(1, Integer.MAX_VALUE),
                BiFunction { s: List<String>, count: Int ->
                    "%2d. %s".format(count, s)
                })

    private fun getObserver(): Observer<String> {
        return object : Observer<String> {
            override fun onSubscribe(d: Disposable) {
                Timber.d(" onSubscribe : %s", d.isDisposed)
            }

            override fun onNext(value: String) {
                textView?.append(" onNext : value : $value");
                textView?.append(AppConstant.LINE_SEPARATOR);
                Timber.d(" onNext : value : %s", value);
            }

            override fun onError(e: Throwable) {
                textView?.append(" onError : " + e.message);
                textView?.append(AppConstant.LINE_SEPARATOR);
                Timber.d(" onError : %s", e.message);
            }

            override fun onComplete() {
                textView?.append(" onComplete");
                textView?.append(AppConstant.LINE_SEPARATOR);
                Timber.d(" onComplete");
            }
        }

    }
}