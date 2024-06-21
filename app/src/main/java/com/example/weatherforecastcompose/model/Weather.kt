package com.example.weatherforecastcompose.model

data class Weather(
    val currentWeather: CurrentWeather,
    val forecast: List<Forecast>,
    val air: Air,
)