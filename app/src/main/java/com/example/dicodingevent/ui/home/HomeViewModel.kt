package com.example.dicodingevent.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.dicodingevent.data.EventRepository
import com.example.dicodingevent.data.Result
import com.example.dicodingevent.data.remote.response.ListEventsItem

class HomeViewModel(private val eventRepository: EventRepository) : ViewModel() {
    private var _cachedEventsUpcoming: LiveData<Result<List<ListEventsItem>>>? = null
    private var _cachedEventsFinished: LiveData<Result<List<ListEventsItem>>>? = null

    fun getEventsUpcoming(): LiveData<Result<List<ListEventsItem>>> {
        if (_cachedEventsUpcoming == null) {
            _cachedEventsUpcoming = eventRepository.getEventsWithLimit(STATUS_UPCOMING, LIMIT)
        }
        return _cachedEventsUpcoming as LiveData<Result<List<ListEventsItem>>>
    }

    fun getEventsFinished(): LiveData<Result<List<ListEventsItem>>> {
        if (_cachedEventsFinished == null) {
            _cachedEventsFinished = eventRepository.getEventsWithLimit(STATUS_FINISHED, LIMIT)
        }
        return _cachedEventsFinished as LiveData<Result<List<ListEventsItem>>>
    }

    companion object {
        private const val STATUS_UPCOMING = "1"
        private const val STATUS_FINISHED = "0"
        private const val LIMIT = "5"
    }
}