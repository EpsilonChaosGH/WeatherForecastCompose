package com.example.weatherforecastcompose.ui.screens.weather

import com.example.weatherforecastcompose.model.City
import com.example.weatherforecastcompose.model.Coordinates
import com.example.weatherforecastcompose.model.Settings


sealed interface WeatherScreenIntent {

    data class SettingsChanged(val value: Settings) : WeatherScreenIntent

    data class LoadWeatherScreenData(val value: Settings) : WeatherScreenIntent

    data object SearchWeatherByCity : WeatherScreenIntent

    data class SearchWeatherByCoordinates(val coordinates: Coordinates) : WeatherScreenIntent

    data class SearchInputChanged(val value: String) : WeatherScreenIntent

    data object ChangeFavorite : WeatherScreenIntent

}