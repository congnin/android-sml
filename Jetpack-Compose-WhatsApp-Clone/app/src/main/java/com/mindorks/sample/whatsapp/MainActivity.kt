package com.mindorks.sample.whatsapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.compose.Providers
import androidx.ui.core.setContent
import com.mindorks.sample.whatsapp.ambients.fakerRepository
import com.mindorks.sample.whatsapp.data.repository.FakerRepository
import com.mindorks.sample.whatsapp.screen.App
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel

class MainActivity : ComponentActivity() {

    private val scope = MainScope()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            Providers(fakerRepository provides FakerRepository()) {
                App(scope)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        scope.cancel()
    }
}