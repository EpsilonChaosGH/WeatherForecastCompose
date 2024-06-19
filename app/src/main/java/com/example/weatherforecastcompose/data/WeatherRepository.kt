package com.example.weatherforecastcompose.data

import android.util.Log
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
import com.example.weatherforecastcompose.model.City
import com.example.weatherforecastcompose.model.Coordinates
import com.example.weatherforecastcompose.model.CurrentWeather
import com.example.weatherforecastcompose.model.SupportedLanguage
import com.example.weatherforecastcompose.model.Units
import com.example.weatherforecastcompose.model.Weather
import com.example.weatherforecastcompose.model.WeatherResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.delay
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
    ): WeatherResult<Weather> {
        return try {
            val weather = getWeatherByCoordinates(
                coordinates = coordinates,
                units = units,
                language = language
            )
            WeatherResult.Success(weather)
        } catch (e: Throwable) {
            WeatherResult.Error(mapThrowableToErrorType(e))
        }
    }

//    suspend fun getWeather(
//        city: City,
//        units: Units,
//        language: SupportedLanguage,
//    ): WeatherResult<Weather> {
//        return try {
//            val weather = getWeatherByCoordinates(
//                coordinates = getCoordinatesByCity(city),
//                units = units,
//                language = language
//            )
//            WeatherResult.Success(weather)
//        } catch (e: Throwable) {
//            WeatherResult.Error(mapThrowableToErrorType(e))
//        }
//    }

    suspend fun getCoordinatesByCity(city: City): WeatherResult<Coordinates> {
        return try {
            val coordinates = geocodingService.getCoordinatesByCity(city.city).responseToDate {
                val response = this.firstOrNull()
                if (response != null) {
                    Coordinates(lat = response.lat.toString(), lon = response.lon.toString())
                } else {
                    throw WrongCityException("City not found")
                }
            }
            WeatherResult.Success(coordinates)
        } catch (e: Throwable) {
            WeatherResult.Error(mapThrowableToErrorType(e))
        }
    }

    suspend fun getFavoritesCurrentWeather(
        coordinatesSet: Set<Coordinates>,
        language: SupportedLanguage,
        units: Units,
    ): WeatherResult<List<CurrentWeather>> {
        return try {
            withContext(Dispatchers.IO) {
                val list = coordinatesSet.map {
                    async {
                        weatherService.getCurrentWeatherByCoordinates(
                            lat = it.lat,
                            lon = it.lon,
                            units = units.unitsValue,
                            language = language.languageValue,
                        ).responseToDate { toWeather(units = units) }
                    }
                }
                WeatherResult.Success(list.awaitAll())
            }
        } catch (e: Throwable) {
            WeatherResult.Error(mapThrowableToErrorType(e))
        }
    }

    private suspend fun getWeatherByCoordinates(
        coordinates: Coordinates,
        units: Units,
        language: SupportedLanguage,
    ): Weather {

        Log.e("aaaTESTER", coordinates.lat)
        delay(300)

        val currentWeather = weatherService.getCurrentWeatherByCoordinates(
            lat = coordinates.lat,
            lon = coordinates.lon,
            units = units.unitsValue,
            language = language.languageValue,
        ).responseToDate {
            toWeather(units = units)
        }

        val air = airService.getAirByCoordinate(
            lat = coordinates.lat,
            lon = coordinates.lon,
        ).responseToDate(mapper = AirResponse::toAir)

        val forecast = forecastService.getForecastByCoordinate(
            lat = coordinates.lat,
            lon = coordinates.lon,
            units = units.unitsValue,
            language = language.languageValue,
        ).responseToDate(mapper = ForecastResponse::toForecastList)

        return Weather(
            currentWeather = currentWeather,
            air = air,
            forecast = forecast,
        )
    }
}