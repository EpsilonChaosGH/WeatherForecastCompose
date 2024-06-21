package com.example.weatherforecastcompose.mappers

import com.example.weatherforecastcompose.data.local.room.FavoritesDbEntity
import com.example.weatherforecastcompose.model.Coordinates
import com.example.weatherforecastcompose.model.FavoritesCoordinates

fun FavoritesDbEntity.toFavoritesCoordinates(): FavoritesCoordinates {
    return FavoritesCoordinates(
        id = this.cityId,
        coordinates = Coordinates(
            lon = this.lon,
            lat = this.lat
        )
    )
}

fun FavoritesCoordinates.toFavoritesDbEntity(): FavoritesDbEntity {
    return FavoritesDbEntity(
        cityId = this.id,
        lon = this.coordinates.lon,
        lat = this.coordinates.lat,
    )
}