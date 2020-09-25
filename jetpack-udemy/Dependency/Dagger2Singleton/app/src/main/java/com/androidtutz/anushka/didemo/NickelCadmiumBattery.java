package com.androidtutz.anushka.didemo;

import android.util.Log;

import javax.inject.Inject;

public class NickelCadmiumBattery implements Battery {
    private static final String TAG = "SmartPhone";

    @Inject
    public NickelCadmiumBattery() {
    }

    @Override
    public void showType() {
        Log.d(TAG, " this is a Nickel Cadmium Battery ... ");
    }
}
