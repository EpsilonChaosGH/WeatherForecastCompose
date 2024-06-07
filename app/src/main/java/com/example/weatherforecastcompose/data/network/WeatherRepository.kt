package com.example.weatherforecastcompose.data.network

import com.example.weatherforecastcompose.mappers.mapThrowableToErrorType
import com.example.weatherforecastcompose.data.network.response.AirResponse
import com.example.weatherforecastcompose.data.network.response.CurrentWeatherResponse
import com.example.weatherforecastcompose.data.network.response.ForecastResponse
import com.example.weatherforecastcompose.data.network.services.AirService
import com.example.weatherforecastcompose.data.network.services.ForecastService
import com.example.weatherforecastcompose.data.network.services.GeocodingService
import com.example.weatherforecastcompose.data.network.services.WeatherService
import com.example.weatherforecastcompose.mappers.responseToDate
import com.example.weatherforecastcompose.mappers.toAir
import com.example.weatherforecastcompose.mappers.toWeather
import com.example.weatherforecastcompose.mappers.toForecastList
import com.example.weatherforecastcompose.model.City
import com.example.weatherforecastcompose.model.Coordinates
import com.example.weatherforecastcompose.model.Weather
import com.example.weatherforecastcompose.model.WeatherResult
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WeatherRepository @Inject constructor(
    private val geocodingService: GeocodingService,
    private val weatherService: WeatherService,
    private val forecastService: ForecastService,
    private val airService: AirService,
) {

    suspend fun getWeather(
        coordinates: Coordinates,
        language: String,
        units: String,
    ): WeatherResult<Weather> {
        return try {
            val weather = getWeatherByCoordinates(
                coordinates = coordinates,
                units = units,
                language = language
            )
            WeatherResult.Success(weather)
        } catch (e: Exception) {
            WeatherResult.Error(mapThrowableToErrorType(e))
        }
    }

    suspend fun getWeather(
        city: City,
        language: String,
        units: String,
    ): WeatherResult<Weather> {
        return try {
            val weather = getWeatherByCoordinates(
                coordinates = getCoordinatesByCity(city),
                units = units,
                language = language
            )
            WeatherResult.Success(weather)
        } catch (e: Exception) {
            WeatherResult.Error(mapThrowableToErrorType(e))
        }
    }

    private suspend fun getCoordinatesByCity(city: City): Coordinates {
        return try {
            geocodingService.getCoordinatesByCity(city.city).responseToDate {
                val response = this.firstOrNull()
                Coordinates(
                    lat = response?.lat.toString(),
                    lon = response?.lon.toString()
                )
            }
        } catch (e: Exception) {
            throw e
        }
    }

    private suspend fun getWeatherByCoordinates(
        coordinates: Coordinates,
        units: String,
        language: String,
    ): Weather {

        val currentWeather = weatherService.getCurrentWeatherByCoordinates(
            lat = coordinates.lat,
            lon = coordinates.lon,
            units = units,
            language = language,
        ).responseToDate(mapper = CurrentWeatherResponse::toWeather)

        val air = airService.getAirByCoordinate(
            lat = coordinates.lat,
            lon = coordinates.lon,
        ).responseToDate(mapper = AirResponse::toAir)

        val forecast = forecastService.getForecastByCoordinate(
            lat = coordinates.lat,
            lon = coordinates.lon,
            units = units,
            language = language,
        ).responseToDate(mapper = ForecastResponse::toForecastList)

        return Weather(
            currentWeather = currentWeather,
            air = air,
            forecast = forecast,
        )
    }
}