package com.example.weatherforecastcompose.ui.screens.weather

import com.example.weatherforecastcompose.model.FavoritesCoordinates


sealed interface WeatherScreenAction {

    data object RefreshScreenState : WeatherScreenAction

    data class AddToFavorites(val favoritesCoordinates: FavoritesCoordinates) : WeatherScreenAction

    data class RemoveFromFavorites(val id: Long) : WeatherScreenAction
}