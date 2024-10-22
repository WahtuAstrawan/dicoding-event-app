package com.example.dicodingevent.ui.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.dicodingevent.data.EventRepository
import com.example.dicodingevent.data.Result
import com.example.dicodingevent.data.remote.response.Event

class DetailViewModel(private val eventRepository: EventRepository) : ViewModel() {
    private var _cachedEventDetail: LiveData<Result<Event>>? = null

    fun getDetailEvent(id: String?): LiveData<Result<Event>> {
        if (_cachedEventDetail == null) {
            _cachedEventDetail = eventRepository.getDetailEvent(id)
        }
        return _cachedEventDetail as LiveData<Result<Event>>
    }
}