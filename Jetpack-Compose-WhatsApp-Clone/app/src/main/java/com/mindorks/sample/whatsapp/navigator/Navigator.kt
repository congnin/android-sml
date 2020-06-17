package com.mindorks.sample.whatsapp.navigator

import androidx.compose.Model
import com.mindorks.sample.whatsapp.model.User

sealed class Navigator {
    object SplashScreen : Navigator()
    object HomeScreen : Navigator()
    data class ChatScreen(val user: User) : Navigator()
}

sealed class HomeNavigator {
    object ChatScreen : HomeNavigator()
    object StatusScreen : HomeNavigator()
    object CallsScreen : HomeNavigator()
}

@Model
object AppNavigation {
    var currentScreen: Navigator = Navigator.SplashScreen
}

@Model
object HomeNavigation {
    var currentScreen: HomeNavigator = HomeNavigator.ChatScreen
}

fun navigateTo(destination: Navigator) {
    AppNavigation.currentScreen = destination
}

fun navigateTo(destination: HomeNavigator) {
    HomeNavigation.currentScreen = destination
}
