package com.example.dicodingevent.ui.upcoming

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dicodingevent.data.EventRepository
import com.example.dicodingevent.data.Result
import com.example.dicodingevent.data.remote.response.ListEventsItem
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class UpcomingViewModel(private val eventRepository: EventRepository) : ViewModel() {
    private val _events = MutableLiveData<Result<List<ListEventsItem>>>()
    val events: LiveData<Result<List<ListEventsItem>>> = _events

    private val _searchText = MutableLiveData<String>()
    val searchText: LiveData<String> = _searchText

    private fun getEvents() {
        viewModelScope.launch {
            eventRepository.getEvents(STATUS).collectLatest { result ->
                _events.value = result
            }
        }
    }

    fun searchEvents() {
        viewModelScope.launch {
            eventRepository.searchEvents(STATUS, searchText.value ?: "").collectLatest { result ->
                _events.value = result
            }
        }
    }

    fun setSearchText(query: String) {
        _searchText.value = query
    }

    companion object{
        const val STATUS = "1"
    }

    init {
        getEvents()
    }
}