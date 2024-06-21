package com.example.weatherforecastcompose.data.network.services

import com.example.weatherforecastcompose.data.network.response.ForecastResponse
import com.example.weatherforecastcompose.utils.AppConfig
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ForecastService {

    @GET("data/2.5/forecast?")
    suspend fun getForecastByCoordinate(
        @Query("lat") lat: String,
        @Query("lon") lon: String,
        @Query("lang") language: String,
        @Query("units") units: String,
        @Query("cnt") cnt: String = AppConfig.CNT
    ): Response<ForecastResponse>
}