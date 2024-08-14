package com.example.weatherforecastcompose.ui.screens.weather

import com.example.weatherforecastcompose.model.Coordinates
import com.example.weatherforecastcompose.model.FavoritesCoordinates
import com.example.weatherforecastcompose.model.Settings


sealed interface WeatherScreenAction {

    data class SettingsChanged(val settings: Settings) : WeatherScreenAction

//    data object SearchWeatherByCity : WeatherScreenAction

    data object SearchWeatherByCoordinates : WeatherScreenAction

    data object PermissionsDenied : WeatherScreenAction

//    data class SearchInputChanged(val value: String) : WeatherScreenAction

    data object RefreshScreenState : WeatherScreenAction

    data class AddToFavorites(val favoritesCoordinates: FavoritesCoordinates) : WeatherScreenAction

    data class RemoveFromFavorites(val id: Long) : WeatherScreenAction
}