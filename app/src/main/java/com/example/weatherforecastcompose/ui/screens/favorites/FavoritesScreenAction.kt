package com.example.weatherforecastcompose.ui.screens.favorites

import com.example.weatherforecastcompose.model.Coordinates


sealed interface FavoritesScreenAction {

    data object RefreshScreenState : FavoritesScreenAction

    data class RemoveFromFavorites(val id: Long) : FavoritesScreenAction

    data class FavoritesItemClicked(val coordinates: Coordinates) : FavoritesScreenAction
}