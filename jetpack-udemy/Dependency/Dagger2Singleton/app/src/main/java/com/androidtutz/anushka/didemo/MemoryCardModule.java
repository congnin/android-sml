package com.androidtutz.anushka.didemo;

import android.util.Log;

import dagger.Module;
import dagger.Provides;

@Module
public class MemoryCardModule {
    private int memorySize;
    private static final String TAG = "SmartPhone";

    public MemoryCardModule(int memorySize) {
        this.memorySize = memorySize;
    }

    @Provides
     MemoryCard provideMemoryCard(){

        Log.d(TAG, " size of  the memory card is  "+memorySize);
        return new MemoryCard();
    }
}
