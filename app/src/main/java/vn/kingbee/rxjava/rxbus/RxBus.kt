package vn.kingbee.rxjava.rxbus

import io.reactivex.Observable

interface RxBus {
    fun send(any: Any)

    fun toObservable(): Observable<Any>

    fun hasObservers(): Boolean
}