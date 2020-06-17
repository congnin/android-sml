package com.mindorks.sample.whatsapp.screen.splash

import androidx.compose.Composable
import androidx.compose.remember
import androidx.lifecycle.MutableLiveData
import androidx.ui.livedata.observeAsState
import androidx.ui.material.Scaffold
import androidx.ui.material.ScaffoldState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@Composable
fun SplashView(scope: CoroutineScope, scaffoldState: ScaffoldState = remember { ScaffoldState() }) {

    val launchHomeView = MutableLiveData(false)
    val viewState = launchHomeView.observeAsState(false)

    Scaffold(
        scaffoldState = scaffoldState,
        bodyContent = {
            viewState.value?.let { launchHome ->
                SplashLayout(launchHome, scaffoldState = scaffoldState)
            }
        }
    )
    launchHomeScreen(scope, launchHomeView)
}

fun launchHomeScreen(
    scope: CoroutineScope,
    launchHomeView: MutableLiveData<Boolean>
) {
    scope.launch {
        delay(3000)
        launchHomeView.postValue(true)
    }
}