package com.example.weatherforecastcompose.data.network.services

import com.example.weatherforecastcompose.data.network.response.AirResponse
import com.example.weatherforecastcompose.utils.AppId
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface AirService {

    @GET("data/2.5/air_pollution?")
    suspend fun getAirByCoordinate(
        @Query("appId")appId: String = AppId.APP_ID,
        @Query("lat") lat: String,
        @Query("lon") lon: String,
    ): Response<AirResponse>
}