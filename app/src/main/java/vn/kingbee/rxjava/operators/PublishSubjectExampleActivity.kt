package vn.kingbee.rxjava.operators

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import io.reactivex.subjects.PublishSubject
import timber.log.Timber
import vn.kingbee.application.AppConstant
import vn.kingbee.widget.BaseActivity
import vn.kingbee.widget.R

class PublishSubjectExampleActivity : BaseActivity() {

    @BindView(R.id.btn)
    lateinit var btn: Button
    @BindView(R.id.textView)
    lateinit var textView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_example)
        ButterKnife.bind(this)
    }

    @OnClick(R.id.btn)
    fun btnClicked() {
        doSomeWork()
    }

    private fun doSomeWork() {
        val source = PublishSubject.create<Int>()

        source.subscribe(getFirstObserver()) // it will get 1, 7, 5, 10 and onComplete

        source.onNext(1)
        source.onNext(7)
        source.onNext(5)

        /*
         * it will emit 10 and onComplete for second observer also.
         */
        source.subscribe(getSecondObserver())

        source.onNext(10)
        source.onComplete()
    }

    private fun getFirstObserver(): Observer<Int> {
        return object : Observer<Int> {
            override fun onComplete() {
                textView.append(" First onComplete")
                textView.append(AppConstant.LINE_SEPARATOR)
                Timber.d("$TAG First onComplete")
            }

            override fun onSubscribe(d: Disposable) {
                Timber.d("$TAG First onSubscribe : ${d.isDisposed}")
            }

            override fun onNext(t: Int) {
                textView.append(" First onNext : value : $t")
                textView.append(AppConstant.LINE_SEPARATOR)
                Timber.d( "$TAG First onNext value : $t")
            }

            override fun onError(e: Throwable) {
                textView.append(" First onError : " + e.message)
                textView.append(AppConstant.LINE_SEPARATOR)
                Timber.d( "$TAG First onError : ${e.message}")
            }

        }
    }

    private fun getSecondObserver(): Observer<Int?> {
        return object : Observer<Int?> {

            override fun onSubscribe(d: Disposable) {
                textView.append(" Second onSubscribe : isDisposed :" + d.isDisposed)
                Timber.d("$TAG Second onSubscribe : ${d.isDisposed}")
                textView.append(AppConstant.LINE_SEPARATOR)
            }

            override fun onNext(t: Int) {
                textView.append(" Second onNext : value : $t")
                textView.append(AppConstant.LINE_SEPARATOR)
                Timber.d("$TAG Second onNext value : $t")
            }

            override fun onError(e: Throwable) {
                textView.append(" Second onError : " + e.message)
                textView.append(AppConstant.LINE_SEPARATOR)
                Timber.d("$TAG Second onError : ${e.message}")
            }

            override fun onComplete() {
                textView.append(" Second onComplete")
                textView.append(AppConstant.LINE_SEPARATOR)
                Timber.d("$TAG Second onComplete")
            }
        }
    }

    companion object {
        private val TAG = PublishSubjectExampleActivity::class.java.simpleName
    }
}