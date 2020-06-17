package com.mindorks.sample.whatsapp.common

import androidx.compose.Composable
import androidx.ui.core.Alignment
import androidx.ui.core.Modifier
import androidx.ui.foundation.Border
import androidx.ui.foundation.TextField
import androidx.ui.foundation.TextFieldValue
import androidx.ui.foundation.shape.corner.RoundedCornerShape
import androidx.ui.graphics.Color
import androidx.ui.layout.fillMaxWidth
import androidx.ui.layout.padding
import androidx.ui.layout.wrapContentHeight
import androidx.ui.material.Surface
import androidx.ui.unit.Dp


@Composable
fun EditTextField(
    value: TextFieldValue,
    onValueChange: (TextFieldValue) -> Unit
) {
    Surface(
        modifier = Modifier.padding(_16dp),
        shape = RoundedCornerShape(_8dp),
        border = Border(size = Dp.Hairline, color = Color.LightGray)
    ) {
        TextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.padding(_16dp) + Modifier.wrapContentHeight(align = Alignment.CenterVertically) + Modifier.fillMaxWidth()
        )

    }
}