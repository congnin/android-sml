package com.example.android.drawableanimations

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commitNow
import com.example.android.drawableanimations.home.HomeFragment

class MainActivity : AppCompatActivity(R.layout.main_activity) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState == null) {
            supportFragmentManager.commitNow {
                replace(R.id.main, HomeFragment())
            }
        }
    }
}