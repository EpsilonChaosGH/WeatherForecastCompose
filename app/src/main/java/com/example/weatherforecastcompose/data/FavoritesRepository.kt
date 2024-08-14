package com.example.weatherforecastcompose.data

import com.example.weatherforecastcompose.data.local.room.FavoritesDao
import com.example.weatherforecastcompose.mappers.toFavoritesCoordinates
import com.example.weatherforecastcompose.mappers.toFavoritesDbEntity
import com.example.weatherforecastcompose.model.FavoritesCoordinates
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FavoritesRepository @Inject constructor(
    private val favoritesDao: FavoritesDao
) {

    fun getFavoritesListFlow(): Flow<List<FavoritesCoordinates>> {
        return favoritesDao.getFavoritesFlow().map {
            it.map { favoritesDbEntity -> favoritesDbEntity.toFavoritesCoordinates() }
        }
    }

    suspend fun addToFavorites(favoritesCoordinates: FavoritesCoordinates) =
        withContext(Dispatchers.IO) {
            favoritesDao.addToFavorites(favoritesCoordinates.toFavoritesDbEntity())
        }

    suspend fun removeFromFavorites(id: Long) =
        withContext(Dispatchers.IO) {
            favoritesDao.deleteFromFavorites(id)
        }

    suspend fun checkForFavorite(id: Long): Boolean = withContext(Dispatchers.IO) {
        favoritesDao.checkForFavorites(id)
    }
}