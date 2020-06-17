package com.mindorks.sample.whatsapp.screen.main.status

import androidx.compose.Composable
import androidx.ui.core.Modifier
import androidx.ui.foundation.AdapterList
import androidx.ui.foundation.drawBackground
import androidx.ui.livedata.observeAsState
import com.mindorks.sample.whatsapp.ambients.fakerRepository
import com.mindorks.sample.whatsapp.common.colorLightGreen
import com.mindorks.sample.whatsapp.common.separator

@Composable
fun StatusView() {

    val fakerRepository = fakerRepository.current
    val statusState = fakerRepository.getStatus().observeAsState(initial = arrayListOf())

    AdapterList(
        data = statusState.value,
        modifier = Modifier.drawBackground(colorLightGreen())
    ) {
        StatusLayout(it)
        separator()
    }
}