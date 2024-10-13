package com.example.dicodingevent.ui.finished

import EventVar
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.dicodingevent.data.api.ApiConfig
import com.example.dicodingevent.data.response.EventsResponse
import com.example.dicodingevent.data.response.ListEventsItem
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FinishedViewModel : ViewModel() {

    private val _listEvent = MutableLiveData<List<ListEventsItem>>()
    val listEvent: LiveData<List<ListEventsItem>> = _listEvent

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _searchText = MutableLiveData<String>()
    val searchText: LiveData<String> = _searchText

    private val _errorMsg = MutableLiveData<EventVar<String>>()
    val errorMsg: LiveData<EventVar<String>> = _errorMsg

    companion object{
        private const val TAG = "FinishedViewModel"
        private const val STATUS = "0"
    }

    fun setSearchText(query: String) {
        _searchText.value = query
    }

    private fun getEvents() {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getEvents(STATUS)
        client.enqueue(object: Callback<EventsResponse> {
            override fun onResponse(
                call: Call<EventsResponse>,
                response: Response<EventsResponse>
            ) {
                _isLoading.value = false
                if(response.isSuccessful) {
                    _listEvent.value = response.body()?.listEvents
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                    _errorMsg.value = EventVar("Gagal memuat data, coba lagi")
                }
            }

            override fun onFailure(call: Call<EventsResponse>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailure: ${t.message.toString()}")
                _errorMsg.value = EventVar("Terjadi kesalahan memuat data, coba lagi")
            }

        })
    }

    fun searchEvents() {
        _isLoading.value = true
        val client = ApiConfig.getApiService().searchEvents(STATUS, searchText.value.toString())
        client.enqueue(object: Callback<EventsResponse> {
            override fun onResponse(
                call: Call<EventsResponse>,
                response: Response<EventsResponse>
            ) {
                _isLoading.value = false
                if(response.isSuccessful) {
                    _listEvent.value = response.body()?.listEvents
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                    _errorMsg.value = EventVar("Gagal mencari data, coba lagi")
                }
            }

            override fun onFailure(call: Call<EventsResponse>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailure: ${t.message.toString()}")
                _errorMsg.value = EventVar("Terjadi kesalahan memuat data, coba lagi")
            }

        })
    }

    init {
        getEvents()
    }
}