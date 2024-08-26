package com.example.weatherforecastcompose.model

sealed class AppResult<T> {
    data class Success<T>(val data: T) : AppResult<T>()

    data class Error<T>(val errorType: ErrorType) : AppResult<T>()
}

enum class ErrorType {
    CLIENT,
    SERVER,
    GENERIC,
    IO_CONNECTION
}