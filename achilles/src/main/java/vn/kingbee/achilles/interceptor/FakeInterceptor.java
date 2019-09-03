package vn.kingbee.achilles.interceptor;

import org.json.JSONException;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Response;
import timber.log.Timber;
import vn.kingbee.achilles.model.EndpointElement;
import vn.kingbee.achilles.repository.ContentRepository;
import vn.kingbee.achilles.service.MockContractor;
import vn.kingbee.achilles.service.callback.MockUserInteractCallback;
import vn.kingbee.achilles.service.model.ApiSession;
import vn.kingbee.achilles.util.OkHttpLogUtils;

public class FakeInterceptor implements Interceptor, MockUserInteractCallback {
    private static final String TAG = FakeInterceptor.class.getSimpleName();
    private static final long ONE_SECOND = 1000L;
    private final MockContractor mockController;
    private ContentRepository contentRepository;
    private static final SynchronizedObject objectSync = new SynchronizedObject();
    private String selectedResponseApiName;

    public FakeInterceptor(ContentRepository contentRepository, MockContractor mockController) {
        this.contentRepository = contentRepository;
        this.mockController = mockController;
        mockController.setInterceptorListener(this);
    }

    public Response intercept(Chain chain) throws IOException {
        synchronized (objectSync) {
            objectSync.resetSession();
            OkHttpLogUtils.Companion.logRequest(chain);
            String apiFileName = this.contentRepository.getFileNameFromInterceptorChain(chain);
            EndpointElement endpointElement = this.contentRepository.getEndpointConfigs(apiFileName);
            if (this.mockController.isUserInteractEnable()) {
                this.startCheckingUserInteract(apiFileName, objectSync.getSessionId(), this.getListResult(apiFileName));
            }

            if (this.shouldDelay(endpointElement)) {
                this.delayThreadFromEndpointConfig(endpointElement);
            }

            Response response;
            if (this.isDataAvailable(endpointElement)) {
                response = null;

                try {
                    response = this.contentRepository.getDataResponse(chain.request(), apiFileName, objectSync.getResponseApiName());
                } catch (JSONException e) {
                    Timber.tag(TAG).i(e.getMessage(), new Object[0]);
                }

                OkHttpLogUtils.Companion.logResponse(response);
                return response;
            } else {
                response = null;

                try {
                    response = this.contentRepository.getScenarioResponse(chain.request(), apiFileName);
                } catch (JSONException e) {
                    Timber.tag(TAG).i(e.getMessage(), new Object[0]);
                }

                OkHttpLogUtils.Companion.logResponse(response);
                return response;
            }
        }
    }

    private String[] getListResult(String fileName) {
        return this.contentRepository.getAllListResultFilePath(fileName);
    }

    private void startCheckingUserInteract(String apiFileName, String sessionId, String[] listResult) throws IOException {
        Timber.d(TAG, "1 Start - waiting for user input -> 3s");

        try {
            this.mockController.notifyPrepareApi(apiFileName, sessionId, listResult);
            objectSync.wait(2 * ONE_SECOND);
            Timber.d(TAG, "2 Start check user interact or not");
            if (this.isUserInteract()) {
                objectSync.wait();
            } else {
                this.mockController.notifyApiStarted(sessionId);
            }

            Timber.d(TAG, "3 End user interact");
        } catch (InterruptedException var5) {
            Timber.d(var5, TAG, "3.5 InterruptedException");
            throw new IOException();
        }
        Timber.d(TAG, "4 End waiting");
    }

    private boolean isUserInteract() {
        return objectSync.isUserInteract();
    }

    private boolean shouldDelay(EndpointElement endpointElement) {
        return null != endpointElement && endpointElement.getDelay() != null && 0 < endpointElement.getDelay();
    }

    private boolean isDataAvailable(EndpointElement endpointElement) {
        return null != endpointElement && null != endpointElement.getDataField() && !endpointElement.getDataField().isEmpty();
    }

    public void delayThreadFromEndpointConfig(EndpointElement endpointElement) {
        try {
            Thread.sleep(ONE_SECOND * (long) endpointElement.getDelay());
        } catch (Exception var3) {
            Timber.tag(TAG).e(var3.getMessage(), new Object[0]);
        }
    }

    private void stopWaiting() {
        synchronized (objectSync) {
            objectSync.notifyAll();
        }
    }

    public void onUserStartInteract(String sessionId) {
        Timber.d("onUserStartInteract: %s", sessionId);
        if (sessionId != null) {
            synchronized (objectSync) {
                Timber.d("onUserStartInteract running: %s", sessionId);
                if (sessionId.equals(objectSync.getSessionId())) {
                    objectSync.setUserInteract(true);
                    this.stopWaiting();
                }
            }
        }
    }

    public void onUserEndInteract(String responseApiName, String sessionId) {
        Timber.d("onUserEndInteract: %s", sessionId);
        if (sessionId != null) {
            synchronized (objectSync) {
                Timber.d("onUserEndInteract running: %s", sessionId);
                if (sessionId.equals(objectSync.getSessionId())) {
                    objectSync.setResponseApiName(responseApiName);
                    objectSync.setUserInteract(false);
                    this.stopWaiting();
                }
            }
        }
    }

    public void onUserEndInteract(ApiSession apiSession) {
        this.onUserEndInteract(apiSession.getApiName(), apiSession.getSessionId());
    }
}
