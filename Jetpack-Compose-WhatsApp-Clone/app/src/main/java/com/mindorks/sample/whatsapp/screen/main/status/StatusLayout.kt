package com.mindorks.sample.whatsapp.screen.main.status

import androidx.compose.Composable
import androidx.ui.core.Modifier
import androidx.ui.foundation.Box
import androidx.ui.foundation.Text
import androidx.ui.layout.*
import androidx.ui.text.TextStyle
import androidx.ui.unit.dp
import androidx.ui.unit.sp
import com.mindorks.sample.whatsapp.common.*
import com.mindorks.sample.whatsapp.model.Status

@Composable
fun StatusLayout(status: Status) {

    Box(
        backgroundColor = colorLightGreen(),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(modifier = Modifier.padding(all = _10dp)) {
            Row(modifier = Modifier.weight(0.5f, true)) {
                Box(modifier = Modifier.fillMaxWidth().fillMaxHeight()) {
                    UserImage(status.imageUrl)
                }
            }
            Spacer(modifier = Modifier.preferredSize(_12dp))
            Column(modifier = Modifier.weight(3.0f, true)) {
                UserName(status.name)
                Spacer(modifier = Modifier.preferredSize(_2dp))
                UserTiming(status)
            }
        }
    }
}


@Composable
fun UserTiming(status: Status) {

    Text(
        text = status.time,
        style = TextStyle(
            fontSize = _16sp,
            color = colorGreen()
        )
    )
}