package com.mindorks.sample.whatsapp.screen.chat

import androidx.compose.Composable
import androidx.ui.core.Modifier
import androidx.ui.foundation.Box
import androidx.ui.foundation.shape.corner.RoundedCornerShape
import androidx.ui.graphics.Color
import androidx.ui.layout.Column
import androidx.ui.layout.Row
import androidx.ui.layout.fillMaxWidth
import androidx.ui.layout.padding
import androidx.ui.unit.dp
import com.mindorks.sample.whatsapp.common.*
import com.mindorks.sample.whatsapp.model.UserChat


@Composable
fun setupOtherUserChat(chats: UserChat) {

    Box(
        backgroundColor = Color(62, 61, 64),
        modifier = Modifier.fillMaxWidth() + Modifier.padding(_10dp, end = _80dp),
        shape = RoundedCornerShape(_8dp)
    ) {
        Row(modifier = Modifier.padding(_10dp)) {
            Column(modifier = Modifier.weight(3.0f, true)) {
                Chats(chats)
            }
        }
    }
}