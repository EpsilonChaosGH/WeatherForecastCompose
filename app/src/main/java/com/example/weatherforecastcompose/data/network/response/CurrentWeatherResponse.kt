package com.example.weatherforecastcompose.data.network.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CurrentWeatherResponse(
    val id: Long,
    val coord: Coord,
    val weather: List<Weather>,
    val main: Main,
    val wind: Wind,
    val dt: Long,
    val sys: Sys,
    val timezone: Long,
    val name: String,
) {

    @Serializable
    data class Coord(
        val lon: Double,
        val lat: Double
    )

    @Serializable
    data class Main(
        @SerialName("temp")
        val temp: Double,

        @SerialName("feels_like")
        val feelsLike: Double,

        @SerialName("pressure")
        val pressure: Long,

        @SerialName("humidity")
        val humidity: Long,
    )

    @Serializable
    data class Sys(
        val country: String = "",
    )

    @Serializable
    data class Weather(
        val main: String,
        val description: String,
        val icon: String
    )

    @Serializable
    data class Wind(
        val speed: Double,
    )
}