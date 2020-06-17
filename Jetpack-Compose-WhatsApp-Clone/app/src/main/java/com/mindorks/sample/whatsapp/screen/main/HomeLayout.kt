package com.mindorks.sample.whatsapp.screen.main

import androidx.compose.Composable
import androidx.ui.animation.Crossfade
import androidx.ui.material.Scaffold
import androidx.ui.material.ScaffoldState
import com.mindorks.sample.whatsapp.navigator.HomeNavigator
import com.mindorks.sample.whatsapp.navigator.HomeNavigation
import com.mindorks.sample.whatsapp.screen.main.call.CallView
import com.mindorks.sample.whatsapp.screen.main.chat.ChatView
import com.mindorks.sample.whatsapp.screen.main.status.StatusView

@Composable
fun HomeLayout(scaffoldState: ScaffoldState) {

    Scaffold(
        scaffoldState = scaffoldState,
        bodyContent = {
            Crossfade(current = HomeNavigation.currentScreen) { screen ->
                when (screen) {
                    is HomeNavigator.ChatScreen -> ChatView()
                    is HomeNavigator.CallsScreen -> CallView()
                    is HomeNavigator.StatusScreen -> StatusView()
                }
            }
        }
    )
}