package com.example.weatherforecastcompose.mappers

import androidx.annotation.StringRes
import com.example.weatherforecastcompose.R
import com.example.weatherforecastcompose.model.ErrorType
import com.example.weatherforecastcompose.utils.ClientException
import com.example.weatherforecastcompose.utils.GenericException
import com.example.weatherforecastcompose.utils.ServerException
import retrofit2.Response
import java.io.IOException
import java.net.HttpURLConnection

fun <T, R> Response<T>.responseToDate(mapper: T.() -> R): R {
    try {
        return if (this.isSuccessful && this.body() != null) {
            this.body()!!.mapper()
        } else {
            val throwable = mapResponseCodeToThrowable(this.code())
            throw throwable
        }
    } catch (e: Throwable) {
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
    HttpURLConnection.HTTP_OK -> ClientException("Empty response body : $code: Check request parameters")
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