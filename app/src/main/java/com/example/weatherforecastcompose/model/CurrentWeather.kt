package com.example.weatherforecastcompose.model

data class CurrentWeather(
    val id: Long,
    val coordinates: Coordinates,
    val city: String,
    val country: String,
    val temperature: String,
    val icon: WeatherType,
    val description: String,
    val feelsLike: String,
    val humidity: String,
    val pressure: String,
    val windSpeed: String,
    val data: String,
    val timezone: Long,
)