package com.example.dicodingevent.ui.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dicodingevent.data.EventRepository
import com.example.dicodingevent.data.Result
import com.example.dicodingevent.data.local.entity.FavoriteEventEntity
import com.example.dicodingevent.data.remote.response.Event
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class DetailViewModel(private val eventRepository: EventRepository) : ViewModel() {
    private val _eventDetail = MutableLiveData<Result<Event>>()
    val eventDetail: LiveData<Result<Event>> = _eventDetail

    private val _isFavorite = MutableLiveData<Boolean>()
    val isFavorite: LiveData<Boolean> = _isFavorite

    fun getDetailEvent(id: String?) {
        viewModelScope.launch {
            eventRepository.getEventDetail(id).collectLatest { result ->
                _eventDetail.value = result
            }
        }
    }

    fun isFavoriteEvent(id: String) {
        viewModelScope.launch {
            val result = eventRepository.isFavoriteEvent(id)
            _isFavorite.value = result
        }
    }

    fun insertFavoriteEvent(event: List<FavoriteEventEntity>) {
        viewModelScope.launch {
            eventRepository.insertFavoriteEvent(event)
        }
    }

    fun deleteFavoriteEvent(id: String) {
        viewModelScope.launch {
            eventRepository.deleteFavoriteEvent(id)
        }
    }
}