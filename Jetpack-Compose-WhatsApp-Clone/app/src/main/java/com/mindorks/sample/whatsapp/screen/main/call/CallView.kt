package com.mindorks.sample.whatsapp.screen.main.call

import androidx.compose.Composable
import androidx.ui.core.Modifier
import androidx.ui.foundation.VerticalScroller
import androidx.ui.foundation.drawBackground
import androidx.ui.graphics.Color
import androidx.ui.layout.Column
import androidx.ui.livedata.observeAsState
import com.mindorks.sample.whatsapp.ambients.fakerRepository
import com.mindorks.sample.whatsapp.common.colorLightGreen
import com.mindorks.sample.whatsapp.common.separator

@Composable
fun CallView() {

    val fakerRepository = fakerRepository.current
    val callState = fakerRepository.getCalls().observeAsState(initial = arrayListOf())

    VerticalScroller(modifier = Modifier.drawBackground(colorLightGreen())) {
        Column {
            callState.value.forEach {
                CallLayout(it)
                separator()
            }
        }
    }
}