package com.example.weatherforecastcompose.mappers

import com.example.weatherforecastcompose.data.FORMAT_EEE_d_MMMM_HH_mm
import com.example.weatherforecastcompose.data.format
import com.example.weatherforecastcompose.data.network.response.CurrentWeatherResponse
import com.example.weatherforecastcompose.model.Coordinates
import com.example.weatherforecastcompose.model.CurrentWeather
import com.example.weatherforecastcompose.model.Units
import com.example.weatherforecastcompose.model.WeatherType
import kotlin.math.roundToInt

fun CurrentWeatherResponse.toWeather(units: Units): CurrentWeather = CurrentWeather(
    id = id,
    coordinates = Coordinates(
        lon = coord.lon.toString(),
        lat = coord.lat.toString()
    ),
    city = name,
    country = sys.country,
    temperature = main.temp.roundToInt().toString() + units.tempUnits,
    icon = WeatherType.find("ic_${weather.firstOrNull()?.icon}"),
    description = weather.firstOrNull()?.description ?: "unknown",
    feelsLike = main.feelsLike.roundToInt().toString() + units.tempUnits,
    humidity = main.humidity.toString()+ "%",
    pressure = main.pressure.toString() + "hPa",
    windSpeed = wind.speed.roundToInt().toString() + units.windUnits,
    data = dt.format(FORMAT_EEE_d_MMMM_HH_mm, timezone),
    timezone = timezone,
)