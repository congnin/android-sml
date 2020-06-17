package com.mindorks.sample.whatsapp.screen

import androidx.compose.Composable
import androidx.ui.animation.Crossfade
import com.mindorks.sample.whatsapp.navigator.AppNavigation
import com.mindorks.sample.whatsapp.navigator.Navigator
import com.mindorks.sample.whatsapp.screen.chat.ChatScreenView
import com.mindorks.sample.whatsapp.screen.main.HomeView
import com.mindorks.sample.whatsapp.screen.splash.SplashView
import kotlinx.coroutines.CoroutineScope

@Composable
fun App(scope: CoroutineScope) {

    Crossfade(AppNavigation.currentScreen) { screen ->
        when (screen) {
            is Navigator.SplashScreen -> SplashView(scope)
            is Navigator.HomeScreen -> HomeView()
            is Navigator.ChatScreen -> ChatScreenView(screen.user)
        }
    }
}