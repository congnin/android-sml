package vn.kingbee.rxjava.rxbus

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import vn.kingbee.application.MyApp
import vn.kingbee.rxjava.model.Events
import vn.kingbee.widget.R

class RxBusActivity : AppCompatActivity() {
    @BindView(R.id.textView)
    lateinit var textView: TextView
    @BindView(R.id.btn)
    lateinit var btn: Button

    private val disposables = CompositeDisposable()

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_example)
        ButterKnife.bind(this)

        disposables.add(MyApp.getInstance()
            .bus
            .toObservable()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                if (it is Events.AutoEvent) {
                    textView.text = "Auto Event Received"
                } else if (it is Events.TapEvent) {
                    textView.text = "Tap Event Received"
                }
            })
    }

    override fun onDestroy() {
        super.onDestroy()
        disposables.clear()
    }

    @OnClick(R.id.btn)
    fun onClicked() {
        MyApp.getInstance().bus.send(Events.TapEvent())
    }

    companion object {
        private val TAG = RxBusActivity::class.java.simpleName
    }
}