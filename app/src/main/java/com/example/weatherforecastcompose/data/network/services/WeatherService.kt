package com.example.weatherforecastcompose.data.network.services

import com.example.weatherforecastcompose.data.network.response.CurrentWeatherResponse
import com.example.weatherforecastcompose.utils.AppId
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherService {

    @GET("data/2.5/weather?")
    suspend fun getCurrentWeatherByCoordinates(
        @Query("appId")appId: String = AppId.APP_ID,
        @Query("lat") lat: String,
        @Query("lon") lon: String,
        @Query("lang") language: String,
        @Query("units") units: String
    ): Response<CurrentWeatherResponse>
}