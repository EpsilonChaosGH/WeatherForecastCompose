package com.example.weatherforecastcompose.ui.screens.favorites

import com.example.weatherforecastcompose.model.FavoriteCoordinates
import com.example.weatherforecastcompose.model.Settings


sealed interface FavoritesScreenIntent {

    data class SettingsChanged(val value: Settings) : FavoritesScreenIntent

    data object RefreshScreenState : FavoritesScreenIntent

    data class AddToFavorites(val value: FavoriteCoordinates) : FavoritesScreenIntent

    data class RemoveFromFavorites(val value: FavoriteCoordinates) : FavoritesScreenIntent

}