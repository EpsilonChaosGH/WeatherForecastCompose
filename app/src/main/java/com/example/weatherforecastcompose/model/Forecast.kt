package com.example.weatherforecastcompose.model

data class Forecast(
    val temperature: String,
    val data: String,
    val humidity: String,
    val weatherType: WeatherType,
)