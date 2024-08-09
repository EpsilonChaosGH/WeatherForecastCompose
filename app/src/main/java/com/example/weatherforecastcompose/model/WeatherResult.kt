package com.example.weatherforecastcompose.model

sealed class WeatherResult<T> {
    data class Success<T>(val data: T) : WeatherResult<T>()

    data class Error<T>(val errorType: ErrorType) : WeatherResult<T>()
}

enum class ErrorType {
    CLIENT,
    SERVER,
    GENERIC,
    IO_CONNECTION,
    WRONG_CITY
}