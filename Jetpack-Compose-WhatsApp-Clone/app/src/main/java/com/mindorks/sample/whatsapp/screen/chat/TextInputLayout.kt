package com.mindorks.sample.whatsapp.screen.chat

import androidx.compose.Composable
import androidx.compose.state
import androidx.ui.core.Alignment
import androidx.ui.core.Modifier
import androidx.ui.foundation.Box
import androidx.ui.foundation.ContentGravity
import androidx.ui.foundation.TextFieldValue
import androidx.ui.layout.Arrangement
import androidx.ui.layout.Row
import androidx.ui.layout.height
import com.mindorks.sample.whatsapp.R
import com.mindorks.sample.whatsapp.common.EditTextField
import com.mindorks.sample.whatsapp.common.ImageLoader
import com.mindorks.sample.whatsapp.common._30dp


@Composable
fun ChatInputField(click: (String) -> Unit) {

    val text = state { TextFieldValue(text = "") }

    Row(verticalGravity = Alignment.CenterVertically) {
        Row(modifier = Modifier.weight(1.8f, true)) {
            EditTextField(
                value = text.value,
                onValueChange = { value ->
                    text.value = value
                }
            )
        }
        Row(
            modifier = Modifier.weight(0.2f, true),
            horizontalArrangement = Arrangement.Center
        ) {
            Box(
                gravity = ContentGravity.Center
            ) {
                ImageLoader.load(
                    imageUrl = R.drawable.ic_send,
                    modifier = Modifier.weight(1f).height(_30dp)
                ) {
                    click(text.value.text)
                    text.value = TextFieldValue(text = "")
                }
            }
        }
    }
}