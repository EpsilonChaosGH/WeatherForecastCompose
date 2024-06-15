package com.example.weatherforecastcompose.data.local

import com.example.weatherforecastcompose.Const
import com.example.weatherforecastcompose.model.Coordinates
import com.example.weatherforecastcompose.model.SupportedLanguage
import com.example.weatherforecastcompose.model.Units
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SettingsRepository @Inject constructor() {

    private val favoriteSet = MutableStateFlow(mutableSetOf(1850144L))

    private val currentLocation = MutableStateFlow(Coordinates(lon = Const.DEFAULT_LON, lat = Const.DEFAULT_LAT))

    fun getLanguage() = MutableStateFlow(SupportedLanguage.RUSSIAN)

    fun getUnits() = MutableStateFlow(Units.IMPERIAL)

    fun getCurrentLocation() = currentLocation

    fun getFavoriteSet() = favoriteSet


    suspend fun addToFavorite(id: Long) {
        val set = favoriteSet.value.toMutableList()
        set.add(id)
        favoriteSet.emit(set.toMutableSet())
    }

    suspend fun removeFromFavorite(id: Long) {
        val set = favoriteSet.value.toMutableList()
        set.remove(id)
        favoriteSet.emit(set.toMutableSet())
    }

    suspend fun setNewLocation(coordinates: Coordinates){
        currentLocation.value = coordinates
    }

    fun checkToFavorite(id: Long): Boolean {
        return favoriteSet.value.contains(id)
    }
}