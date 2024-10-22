package com.example.dicodingevent.ui.upcoming

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.dicodingevent.data.EventRepository
import com.example.dicodingevent.data.Result
import com.example.dicodingevent.data.remote.response.ListEventsItem

class UpcomingViewModel(private val eventRepository: EventRepository) : ViewModel() {
    private var _cachedEvents: LiveData<Result<List<ListEventsItem>>>? = null
    private var _cachedSearchEvents: LiveData<Result<List<ListEventsItem>>>? = null

    private var lastSearch: String? = null

    private val _searchText = MutableLiveData<String>()
    val searchText: LiveData<String> = _searchText

    fun getEvents(): LiveData<Result<List<ListEventsItem>>> {
        if (_cachedEvents == null) {
            _cachedEvents = eventRepository.getEvents(STATUS)
        }
        return _cachedEvents as LiveData<Result<List<ListEventsItem>>>
    }

    fun searchEvents(
        status: String,
        keyword: String,
        isSubmit: Boolean = false
    ): LiveData<Result<List<ListEventsItem>>> {
        if (_cachedSearchEvents == null || lastSearch != keyword || isSubmit) {
            _cachedSearchEvents = eventRepository.searchEvents(status, keyword)
            lastSearch = keyword
        }
        return _cachedSearchEvents as LiveData<Result<List<ListEventsItem>>>
    }

    fun setSearchText(query: String) {
        if (_searchText.value != query) {
            _searchText.value = query
        }
    }

    companion object{
        const val STATUS = "1"
    }
}