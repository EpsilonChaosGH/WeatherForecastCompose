package com.example.weatherforecastcompose.data.local

import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.weatherforecastcompose.Const
import com.example.weatherforecastcompose.model.Coordinates
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
        set(key = prefLanguage, value = language.name)
    }

     fun getLanguage(): Flow<SupportedLanguage> {
        return get(key = prefLanguage, default = SupportedLanguage.ENGLISH.name).map {
            SupportedLanguage.valueOf(it)
        }
    }

     suspend fun setUnits(units: Units) {
        set(key = prefUnits, value = units.name)
    }

    fun getUnits(): Flow<Units> {
        return get(key = prefUnits, default = Units.METRIC.name).map {
            Units.valueOf(it)
        }
    }

     suspend fun setCoordinates(coordinates: Coordinates) {
         Log.e("aaaSET","SET")
        set(key = prefCoordinates, value = "${coordinates.lat}/${coordinates.lon}")
    }

    fun getCoordinates(): Flow<Coordinates> {
        return get(
            key = prefCoordinates,
            default = "$DEFAULT_LAT/$DEFAULT_LON"
        ).map { coordinates ->
            Log.e("aaagetCoordinates","GET")
            val latLngList = coordinates.split("/")
            Coordinates(lat = latLngList[0], lon = latLngList[1])
        }
    }


    private val favoriteSet = MutableStateFlow(mutableSetOf(1850144L))

//    private val currentLocation = MutableStateFlow(Coordinates(lon = Const.DEFAULT_LON, lat = Const.DEFAULT_LAT))

//    fun getLanguage() = MutableStateFlow(SupportedLanguage.RUSSIAN)

//    fun getUnits() = MutableStateFlow(Units.IMPERIAL)

//    fun getCurrentLocation() = currentLocation

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

//    suspend fun setNewLocation(coordinates: Coordinates){
//        currentLocation.value = coordinates
//    }

    fun checkToFavorite(id: Long): Boolean {
        return favoriteSet.value.contains(id)
    }



    private suspend fun <T> set(key: Preferences.Key<T>, value: T) {
        context.dataStore.edit { settings ->
            settings[key] = value
        }
    }

    private fun <T> get(key: Preferences.Key<T>, default: T): Flow<T> {
        return context.dataStore.data.map { settings ->
            settings[key] ?: default
        }
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

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")