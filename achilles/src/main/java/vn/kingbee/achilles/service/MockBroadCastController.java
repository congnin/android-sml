package vn.kingbee.achilles.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import com.google.gson.Gson;

import androidx.annotation.NonNull;
import vn.kingbee.achilles.service.callback.MockUserInteractCallback;
import vn.kingbee.achilles.service.callback.UserInteractCallback;
import vn.kingbee.achilles.service.model.ApiSession;
import vn.kingbee.achilles.service.model.PrepareApi;
import vn.kingbee.achilles.util.GsonHelper;

public class MockBroadCastController implements MockContractor {
    private static final String TAG = MockBroadCastController.class.getSimpleName();
    private static final String EXTRA_DATA = "EXTRA_DATA";
    private static final String ACTION_PREPARE_API = "ACTION_PREPARE_API";
    private static final String ACTION_API_STARTED = "ACTION_API_STARTED";
    private static final String ACTION_ON_USER_START = "ACTION_ON_USER_START";
    private static final String ACTION_ON_USER_END = "ACTION_ON_USER_END";
    private final Context appContext;
    private final Gson gson;
    private final UserInteractConfig config;
    private UserInteractCallback userInteractListener;
    private MockUserInteractCallback mockListener;
    private BroadcastReceiver userInteractReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            if (MockBroadCastController.this.userInteractListener != null && intent.getAction() != null) {
                String action = intent.getAction();
                byte type = -1;
                switch (action.hashCode()) {
                    case -1777989997:
                        if (action.equals(ACTION_API_STARTED)) {
                            type = 1;
                        }
                        break;
                    case 358623769:
                        if (action.equals(ACTION_PREPARE_API)) {
                            type = 0;
                        }
                }

                switch (type) {
                    case 0:
                        PrepareApi prepareApi = (PrepareApi) GsonHelper.jsonToObject(MockBroadCastController.this.gson,
                                intent.getStringExtra("PrepareApi"), PrepareApi.class);
                        MockBroadCastController.this.userInteractListener.onPrepareApi(prepareApi);
                        break;
                    case 1:
                        MockBroadCastController.this.userInteractListener.onApiStarted(intent.getStringExtra(EXTRA_DATA));
                }
            }

        }
    };
    private BroadcastReceiver mockInteractReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            if (MockBroadCastController.this.mockListener != null && intent.getAction() != null) {
                String action = intent.getAction();
                byte type = -1;
                switch (action.hashCode()) {
                    case -328288539:
                        if (action.equals(ACTION_ON_USER_START)) {
                            type = 1;
                        }
                        break;
                    case 1751598110:
                        if (action.equals(ACTION_ON_USER_END)) {
                            type = 0;
                        }
                }

                switch (type) {
                    case 0:
                        ApiSession apiSession = (ApiSession) GsonHelper.jsonToObject(MockBroadCastController.this.gson,
                                intent.getStringExtra("ApiSession"), ApiSession.class);
                        MockBroadCastController.this.mockListener.onUserEndInteract(apiSession);
                        break;
                    case 1:
                        MockBroadCastController.this.mockListener.onUserStartInteract(intent.getStringExtra(EXTRA_DATA));
                }
            }

        }
    };

    public MockBroadCastController(Context context, @NonNull UserInteractConfig config) {
        this.appContext = context.getApplicationContext();
        this.gson = new Gson();
        this.config = config;
    }

    public boolean isUserInteractEnable() {
        return this.config.isEnableUserInteractFeature();
    }

    public void notifyPrepareApi(String apiFileName, String sessionId, String[] listResult) {
        PrepareApi prepareApi = new PrepareApi(apiFileName, sessionId, listResult);
        this.appContext.sendBroadcast(this.getIntentData(ACTION_PREPARE_API, "PrepareApi",
                GsonHelper.jsonFromObject(this.gson, prepareApi)));
    }

    private Intent getIntentData(String action, String key, String data) {
        Intent intent = new Intent(action);
        intent.putExtra(key, data);
        return intent;
    }

    public void notifyApiStarted(String sessionId) {
        this.appContext.sendBroadcast(this.getIntentData(ACTION_API_STARTED, EXTRA_DATA, sessionId));
    }

    public void notifyUserStartInteract(String sessionId) {
        this.appContext.sendBroadcast(this.getIntentData(ACTION_ON_USER_START, EXTRA_DATA, sessionId));
    }

    public void notifyUserEndInteract(String selectedApiName, String sessionId) {
        ApiSession apiSession = new ApiSession(selectedApiName, sessionId);
        this.appContext.sendBroadcast(this.getIntentData(ACTION_ON_USER_END, "ApiSession", GsonHelper.jsonFromObject(this.gson, apiSession)));
    }

    public void registerUserInteractListener(UserInteractCallback userInteractListener) {
        this.userInteractListener = userInteractListener;
        this.appContext.registerReceiver(this.userInteractReceiver, this.getUserInteractAction());
    }

    private IntentFilter getUserInteractAction() {
        IntentFilter filter = new IntentFilter(ACTION_API_STARTED);
        filter.addAction(ACTION_PREPARE_API);
        return filter;
    }

    public void setInterceptorListener(MockUserInteractCallback fakeInterceptor) {
        this.mockListener = fakeInterceptor;
        this.appContext.registerReceiver(this.mockInteractReceiver, this.getMockInterceptorListener());
    }

    private IntentFilter getMockInterceptorListener() {
        IntentFilter filter = new IntentFilter(ACTION_ON_USER_END);
        filter.addAction(ACTION_ON_USER_START);
        return filter;
    }
}
