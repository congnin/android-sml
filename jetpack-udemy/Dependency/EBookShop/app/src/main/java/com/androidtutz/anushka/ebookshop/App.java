package com.androidtutz.anushka.ebookshop;

import android.app.Application;

import com.androidtutz.anushka.ebookshop.di.AppModule;
import com.androidtutz.anushka.ebookshop.di.DaggerEBookShopComponent;
import com.androidtutz.anushka.ebookshop.di.EBookShopComponent;

public class App extends Application {

    private  static App app;
    private EBookShopComponent eBookShopComponent;


    @Override
    public void onCreate() {
        super.onCreate();
        app = this;

        eBookShopComponent = DaggerEBookShopComponent
        .builder()
        .appModule(new AppModule(this))
        .build();
    }

    public static App getApp() {
        return app;
    }

    public EBookShopComponent geteBookShopComponent() {
        return eBookShopComponent;
    }
}
