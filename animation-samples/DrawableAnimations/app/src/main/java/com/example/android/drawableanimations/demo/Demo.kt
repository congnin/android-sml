package com.example.android.drawableanimations.demo

import androidx.fragment.app.Fragment

data class Demo(
    val title: String,
    val createFragment: () -> Fragment
)