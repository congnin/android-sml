package com.mindorks.sample.whatsapp.screen.main

import androidx.compose.Composable
import androidx.compose.MutableState
import androidx.compose.state
import androidx.ui.foundation.Text
import androidx.ui.graphics.Color
import androidx.ui.layout.Column
import androidx.ui.material.*
import com.mindorks.sample.whatsapp.common.*
import com.mindorks.sample.whatsapp.data.Constant
import com.mindorks.sample.whatsapp.navigator.HomeNavigator
import com.mindorks.sample.whatsapp.navigator.navigateTo

@Composable
fun HomeTopBar() {

    val clickedState: MutableState<Int> = state { 1 }

    Column {
        TopAppBar(
            backgroundColor = colorTopBar(),
            elevation = _0dp,
            title = {
                Text(
                    text = "WhatsApp",
                    color = Color.White,
                    style = MaterialTheme.typography.h5
                )
            }
        )
        TabRow(
            items = Constant.tabs,
            backgroundColor = colorTopBar(),
            selectedIndex = clickedState.value,
            indicatorContainer = {
                TabRow.IndicatorContainer(tabPositions = it, selectedIndex = clickedState.value) {
                    LayoutIndicator(color = colorGreen())
                }
            }
        ) { index, text ->
            Tab(
                text = {
                    Text(
                        text,
                        style = MaterialTheme.typography.h6
                    )
                },
                activeColor = colorGreen(),
                inactiveColor = Color.Gray,
                selected = clickedState.value == index,
                onSelected = {
                    clickedState.value = index
                    when (index) {
                        0 -> navigateTo(HomeNavigator.StatusScreen)
                        1 -> navigateTo(HomeNavigator.ChatScreen)
                        2 -> navigateTo(HomeNavigator.CallsScreen)
                    }
                }
            )
        }
    }
}

@Composable
fun LayoutIndicator(color: Color) {

    Divider(thickness = _2dp, color = color)
}