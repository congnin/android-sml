package com.mindorks.sample.whatsapp.screen.chat

import androidx.compose.Composable
import androidx.ui.livedata.observeAsState
import androidx.ui.material.Scaffold
import com.mindorks.sample.whatsapp.ambients.fakerRepository
import com.mindorks.sample.whatsapp.data.source.addChat
import com.mindorks.sample.whatsapp.model.User

@Composable
fun ChatScreenView(user: User) {

    val fakerRepository = fakerRepository.current
    val chats = fakerRepository.getChat().observeAsState(initial = arrayListOf())

    Scaffold(
        topAppBar = {
            ChatTopBar(user)
        },
        bodyContent = {
            ChatScreenLayout(chats = chats.value)
        }, bottomAppBar = {
            ChatInputField {
                if (it.isNotEmpty()) {
                    addChat(it)
                }
            }
        }
    )
}