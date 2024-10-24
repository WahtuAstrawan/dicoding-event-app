package com.example.dicodingevent.ui.favorite

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.dicodingevent.data.EventRepository
import com.example.dicodingevent.data.local.entity.FavoriteEventEntity

class FavoriteViewModel(eventRepository: EventRepository) : ViewModel() {
    val favoriteEvents: LiveData<List<FavoriteEventEntity>> = eventRepository.getAllFavoriteEvents().asLiveData()
}