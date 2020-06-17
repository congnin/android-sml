package com.mindorks.sample.whatsapp.screen.chat

import androidx.compose.Composable
import androidx.ui.core.Modifier
import androidx.ui.foundation.AdapterList
import androidx.ui.foundation.drawBackground
import androidx.ui.layout.Column
import androidx.ui.layout.fillMaxHeight
import androidx.ui.layout.padding
import com.mindorks.sample.whatsapp.common._10dp
import com.mindorks.sample.whatsapp.common._48dp
import com.mindorks.sample.whatsapp.common.colorLightGreen
import com.mindorks.sample.whatsapp.common.spacer
import com.mindorks.sample.whatsapp.model.UserChat

@Composable
fun ChatScreenLayout(chats: List<UserChat>) {

    AdapterList(
        chats,
        modifier = Modifier.fillMaxHeight()
                + Modifier.drawBackground(colorLightGreen())
                + Modifier.padding(
            top = _10dp,
            start = _10dp,
            end = _10dp,
            bottom = _48dp
        )
    ) {
        if (it.id == 2) {
            Column {
                setupOtherUserChat(it)
                spacer()
            }
        } else {
            setupMyChat(it)
            spacer()
        }
    }
}