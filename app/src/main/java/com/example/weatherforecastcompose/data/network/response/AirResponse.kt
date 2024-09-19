package com.example.weatherforecastcompose.data.network.response

import kotlinx.serialization.Serializable

@Serializable
data class AirResponse(
    val list: List<ListElement>
) {
    @Serializable
    data class ListElement(
        val components: Map<String, Double>,
    )
}