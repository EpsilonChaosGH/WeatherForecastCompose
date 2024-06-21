package com.example.weatherforecastcompose.data.local.room

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [FavoritesDbEntity::class], version = 1
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun favoritesDao(): FavoritesDao
}