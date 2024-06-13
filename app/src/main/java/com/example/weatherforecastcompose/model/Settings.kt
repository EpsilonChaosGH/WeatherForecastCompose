package com.example.weatherforecastcompose.model

data class Settings(
    val units: Units,
    val language: SupportedLanguage,
    val defaultLocation: Coordinates,
    val favoriteSet: Set<Long>
)
