package com.example.weatherforecastcompose.ui.screens.weather

import com.example.weatherforecastcompose.model.City
import com.example.weatherforecastcompose.model.Coordinates


sealed interface WeatherScreenIntent {

    data object LoadWeatherScreenData : WeatherScreenIntent

    data object SearchWeatherByCity : WeatherScreenIntent

    data class SearchWeatherByCoordinates(val coordinates: Coordinates) : WeatherScreenIntent

    data class SearchInputChanged(val value: String) : WeatherScreenIntent

    data object ChangeFavorite : WeatherScreenIntent

}