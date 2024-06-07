package com.example.weatherforecastcompose.data.network.response

data class AirResponse(
    val list: List<ListElement>
) {
    data class ListElement(
        val components: Map<String, Double>,
    )
}