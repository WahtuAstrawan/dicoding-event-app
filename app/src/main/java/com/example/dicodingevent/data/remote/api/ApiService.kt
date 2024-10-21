package com.example.dicodingevent.data.remote.api

import com.example.dicodingevent.data.remote.response.EventResponse
import com.example.dicodingevent.data.remote.response.EventsResponse
import retrofit2.http.*

interface ApiService {
    @GET("events")
    suspend fun getEvents(
        @Query("active") status: String
    ): EventsResponse

    @GET("events")
    suspend fun getEventsWithLimit(
        @Query("active") status: String,
        @Query("limit") limit: String
    ): EventsResponse

    @GET("events/{id}")
    suspend fun getEventDetail(
        @Path("id") id: String
    ): EventResponse

    @GET("events")
    suspend fun searchEvents(
        @Query("active") status: String,
        @Query("q") keyword: String
    ): EventsResponse
}