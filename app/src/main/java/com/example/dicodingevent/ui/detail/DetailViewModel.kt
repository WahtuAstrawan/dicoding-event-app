package com.example.dicodingevent.ui.detail

import com.example.dicodingevent.utils.EventVar
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.dicodingevent.data.api.ApiConfig
import com.example.dicodingevent.data.response.Event
import com.example.dicodingevent.data.response.EventResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailViewModel : ViewModel() {
    private val _event = MutableLiveData<Event>()
    val event: LiveData<Event> = _event

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _errorMsg = MutableLiveData<EventVar<String>>()
    val errorMsg: LiveData<EventVar<String>> = _errorMsg

    companion object {
        private const val TAG = "DetailViewModel"
    }

    fun getDetailEvent(id: String?) {
        if (_event.value != null) {
            return
        }

        if (id != null) {
            _isLoading.value = true
            val client = ApiConfig.getApiService().getEventDetail(id)
            client.enqueue(object : Callback<EventResponse> {
                override fun onResponse(
                    call: Call<EventResponse>,
                    response: Response<EventResponse>
                ) {
                    _isLoading.value = false
                    if (response.isSuccessful) {
                        _event.value = response.body()?.event
                    } else {
                        Log.e(TAG, "onFailure: ${response.message()}")
                        _errorMsg.value = EventVar("Gagal memuat data, coba lagi")
                    }
                }

                override fun onFailure(call: Call<EventResponse>, t: Throwable) {
                    _isLoading.value = false
                    Log.e(TAG, "onFailure: ${t.message.toString()}")
                    _errorMsg.value = EventVar("Terjadi kesalahan memuat data, coba lagi")
                }

            })
        } else {
            _errorMsg.value = EventVar("Terjadi kesalahan memuat data, coba lagi")
        }
    }
}