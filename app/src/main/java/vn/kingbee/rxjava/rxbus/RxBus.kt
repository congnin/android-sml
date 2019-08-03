package vn.kingbee.rxjava.rxbus

import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject

class RxBus {
    private val bus = PublishSubject.create<Any>()

    fun send(any: Any) {
        bus.onNext(any)
    }

    fun toObservable(): Observable<Any> = bus

    fun hasObservers(): Boolean = bus.hasObservers()
}