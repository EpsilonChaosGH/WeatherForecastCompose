package com.example.weatherforecastcompose.mappers

import com.example.weatherforecastcompose.data.FORMAT_EEE_d_MMMM_HH_mm
import com.example.weatherforecastcompose.data.format
import com.example.weatherforecastcompose.data.network.response.ForecastResponse
import com.example.weatherforecastcompose.model.Forecast
import com.example.weatherforecastcompose.model.WeatherType
import kotlin.math.roundToInt

internal fun ForecastResponse.toForecastList(): List<Forecast> {

     return list.map {
            Forecast(
                temperature = it.main.temp.roundToInt().toString(),
                data = it.dt.format(FORMAT_EEE_d_MMMM_HH_mm, city.timezone),
                humidity = it.main.humidity.toString(),
                weatherType = WeatherType.find("ic_${it.weather.firstOrNull()?.icon}")
            )
    }
}