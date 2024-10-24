package com.example.dicodingevent.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.dicodingevent.data.EventRepository
import com.example.dicodingevent.data.Result
import com.example.dicodingevent.data.remote.response.ListEventsItem

class HomeViewModel(eventRepository: EventRepository) : ViewModel() {
    val eventsUpcoming: LiveData<Result<List<ListEventsItem>>> = eventRepository.getEventsWithLimit(STATUS_UPCOMING, LIMIT).asLiveData()
    val eventsFinished: LiveData<Result<List<ListEventsItem>>> = eventRepository.getEventsWithLimit(STATUS_FINISHED, LIMIT).asLiveData()

    companion object {
        private const val STATUS_UPCOMING = "1"
        private const val STATUS_FINISHED = "0"
        private const val LIMIT = "5"
    }
}