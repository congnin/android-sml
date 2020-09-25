package com.androidtutz.anushka.didemo;

import android.app.Application;

public class App extends Application {
    private static App app;
    private SmartPhoneComponent smartPhoneComponent;

    public static App getApp() {
        return app;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        app = this;

        smartPhoneComponent = DaggerSmartPhoneComponent.builder()
                .memoryCardModule(new MemoryCardModule(100))
                .build();
    }

    public SmartPhoneComponent getSmartPhoneComponent() {
        return smartPhoneComponent;
    }
}
