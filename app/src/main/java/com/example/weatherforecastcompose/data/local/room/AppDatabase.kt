package com.example.weatherforecastcompose.data.local.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.weatherforecastcompose.data.local.util.InstantConverter

@Database(
    entities = [
        FavoritesDbEntity::class,
        RecentSearchQueryEntity::class
    ], version = 1
)
@TypeConverters(
    InstantConverter::class,
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun favoritesDao(): FavoritesDao
    abstract fun recentSearchQueryDao(): RecentSearchQueryDao
}