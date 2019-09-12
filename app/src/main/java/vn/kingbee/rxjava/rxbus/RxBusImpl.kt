package vn.kingbee.rxjava.rxbus

import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject

class RxBusImpl : RxBus {
    private val bus = PublishSubject.create<Any>()

    override fun send(any: Any) {
        bus.onNext(any)
    }

    override fun toObservable(): Observable<Any> = bus

    override fun hasObservers(): Boolean = bus.hasObservers()
}