package vn.kingbee.achilles.util;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;

public class RxJavaHandler {
    public RxJavaHandler() {
    }

    public DisposableObserver runOnMainThread(final RxJavaHandler.Callback<Boolean> callback) {
        DisposableObserver<Boolean> autoDispose = new DisposableObserver<Boolean>() {
            public void onNext(Boolean aBoolean) {
                callback.run(aBoolean);
            }

            public void onError(Throwable e) {
                this.dispose();
            }

            public void onComplete() {
                this.dispose();
            }
        };
        this.getMainThreadObservable().subscribe(autoDispose);
        return autoDispose;
    }

    private Observable<Boolean> getMainThreadObservable() {
        return Observable.just(true).observeOn(AndroidSchedulers.mainThread());
    }

    @FunctionalInterface
    public interface Callback<T> {
        void run(T var1);
    }
}
