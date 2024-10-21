package com.example.dicodingevent.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.example.dicodingevent.data.local.room.EventDao
import com.example.dicodingevent.data.remote.api.ApiService
import com.example.dicodingevent.data.remote.response.Event
import com.example.dicodingevent.data.remote.response.ListEventsItem
import com.example.dicodingevent.utils.AppExecutors
import com.example.dicodingevent.utils.EventVar

class EventRepository private constructor(
    private val apiService: ApiService,
    private val eventDao: EventDao,
    private val appExecutors: AppExecutors
) {
    fun getDetailEvent(id: String?): LiveData<Result<Event>> = liveData {
        emit(Result.Loading)
        try {
            if (id != null) {
                val response = apiService.getEventDetail(id)
                emit(Result.Success(response.event))
            } else {
                emit(Result.Error(EventVar("Terjadi kesalahan memuat data, coba lagi")))
            }
        } catch (e: Exception) {
            emit(Result.Error(EventVar("Terjadi kesalahan memuat data, coba lagi")))
        }
    }

    fun getEvents(status: String): LiveData<Result<List<ListEventsItem>>> = liveData {
        emit(Result.Loading)
        try {
            val response = apiService.getEvents(status)
            val events = response.listEvents
            emit(Result.Success(events))
        } catch (e: Exception) {
            emit(Result.Error(EventVar("Terjadi kesalahan memuat data, coba lagi")))
        }
    }

    fun getEventsWithLimit(status: String, limit: String): LiveData<Result<List<ListEventsItem>>> = liveData {
        emit(Result.Loading)
        try {
            val response = apiService.getEventsWithLimit(status, limit)
            val events = response.listEvents
            emit(Result.Success(events))
        } catch (e: Exception) {
            emit(Result.Error(EventVar("Terjadi kesalahan memuat data, coba lagi")))
        }
    }

    fun searchEvents(status: String, keyword: String): LiveData<Result<List<ListEventsItem>>> = liveData {
        emit(Result.Loading)
        try {
            val response = apiService.searchEvents(status, keyword)
            val events = response.listEvents
            emit(Result.Success(events))
        } catch (e: Exception) {
            emit(Result.Error(EventVar("Terjadi kesalahan mencari data, coba lagi")))
        }
    }

    companion object {
        @Volatile
        private var instance: EventRepository? = null
        fun getInstance(
            apiService: ApiService,
            eventDao: EventDao,
            appExecutors: AppExecutors
        ): EventRepository =
            instance ?: synchronized(this) {
                instance ?: EventRepository(apiService, eventDao, appExecutors)
            }.also { instance = it }
    }
}