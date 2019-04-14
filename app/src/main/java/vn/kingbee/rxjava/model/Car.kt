package vn.kingbee.rxjava.model

import io.reactivex.Observable
import io.reactivex.ObservableSource
import java.util.concurrent.Callable


class Car {
    private var brand: String? = null

    fun brandDeferObservable(): Observable<String> {
        return Observable.defer { Observable.just(brand!!) }
    }
}