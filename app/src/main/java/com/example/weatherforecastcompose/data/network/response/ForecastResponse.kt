package com.example.weatherforecastcompose.data.network.response


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ForecastResponse(
    val list: List<ListElement>,
    val city: City
) {

    @Serializable
    data class City(
        val id: Long,
        val name: String,
        val coord: Coord,
        val country: String,
        val timezone: Long,
    )

    @Serializable
    data class Coord(
        val lat: Double,
        val lon: Double
    )

    @Serializable
    data class ListElement(
        val dt: Long,
        val main: MainClass,
        val weather: List<Weather>,
        val wind: Wind,
    )

    @Serializable
    data class MainClass(
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