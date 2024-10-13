package com.example.dicodingevent.ui.home

import com.example.dicodingevent.utils.EventVar
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

class HomeViewModel : ViewModel() {

    private val _listEventUp = MutableLiveData<List<ListEventsItem>>()
    val listEventUp: LiveData<List<ListEventsItem>> = _listEventUp

    private val _listEventFin = MutableLiveData<List<ListEventsItem>>()
    val listEventFin: LiveData<List<ListEventsItem>> = _listEventFin

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _errorMsg = MutableLiveData<EventVar<String>>()
    val errorMsg: LiveData<EventVar<String>> = _errorMsg

    companion object {
        private const val TAG = "HomeViewModel"
        private const val STATUS_UPCOMING = "1"
        private const val STATUS_FINISHED = "0"
    }

    private fun getEventsUpcoming() {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getEventsWithLimit(STATUS_UPCOMING, "5")
        client.enqueue(object : Callback<EventsResponse> {
            override fun onResponse(
                call: Call<EventsResponse>,
                response: Response<EventsResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _listEventUp.value = response.body()?.listEvents
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

    private fun getEventsFinished() {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getEventsWithLimit(STATUS_FINISHED, "5")
        client.enqueue(object : Callback<EventsResponse> {
            override fun onResponse(
                call: Call<EventsResponse>,
                response: Response<EventsResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _listEventFin.value = response.body()?.listEvents
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

    init {
        getEventsUpcoming()
        getEventsFinished()
    }

}