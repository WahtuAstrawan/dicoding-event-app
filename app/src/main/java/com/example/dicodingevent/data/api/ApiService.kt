package com.example.dicodingevent.data.api

import com.example.dicodingevent.data.response.EventResponse
import com.example.dicodingevent.data.response.EventsResponse
import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    @GET("events")
    fun getEvents(
        @Query("active") status: String
    ): Call<EventsResponse>

    @GET("events")
    fun getEventsWithLimit(
        @Query("active") status: String,
        @Query("limit") limit: String
    ): Call<EventsResponse>

    @GET("events/{id}")
    fun getEventDetail(
        @Path("id") id: String
    ): Call<EventResponse>

    @GET("events")
    fun searchEvents(
        @Query("active") status: String,
        @Query("q") keyword: String
    ): Call<EventsResponse>
}