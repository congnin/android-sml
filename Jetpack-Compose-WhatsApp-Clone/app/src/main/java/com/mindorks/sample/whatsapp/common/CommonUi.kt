package com.mindorks.sample.whatsapp.common

import androidx.compose.Composable
import androidx.ui.foundation.Text
import androidx.ui.graphics.Color
import androidx.ui.material.Divider
import androidx.ui.text.TextStyle
import androidx.ui.text.font.FontWeight
import androidx.ui.unit.sp

@Composable
fun separator() = Divider(color = Color(33, 46, 55))

@Composable
fun spacer() = Divider(color = Color.Transparent, thickness = _8dp)

@Composable
fun UserImage(url: String) = ImageLoader.load(url = url)

@Composable
fun UserName(name: String) {

    Text(
        text = name,
        style = TextStyle(
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp,
            color = Color.White
        )
    )
}