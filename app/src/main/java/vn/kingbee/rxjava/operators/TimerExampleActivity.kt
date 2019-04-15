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
import vn.kingbee.widget.BaseActivity
import vn.kingbee.widget.R
import java.util.concurrent.TimeUnit


class TimerExampleActivity : BaseActivity() {
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
            .subscribe(getObserver())
    }

    private fun getObservable(): Observable<out Long> {
        return Observable.timer(2, TimeUnit.SECONDS)
    }

    private fun getObserver(): Observer<Long> {
        return object : Observer<Long> {
            override fun onComplete() {
                textView?.append(" onComplete")
                textView?.append(AppConstant.LINE_SEPARATOR)
                Timber.d( " onComplete")
            }

            override fun onSubscribe(d: Disposable) {
                Timber.d( " onSubscribe : %s", d.isDisposed)
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