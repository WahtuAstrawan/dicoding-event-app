package com.example.dicodingevent.ui.favorite

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.dicodingevent.data.EventRepository

class FavoriteViewModel(eventRepository: EventRepository) : ViewModel() {
    val favoriteEvents = eventRepository.getAllFavoriteEvents().asLiveData()
}