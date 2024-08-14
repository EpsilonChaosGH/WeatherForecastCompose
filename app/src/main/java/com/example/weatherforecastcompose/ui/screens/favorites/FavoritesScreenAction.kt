package com.example.weatherforecastcompose.ui.screens.favorites

import com.example.weatherforecastcompose.model.Coordinates
import com.example.weatherforecastcompose.model.Settings


sealed interface FavoritesScreenAction {

    data class SettingsChanged(val settings: Settings) : FavoritesScreenAction

    data object RefreshScreenState : FavoritesScreenAction

    data class SetCoordinates(val coordinates: Coordinates) : FavoritesScreenAction

    data class RemoveFromFavorites(val id: Long) : FavoritesScreenAction
}