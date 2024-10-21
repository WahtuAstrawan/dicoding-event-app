package com.example.dicodingevent.data

import com.example.dicodingevent.utils.EventVar

sealed class Result<out R> private constructor() {
    data class Success<out T>(val data: T) : Result<T>()
    data class Error(val message: EventVar<String>) : Result<Nothing>()
    data object Loading : Result<Nothing>()
}