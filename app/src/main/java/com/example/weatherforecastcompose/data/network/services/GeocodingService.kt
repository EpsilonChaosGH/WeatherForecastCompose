package com.example.weatherforecastcompose.data.network.services

import com.example.weatherforecastcompose.data.network.response.GeocodingResponse
import com.example.weatherforecastcompose.utils.AppId
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface GeocodingService {

    @GET("geo/1.0/direct?")
    suspend fun getCoordinatesByCity(
        @Query("appId")appId: String = AppId.APP_ID,
        @Query("q") city: String,
    ): Response<List<GeocodingResponse>>
}