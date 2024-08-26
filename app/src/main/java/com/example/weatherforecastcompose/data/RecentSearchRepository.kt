package com.example.weatherforecastcompose.data

import com.example.weatherforecastcompose.data.local.room.RecentSearchQueryDao
import com.example.weatherforecastcompose.data.local.room.RecentSearchQueryEntity
import com.example.weatherforecastcompose.model.RecentSearchQuery
import com.example.weatherforecastcompose.model.asExternalModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.datetime.Clock
import javax.inject.Inject

class RecentSearchRepository @Inject constructor(
    private val recentSearchQueryDao: RecentSearchQueryDao,
) {
    suspend fun insertOrReplaceRecentSearch(searchQuery: String) {
        recentSearchQueryDao.insertOrReplaceRecentSearchQuery(
            RecentSearchQueryEntity(
                query = searchQuery,
                queriedDate = Clock.System.now(),
            ),
        )
    }

    fun getRecentSearchQueries(limit: Int): Flow<List<RecentSearchQuery>> {
        return recentSearchQueryDao.getRecentSearchQueryEntities(limit).map { searchQueries ->
            searchQueries.map { it.asExternalModel() }
        }
    }

    suspend fun clearRecentSearches() = recentSearchQueryDao.clearRecentSearchQueries()
}