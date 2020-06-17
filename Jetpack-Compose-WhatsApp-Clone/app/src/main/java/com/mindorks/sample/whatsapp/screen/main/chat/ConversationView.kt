package com.mindorks.sample.whatsapp.screen.main.chat

import androidx.compose.Composable
import androidx.ui.core.Modifier
import androidx.ui.foundation.AdapterList
import androidx.ui.foundation.drawBackground
import androidx.ui.graphics.Color
import androidx.ui.livedata.observeAsState
import com.mindorks.sample.whatsapp.ambients.fakerRepository
import com.mindorks.sample.whatsapp.common.separator

@Composable
fun ChatView() {

    val fakerRepository = fakerRepository.current
    val chatState = fakerRepository.getChatList().observeAsState(initial = arrayListOf())

    AdapterList(
        data = chatState.value,
        modifier = Modifier.drawBackground(Color(21, 31, 40))
    ) {
        ConversationLayout(chat = it)
        separator()
    }
}