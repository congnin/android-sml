package com.mindorks.sample.whatsapp.screen.main.call

import androidx.compose.Composable
import androidx.ui.core.Alignment
import androidx.ui.core.Modifier
import androidx.ui.foundation.Box
import androidx.ui.foundation.ContentGravity
import androidx.ui.foundation.Text
import androidx.ui.graphics.Color
import androidx.ui.layout.*
import androidx.ui.text.TextStyle
import androidx.ui.text.style.TextAlign
import androidx.ui.text.style.TextOverflow
import androidx.ui.unit.dp
import androidx.ui.unit.sp
import com.mindorks.sample.whatsapp.R
import com.mindorks.sample.whatsapp.common.*
import com.mindorks.sample.whatsapp.model.Call

@Composable
fun CallLayout(call: Call) {

    Box(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.padding(all = _10dp) + Modifier.padding(
                top = _4dp,
                bottom = _4dp
            )
        ) {
            Row(modifier = Modifier.weight(0.5f, true)) {
                Box(
                    gravity = ContentGravity.Center
                ) {
                    UserImage(call.imageUrl)
                }
            }

            Spacer(modifier = Modifier.preferredSize(_12dp))

            Column(modifier = Modifier.weight(3.0f, true)) {
                UserName(call.name)
                Spacer(modifier = Modifier.preferredSize(2.dp))
                UserTime(call)
            }
            Row(modifier = Modifier.weight(0.5f, true)) {
                Box(
                    modifier = Modifier.preferredHeightIn(maxHeight = 40.dp) + Modifier.fillMaxHeight(),
                    gravity = ContentGravity.Center
                ) {
                    ImageLoader.load(
                        imageUrl = R.drawable.ic_communications, modifier = Modifier
                    ) {

                    }
                }
            }
        }
    }
}


@Composable
fun UserTime(call: Call) {

    Row(verticalGravity = Alignment.CenterVertically) {
        ImageLoader.load(
            imageUrl = call.voiceStatus,
            modifier = Modifier.width(_12dp) + Modifier.height(_12dp)
        ) { }

        Spacer(modifier = Modifier.preferredSize(_8dp))

        Text(
            text = call.time,
            style = TextStyle(
                fontSize = _12sp,
                color = Color.Gray,
                textAlign = TextAlign.Center
            ),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}
