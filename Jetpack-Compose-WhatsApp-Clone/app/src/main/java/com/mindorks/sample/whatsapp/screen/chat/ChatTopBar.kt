package com.mindorks.sample.whatsapp.screen.chat

import androidx.compose.Composable
import androidx.ui.core.Modifier
import androidx.ui.foundation.Icon
import androidx.ui.foundation.Text
import androidx.ui.graphics.Color
import androidx.ui.layout.Column
import androidx.ui.layout.padding
import androidx.ui.material.IconButton
import androidx.ui.material.TopAppBar
import androidx.ui.res.vectorResource
import androidx.ui.text.TextStyle
import androidx.ui.text.style.TextAlign
import com.mindorks.sample.whatsapp.R
import com.mindorks.sample.whatsapp.common.UserImage
import com.mindorks.sample.whatsapp.common._18sp
import com.mindorks.sample.whatsapp.common._8dp
import com.mindorks.sample.whatsapp.common.colorTopBar
import com.mindorks.sample.whatsapp.model.User
import com.mindorks.sample.whatsapp.navigator.Navigator
import com.mindorks.sample.whatsapp.navigator.navigateTo

@Composable
fun ChatTopBar(user: User) {

    Column {
        TopAppBar(
            backgroundColor = colorTopBar(),
            navigationIcon = {
                IconButton(onClick = {
                    navigateTo(Navigator.HomeScreen)
                }) {
                    Icon(
                        vectorResource(id = R.drawable.ic_arrow_back),
                        modifier = Modifier.padding(start = _8dp),
                        tint = Color.White
                    )
                }
            },
            actions = {
                UserImage(url = user.imageUrl)
            },
            title = {
                Text(
                    text = user.name,
                    color = Color.White,
                    style = TextStyle(fontSize = _18sp, textAlign = TextAlign.Center)
                )
            }
        )
    }
}