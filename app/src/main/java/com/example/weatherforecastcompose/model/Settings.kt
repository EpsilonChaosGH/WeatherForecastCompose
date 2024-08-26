package com.example.weatherforecastcompose.model

data class Settings(
    val language: SupportedLanguage,
    val units: Units,
    val coordinates: Coordinates,
    val favoriteSet: List<FavoritesCoordinates>
)