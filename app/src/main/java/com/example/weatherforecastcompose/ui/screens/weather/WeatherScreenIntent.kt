package com.example.weatherforecastcompose.ui.screens.weather

import com.example.weatherforecastcompose.model.Coordinates
import com.example.weatherforecastcompose.model.FavoritesCoordinates
import com.example.weatherforecastcompose.model.Settings


sealed interface WeatherScreenIntent {

    data class SettingsChanged(val value: Settings) : WeatherScreenIntent

    data object SearchWeatherByCity : WeatherScreenIntent

    data class SearchWeatherByCoordinates(val value: Coordinates) : WeatherScreenIntent

    data object PermissionsDenied : WeatherScreenIntent

    data class SearchInputChanged(val value: String) : WeatherScreenIntent

    data object RefreshScreenState : WeatherScreenIntent

    data class AddToFavorites(val value: FavoritesCoordinates) : WeatherScreenIntent

    data class RemoveFromFavorites(val value: FavoritesCoordinates) : WeatherScreenIntent
}