package com.example.dicodingevent.data.api

import com.example.dicodingevent.data.response.EventResponse
import com.example.dicodingevent.data.response.EventsResponse
import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    @GET("events?active={status}")
    fun getEvents(
        @Path("status") status: Int
    ): Call<EventsResponse>

    @GET("events?active={status}&limit={limit}")
    fun getEventsWithLimit(
        @Path("status") status: Int,
        @Path("limit") limit: Int
    ): Call<EventsResponse>

    @GET("events/{id}")
    fun getEventDetail(
        @Path("id") id: Int
    ): Call<EventResponse>

    @GET("events?active=-1&q={keyword}")
    fun searchEvents(
        @Path("keyword") keyword: String
    ): Call<EventsResponse>
}