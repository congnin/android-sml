package com.mindorks.sample.whatsapp.screen.chat

import androidx.compose.Composable
import androidx.ui.foundation.Text
import androidx.ui.graphics.Color
import androidx.ui.text.TextStyle
import androidx.ui.text.style.TextOverflow
import com.mindorks.sample.whatsapp.common._16sp
import com.mindorks.sample.whatsapp.model.UserChat

@Composable
fun Chats(chat: UserChat) {

    Text(
        text = chat.chat,
        style = TextStyle(
            fontSize = _16sp,
            color = Color.White
        ),
        maxLines = 1,
        overflow = TextOverflow.Ellipsis
    )
}