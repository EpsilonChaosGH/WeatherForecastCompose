package com.example.weatherforecastcompose.mappers

import androidx.annotation.StringRes
import com.example.weatherforecastcompose.R
import com.example.weatherforecastcompose.data.ClientException
import com.example.weatherforecastcompose.data.FORMAT_EEE_d_MMMM_HH_mm
import com.example.weatherforecastcompose.data.GenericException
import com.example.weatherforecastcompose.data.ServerException
import com.example.weatherforecastcompose.data.format
import com.example.weatherforecastcompose.data.network.response.CurrentWeatherResponse
import com.example.weatherforecastcompose.model.Coordinates
import com.example.weatherforecastcompose.model.ErrorType
import com.example.weatherforecastcompose.model.CurrentWeather
import com.example.weatherforecastcompose.model.WeatherType
import retrofit2.Response
import java.io.IOException
import java.net.HttpURLConnection


fun CurrentWeatherResponse.toWeather(): CurrentWeather = CurrentWeather(
    id = id,
    coordinates = Coordinates(
        lon = coord.lon.toString(),
        lat = coord.lat.toString()
    ),
    city = name,
    country = sys.country,
    temperature = main.temp.toString(),
    icon = WeatherType.find("ic_${weather.firstOrNull()?.icon}"),
    description = weather.firstOrNull()?.description ?: "unknown",
    feelsLike = main.feelsLike.toString(),
    humidity = main.humidity.toString(),
    pressure = main.pressure.toString(),
    windSpeed = wind.speed.toString(),
    data = dt.format(FORMAT_EEE_d_MMMM_HH_mm, timezone),
    timezone = timezone,
)

fun <T, R> Response<T>.responseToDate(mapper: T.() -> R): R {
    try {
        return if (this.isSuccessful && this.body() != null) {
            this.body()!!.mapper()
        } else {
            val throwable = mapResponseCodeToThrowable(this.code())
            throw throwable
        }
    } catch (e: Exception) {
        throw e
    }
}

@StringRes
fun ErrorType.toResourceId(): Int = when (this) {
    ErrorType.SERVER -> R.string.error_server
    ErrorType.GENERIC -> R.string.error_generic
    ErrorType.IO_CONNECTION -> R.string.error_connection
    ErrorType.CLIENT -> R.string.error_client
}

fun mapResponseCodeToThrowable(code: Int): Throwable = when (code) {
    HttpURLConnection.HTTP_BAD_REQUEST -> ClientException("Bad request : $code: Check request parameters")
    HttpURLConnection.HTTP_UNAUTHORIZED -> ClientException("Unauthorized access : $code: Check API Token")
    HttpURLConnection.HTTP_NOT_FOUND -> ClientException("Resource not found : $code: Check parameters")
    TOO_MANY_REQUESTS -> ClientException("Too many requests : $code: Rate limit exceeded")
    in CLIENT_ERRORS -> ClientException("Client error : $code")
    in SERVER_ERRORS -> ServerException("Server error : $code")
    else -> GenericException("Generic error : $code")
}

fun mapThrowableToErrorType(throwable: Throwable): ErrorType {
    val errorType = when (throwable) {
        is IOException -> ErrorType.IO_CONNECTION
        is ClientException -> ErrorType.CLIENT
        is ServerException -> ErrorType.SERVER
        else -> ErrorType.GENERIC
    }
    return errorType
}

private val SERVER_ERRORS = 500..600
private val CLIENT_ERRORS = 400..499
private const val TOO_MANY_REQUESTS = 429