package vn.kingbee.rxjava.operators

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import vn.kingbee.application.AppConstant
import vn.kingbee.widget.R
import java.util.concurrent.TimeUnit

class ThrottleFirstExampleActivity : AppCompatActivity() {
    lateinit var btn: Button
    lateinit var textView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_example)
        btn = findViewById(R.id.btn)
        textView = findViewById(R.id.textView)

        btn.setOnClickListener {
            doSomeWork()
        }
    }

    /*
    * Using throttleFirst() -> if the source Observable has emitted no items since
    * the last time it was sampled, the Observable that results from this operator
    * will emit no item for that sampling period.
    */
    private fun doSomeWork() {
        getObservable()
            .throttleFirst(500, TimeUnit.MILLISECONDS)
            // Run on a background thread
            .subscribeOn(Schedulers.io())
            // Be notified on the main thread
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(getObserver())
    }

    @Throws(Exception::class)
    private fun getObservable(): Observable<Int> {
        return Observable.create { emitter ->
            // send events with simulated time wait
            Thread.sleep(0)
            emitter.onNext(1) //skip
            emitter.onNext(2) // deliver
            Thread.sleep(505)
            emitter.onNext(3) //skip
            Thread.sleep(99)
            emitter.onNext(4) //skip
            Thread.sleep(100)
            emitter.onNext(5) //skip
            emitter.onNext(6) //deliver
            Thread.sleep(305)
            emitter.onNext(7) //deliver
            Thread.sleep(510)
            emitter.onComplete()
        }
    }

    private fun getObserver(): Observer<Int> {
        return object : Observer<Int> {
            override fun onComplete() {
                textView.append(" onComplete")
                textView.append(AppConstant.LINE_SEPARATOR)
                Timber.d(" onComplete")
            }

            override fun onSubscribe(d: Disposable) {
                Timber.d(" onSubscribe : %s", d.isDisposed)
            }

            override fun onNext(value: Int) {
                textView.append(" onNext : ")
                textView.append(AppConstant.LINE_SEPARATOR)
                textView.append(" value : $value")
                textView.append(AppConstant.LINE_SEPARATOR)
                Timber.d(" onNext ")
                Timber.d(" value : %s", value)
            }

            override fun onError(e: Throwable) {
                textView.append(" onError : " + e.message)
                textView.append(AppConstant.LINE_SEPARATOR)
                Timber.d(" onError : %s", e.message)
            }
        }
    }
}