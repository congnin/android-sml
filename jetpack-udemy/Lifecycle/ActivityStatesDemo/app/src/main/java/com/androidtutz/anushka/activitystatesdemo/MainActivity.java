package com.androidtutz.anushka.activitystatesdemo;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "lifecycle";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Log.i(TAG,"***************    MainActivity onCreate() ");

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              Intent intent = new Intent(MainActivity.this,SecondActivity.class);
              startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i(TAG,"***************    MainActivity onStart() ");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(TAG,"***************    MainActivity onResume() ");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i(TAG,"***************    MainActivity onPause() ");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i(TAG,"***************    MainActivity onStop() ");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i(TAG,"***************    MainActivity onDestroy() ");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.i(TAG,"***************    MainActivity onRestart() ");
    }
}
