package com.example.dicodingevent.ui.favorite

import androidx.lifecycle.ViewModel
import com.example.dicodingevent.data.EventRepository

class FavoriteViewModel(private val eventRepository: EventRepository) : ViewModel() {
    fun getAllFavoriteEvents() = eventRepository.getAllFavoriteEvent()
}