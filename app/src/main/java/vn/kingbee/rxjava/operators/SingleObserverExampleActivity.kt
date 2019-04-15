package vn.kingbee.rxjava.operators

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import io.reactivex.Single
import io.reactivex.SingleObserver
import io.reactivex.SingleOnSubscribe
import io.reactivex.disposables.Disposable
import timber.log.Timber
import vn.kingbee.application.AppConstant
import vn.kingbee.widget.BaseActivity
import vn.kingbee.widget.R

class SingleObserverExampleActivity : BaseActivity() {
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
        Single.create(SingleOnSubscribe<Long> { emitter ->
            while (!emitter.isDisposed) {
                val time = System.currentTimeMillis()
                if (time % 2 != 0L) {
                    emitter.onError(IllegalStateException("Odd millisecond!"))
                    break
                } else {
                    emitter.onSuccess(time)
                }
            }
        }).subscribe(getSingleObserver())
    }

    private fun getSingleObserver(): SingleObserver<Long> {
        return object : SingleObserver<Long> {
            override fun onSuccess(value: Long) {
                textView?.append(" onNext : value : $value")
                textView?.append(AppConstant.LINE_SEPARATOR)
                Timber.d(" onNext value : %s", value)
            }

            override fun onSubscribe(d: Disposable) {
                Timber.d(" onSubscribe : %s", d.isDisposed)
            }

            override fun onError(e: Throwable) {
                textView?.append(" onError : " + e.message)
                textView?.append(AppConstant.LINE_SEPARATOR)
                Timber.d(" onError : %s", e.message)
            }

        }
    }
}