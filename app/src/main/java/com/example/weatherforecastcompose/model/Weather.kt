package com.example.weatherforecastcompose.model

import com.example.weatherforecastcompose.Const

data class Weather(
    val currentWeather: CurrentWeather,
    val forecast: List<Forecast>,
    val air: Air,
) {
    companion object {
        fun defaultEmptyWeather() = Weather(
            currentWeather = CurrentWeather(
                id = 0,
                coordinates = Coordinates(
                    lon = Const.DEFAULT_LON,
                    lat = Const.DEFAULT_LAT
                ),
                city = "",
                country = "",
                temperature = "",
                icon = WeatherType.IC_UNKNOWN,
                description = "",
                feelsLike = "",
                humidity = "",
                pressure = "",
                windSpeed = "",
                data = "",
                timezone = 0,
            ),
            forecast = emptyList(),
            air = Air(
                no2 = "",
                no2Quality = AirQuality.ERROR,
                o3 = "",
                o3Quality = AirQuality.ERROR,
                pm10 = "",
                pm10Quality = AirQuality.ERROR,
                pm25 = "",
                pm25Quality = AirQuality.ERROR,
            )
        )

    }
}