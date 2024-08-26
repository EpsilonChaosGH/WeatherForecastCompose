package com.example.weatherforecastcompose.data

import com.example.weatherforecastcompose.data.network.response.AirResponse
import com.example.weatherforecastcompose.data.network.response.ForecastResponse
import com.example.weatherforecastcompose.data.network.services.AirService
import com.example.weatherforecastcompose.data.network.services.ForecastService
import com.example.weatherforecastcompose.data.network.services.GeocodingService
import com.example.weatherforecastcompose.data.network.services.WeatherService
import com.example.weatherforecastcompose.mappers.mapThrowableToErrorType
import com.example.weatherforecastcompose.mappers.responseToDate
import com.example.weatherforecastcompose.mappers.toAir
import com.example.weatherforecastcompose.mappers.toForecastList
import com.example.weatherforecastcompose.mappers.toWeather
import com.example.weatherforecastcompose.model.AppResult
import com.example.weatherforecastcompose.model.City
import com.example.weatherforecastcompose.model.Coordinates
import com.example.weatherforecastcompose.model.CurrentWeather
import com.example.weatherforecastcompose.model.SupportedLanguage
import com.example.weatherforecastcompose.model.Units
import com.example.weatherforecastcompose.model.Weather
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.withContext
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
        language: SupportedLanguage,
        units: Units,
    ): AppResult<Weather> {
        return try {
            val weather = getWeatherByCoordinates(
                coordinates = coordinates,
                language = language,
                units = units
            )
            AppResult.Success(weather)
        } catch (e: Throwable) {
            AppResult.Error(mapThrowableToErrorType(e))
        }
    }

    suspend fun getCoordinatesByCity(city: City): AppResult<Coordinates?> {
        return try {
            val coordinates =
                geocodingService.getCoordinatesByCity(city = city.city).responseToDate {
                    val response = this.firstOrNull()
                    if (response != null) {
                        Coordinates(lat = response.lat.toString(), lon = response.lon.toString())
                    } else {
                        null
                    }
                }
            AppResult.Success(coordinates)
        } catch (e: Throwable) {
            AppResult.Error(mapThrowableToErrorType(e))
        }
    }

    suspend fun getFavoritesCurrentWeather(
        coordinatesList: List<Coordinates>,
        language: SupportedLanguage,
        units: Units,
    ): AppResult<List<CurrentWeather>> {
        return try {
            withContext(Dispatchers.IO) {
                val list = coordinatesList.map {
                    async {
                        weatherService.getCurrentWeatherByCoordinates(
                            lat = it.lat,
                            lon = it.lon,
                            units = units.unitsValue,
                            language = language.languageValue,
                        ).responseToDate { toWeather(units = units) }
                    }
                }
                AppResult.Success(list.awaitAll())
            }
        } catch (e: Throwable) {
            AppResult.Error(mapThrowableToErrorType(e))
        }
    }

    private suspend fun getWeatherByCoordinates(
        coordinates: Coordinates,
        language: SupportedLanguage,
        units: Units,
    ): Weather = withContext(Dispatchers.IO) {

        val currentWeatherDeferred = async {
            weatherService.getCurrentWeatherByCoordinates(
                lat = coordinates.lat,
                lon = coordinates.lon,
                units = units.unitsValue,
                language = language.languageValue,
            ).responseToDate {
                toWeather(units = units)
            }
        }

        val airDeferred = async {
            airService.getAirByCoordinate(
                lat = coordinates.lat,
                lon = coordinates.lon,
            ).responseToDate(mapper = AirResponse::toAir)
        }

        val forecastDeferred = async {
            forecastService.getForecastByCoordinate(
                lat = coordinates.lat,
                lon = coordinates.lon,
                units = units.unitsValue,
                language = language.languageValue,
            ).responseToDate(mapper = ForecastResponse::toForecastList)
        }

        Weather(
            currentWeather = currentWeatherDeferred.await(),
            air = airDeferred.await(),
            forecast = forecastDeferred.await(),
        )
    }
}