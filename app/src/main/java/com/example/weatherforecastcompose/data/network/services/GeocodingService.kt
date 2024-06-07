package com.example.weatherforecastcompose.data.network.services

import com.example.weatherforecastcompose.data.network.response.GeocodingResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface GeocodingService {

    @GET("geo/1.0/direct?")
    suspend fun getCoordinatesByCity(
        @Query("q") city: String,
    ): Response<List<GeocodingResponse>>
}