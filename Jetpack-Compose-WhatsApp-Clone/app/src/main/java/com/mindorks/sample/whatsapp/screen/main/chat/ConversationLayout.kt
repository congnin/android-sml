package com.mindorks.sample.whatsapp.screen.main.chat

import androidx.compose.Composable
import androidx.ui.core.Alignment
import androidx.ui.core.Modifier
import androidx.ui.foundation.Box
import androidx.ui.foundation.Clickable
import androidx.ui.foundation.Text
import androidx.ui.foundation.shape.corner.CircleShape
import androidx.ui.graphics.Color
import androidx.ui.layout.*
import androidx.ui.text.TextStyle
import androidx.ui.text.style.TextOverflow
import androidx.ui.unit.dp
import androidx.ui.unit.sp
import com.mindorks.sample.whatsapp.common.*
import com.mindorks.sample.whatsapp.model.Chat
import com.mindorks.sample.whatsapp.model.User
import com.mindorks.sample.whatsapp.navigator.Navigator
import com.mindorks.sample.whatsapp.navigator.navigateTo

@Composable
fun ConversationLayout(chat: Chat) {

    Box(
        modifier = Modifier.fillMaxWidth() + Modifier.padding(top = _4dp, bottom = _4dp)
    ) {
        Clickable(onClick = {
            navigateTo(Navigator.ChatScreen(User(2, chat.name, chat.url)))
        }) {
            Row(modifier = Modifier.padding(_10dp)) {
                Row(modifier = Modifier.weight(0.5f, true)) {
                    Box(modifier = Modifier.fillMaxWidth().fillMaxHeight()) {
                        UserImage(chat.url)
                    }
                }
                Spacer(modifier = Modifier.preferredSize(_12dp))
                Column(modifier = Modifier.weight(3.0f, true)) {
                    UserName(chat.name)
                    Spacer(modifier = Modifier.preferredSize(_2dp))
                    UserChat(chat)
                }
                Column(modifier = Modifier.weight(1.0f, true), horizontalGravity = Alignment.End) {
                    MessageTime(chat)
                    Spacer(modifier = Modifier.preferredSize(_2dp))
                    UnreadCount(chat)
                }
            }
        }
    }
}

@Composable
fun UserChat(chat: Chat) {

    Text(
        text = chat.chat,
        style = TextStyle(
            fontSize = _16sp,
            color = Color.Gray
        ),
        maxLines = 1,
        overflow = TextOverflow.Ellipsis
    )
}


@Composable
fun MessageTime(chat: Chat) {

    Text(
        text = chat.time,
        style = TextStyle(
            fontSize = _12sp,
            color = colorGreen()
        )
    )
}

@Composable
fun UnreadCount(chat: Chat) {

    if (chat.unreadCount != "0") {
        setupUnreadCount(chat.unreadCount)
    }
}

@Composable
fun setupUnreadCount(count: String) {

    Column(horizontalGravity = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier.preferredSize(_20dp),
            backgroundColor = colorGreen(),
            gravity = Alignment.Center,
            shape = CircleShape
        ) {
            Text(
                text = count,
                style = TextStyle(
                    fontSize = _12sp,
                    color = colorLightGreen()
                )
            )
        }
    }
}