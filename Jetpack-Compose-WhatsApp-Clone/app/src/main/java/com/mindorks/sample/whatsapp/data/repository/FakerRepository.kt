package com.mindorks.sample.whatsapp.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.mindorks.sample.whatsapp.data.source.calls
import com.mindorks.sample.whatsapp.data.source.chat
import com.mindorks.sample.whatsapp.data.source.chatsList
import com.mindorks.sample.whatsapp.data.source.status
import com.mindorks.sample.whatsapp.model.Call
import com.mindorks.sample.whatsapp.model.Chat
import com.mindorks.sample.whatsapp.model.Status
import com.mindorks.sample.whatsapp.model.UserChat

class FakerRepository {

    fun getChatList(): LiveData<List<Chat>> = MutableLiveData(chatsList)

    fun getChat(): LiveData<List<UserChat>> = MutableLiveData(chat)

    fun getStatus(): LiveData<List<Status>> = MutableLiveData(status)

    fun getCalls(): LiveData<List<Call>> = MutableLiveData(calls)

}