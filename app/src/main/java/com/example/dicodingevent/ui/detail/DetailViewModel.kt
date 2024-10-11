package com.example.dicodingevent.ui.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.dicodingevent.data.response.Event

class DetailViewModel: ViewModel() {
    private val _event = MutableLiveData<Event>()
    val event: LiveData<Event> = _event
}