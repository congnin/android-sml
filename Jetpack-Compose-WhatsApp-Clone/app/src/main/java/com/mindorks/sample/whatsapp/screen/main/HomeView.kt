package com.mindorks.sample.whatsapp.screen.main

import androidx.compose.Composable
import androidx.compose.remember
import androidx.ui.material.Scaffold
import androidx.ui.material.ScaffoldState

@Composable
fun HomeView(scaffoldState: ScaffoldState = remember { ScaffoldState() }) {

    Scaffold(
        scaffoldState = scaffoldState,
        topAppBar = { HomeTopBar() },
        bodyContent = { HomeLayout(scaffoldState = scaffoldState) }
    )
}