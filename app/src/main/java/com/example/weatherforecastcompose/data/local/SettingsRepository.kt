package com.example.weatherforecastcompose.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.weatherforecastcompose.Const
import com.example.weatherforecastcompose.model.Coordinates
import com.example.weatherforecastcompose.model.FavoriteCoordinates
import com.example.weatherforecastcompose.model.SupportedLanguage
import com.example.weatherforecastcompose.model.Units
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SettingsRepository @Inject constructor(
    @ApplicationContext private val context: Context
) {

    private val prefLanguage by lazy { stringPreferencesKey(KEY_LANGUAGE) }
    private val prefUnits by lazy { stringPreferencesKey(KEY_UNITS) }
    private val prefCoordinates by lazy { stringPreferencesKey(KEY_COORDINATES) }


    suspend fun setLanguage(language: SupportedLanguage) {
        context.dataStoreLanguage.edit { it[prefLanguage] = language.name }
    }

    fun getLanguage(): Flow<SupportedLanguage> {
        return context.dataStoreLanguage.data.map {
            SupportedLanguage.valueOf(it[prefLanguage] ?: SupportedLanguage.ENGLISH.name)
        }
    }

    suspend fun setUnits(units: Units) {
        context.dataStoreUnits.edit { it[prefUnits] = units.name }
    }

    fun getUnits(): Flow<Units> {
        return context.dataStoreUnits.data.map {
            Units.valueOf(it[prefUnits] ?: Units.METRIC.name)
        }
    }

    suspend fun setCoordinates(coordinates: Coordinates) {
        context.dataStoreCoordinates.edit {
            it[prefCoordinates] = "${coordinates.lat}/${coordinates.lon}"
        }
    }

    fun getCoordinates(): Flow<Coordinates> {
        return context.dataStoreCoordinates.data.map {
            val latLngList = (it[prefCoordinates] ?: "$DEFAULT_LAT/$DEFAULT_LON").split("/")
            Coordinates(lat = latLngList[0], lon = latLngList[1])
        }
    }


    private val favoriteSet = MutableStateFlow(
        mutableSetOf(
            FavoriteCoordinates(
                1850144L,
                Coordinates(lon = Const.DEFAULT_LON, lat = Const.DEFAULT_LAT)
            )
        )
    )

    fun getFavoriteSet() = favoriteSet

    suspend fun addToFavorite(favoriteCoordinates: FavoriteCoordinates) {
        val set = favoriteSet.value.toMutableList()
        set.add(favoriteCoordinates)
        favoriteSet.emit(set.toMutableSet())
    }

    suspend fun removeFromFavorite(favoriteCoordinates: FavoriteCoordinates) {
        val set = favoriteSet.value.toMutableList()
        set.remove(favoriteCoordinates)
        favoriteSet.emit(set.toMutableSet())
    }

    fun checkToFavorite(id: Long): Boolean {
        favoriteSet.value.forEach {
            if (it.id == id) return true
        }
        return false
    }

    companion object {
        // Tokyo coordinates
        const val DEFAULT_LON = "139.6917"
        const val DEFAULT_LAT = "35.6895"

        const val KEY_LANGUAGE = "language"
        const val KEY_UNITS = "units"
        const val KEY_COORDINATES = "coordinates"
    }
}

val Context.dataStoreLanguage: DataStore<Preferences> by preferencesDataStore(name = "language")
val Context.dataStoreUnits: DataStore<Preferences> by preferencesDataStore(name = "units")
val Context.dataStoreCoordinates: DataStore<Preferences> by preferencesDataStore(name = "coordinates")