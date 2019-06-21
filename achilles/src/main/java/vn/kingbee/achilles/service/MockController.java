package vn.kingbee.achilles.service;

import android.content.Context;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;
import vn.kingbee.achilles.service.callback.MockUserInteractCallback;
import vn.kingbee.achilles.service.callback.UserInteractCallback;
import vn.kingbee.achilles.util.RxJavaHandler;

public class MockController implements MockContractor {
    private static final String TAG = MockController.class.getSimpleName();
    private final Context appContext;
    private UserInteractCallback userInteractListener;
    private MockUserInteractCallback mockListener;
    private Disposable currentTask;
    private RxJavaHandler rxjavaHandler = new RxJavaHandler();

    public MockController(Context context) {
        this.appContext = context.getApplicationContext();
    }

    public boolean isUserInteractEnable() {
        return true;
    }

    public void notifyPrepareApi(String apiFileName, String sessionId, String[] listResult) {
        this.disposeCurrentTask();
        this.currentTask = this.rxjavaHandler.runOnMainThread((result) -> {
            if (this.userInteractListener != null) {
                this.userInteractListener.onPrepareApi(apiFileName, sessionId, listResult);
            }

        });
    }

    public void notifyApiStarted(String sessionId) {
        this.disposeCurrentTask();
        this.currentTask = this.rxjavaHandler.runOnMainThread((result) -> {
            if (this.userInteractListener != null) {
                this.userInteractListener.onApiStarted(sessionId);
            }

        });
    }

    private void disposeCurrentTask() {
        if (this.currentTask != null && !this.currentTask.isDisposed()) {
            this.currentTask.dispose();
        }

    }

    public void notifyUserStartInteract(String sessionId) {
        this.disposeCurrentTask();
        this.currentTask = this.getObservableNotifyStartInteract(sessionId).subscribe((aBoolean) -> {
            this.disposeCurrentTask();
        });
    }

    public void notifyUserEndInteract(String selectedApiName, String sessionId) {
        this.disposeCurrentTask();
        this.currentTask = this.getObservableNotifyEndInteract(selectedApiName, sessionId).subscribe((aBoolean) -> {
            this.disposeCurrentTask();
        });
    }

    private Observable<Boolean> getObservableNotifyStartInteract(String sessionId) {
        return Observable.fromCallable(() -> {
            Timber.d(TAG + "Notify Start Interact");
            if (this.mockListener != null) {
                this.mockListener.onUserStartInteract(sessionId);
                return true;
            } else {
                return false;
            }
        }).subscribeOn(Schedulers.io());
    }

    private Observable<Boolean> getObservableNotifyEndInteract(String selectedResponseName, String sessionId) {
        return Observable.fromCallable(() -> {
            if (this.mockListener != null) {
                this.mockListener.onUserEndInteract(selectedResponseName, sessionId);
                return true;
            } else {
                return false;
            }
        }).subscribeOn(Schedulers.io());
    }

    public void registerUserInteractListener(UserInteractCallback userInteractListener) {
        this.userInteractListener = userInteractListener;
    }

    public void setInterceptorListener(MockUserInteractCallback fakeInterceptor) {
        this.mockListener = fakeInterceptor;
    }
}
