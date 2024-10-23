package com.example.dicodingevent.di

import android.content.Context
import com.example.dicodingevent.data.EventRepository
import com.example.dicodingevent.data.local.room.EventDatabase
import com.example.dicodingevent.data.remote.api.ApiConfig
import com.example.dicodingevent.ui.SettingPreferences
import com.example.dicodingevent.ui.dataStore

object Injection {
    fun provideRepository(context: Context): EventRepository {
        val apiService = ApiConfig.getApiService()
        val database = EventDatabase.getInstance(context)
        val dao = database.eventDao()
        return EventRepository.getInstance(apiService, dao)
    }

    fun providePreferences(context: Context): SettingPreferences {
        return SettingPreferences.getInstance(context.dataStore)
    }
}