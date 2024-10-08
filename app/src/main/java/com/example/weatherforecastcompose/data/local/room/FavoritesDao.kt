package com.example.weatherforecastcompose.data.local.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoritesDao {

    @Query("SELECT * FROM favorites")
    fun getFavoritesFlow(): Flow<List<FavoritesDbEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addToFavorites(favoritesDbEntity: FavoritesDbEntity)

    @Query("DELETE FROM favorites WHERE city_id = :cityId")
    suspend fun deleteFromFavorites(cityId: Long)

    @Query("SELECT EXISTS(SELECT city_id FROM favorites WHERE city_id =:id)")
    fun checkForFavorites(id: Long): Boolean
}