package com.androidtutz.anushka.activitystatesdemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class SecondActivity extends AppCompatActivity {

    private static final String TAG = "lifecycle";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        Log.i(TAG,"***************    SecondActivity onCreate() ");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i(TAG,"***************    SecondActivity onStart() ");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(TAG,"***************    SecondActivity onResume() ");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i(TAG,"***************    SecondActivity onPause() ");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i(TAG,"***************    SecondActivity onStop() ");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i(TAG,"***************    SecondActivity onDestroy() ");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.i(TAG,"***************    SecondActivity onRestart() ");
    }

}
