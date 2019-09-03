package vn.kingbee.widget.base.activity

import android.os.Bundle
import android.view.MotionEvent
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import vn.kingbee.application.MyApp
import vn.kingbee.rxjava.model.Events
import vn.kingbee.rxjava.rxbus.RxBus
import vn.kingbee.widget.notification.NotificationManager
import vn.kingbee.widget.notification.NotificationManagerImpl

abstract class KioskBaseActivity : BaseActivityImpl() {
    lateinit var rxBus: RxBus

    private var mTimeoutSubscription: Disposable? = null
    private var mAppSubscription: Disposable? = null
    private var mImeiNotInWhiteList: Disposable? = null

    lateinit var notification: NotificationManager<NotificationManagerImpl.NotificationData>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        rxBus = MyApp.getInstance().mAppComponent.rxBus()
        notification = NotificationManagerImpl(window)
    }

    override fun onResume() {
        super.onResume()

        mImeiNotInWhiteList = rxBus.toObservable()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({it ->
            if(it is Events.ImeiNotInWhiteListEvent && !isFinishing) {
                notification
            }})
    }

    protected fun isUserLoggedIn(): Boolean = false

    override fun onPause() {
        super.onPause()

        if (mTimeoutSubscription != null) {
            mTimeoutSubscription?.dispose()
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        if (mTimeoutSubscription != null) {
            mTimeoutSubscription?.dispose()
        }
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        return super.dispatchTouchEvent(ev)
    }

    protected fun allowRestartTimeOutWhenTouch() = true

    fun notifyImeiNotInWhiteList() {

    }
}