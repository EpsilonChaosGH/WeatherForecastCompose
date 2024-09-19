package com.example.weatherforecastcompose.data.network.response

import kotlinx.serialization.Serializable

@Serializable
data class GeocodingResponse(
    val lat: Double,
    val lon: Double,
)