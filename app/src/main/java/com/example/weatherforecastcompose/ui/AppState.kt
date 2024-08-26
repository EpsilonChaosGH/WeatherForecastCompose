package com.example.weatherforecastcompose.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.NavDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.weatherforecastcompose.data.network.NetworkMonitor
import com.example.weatherforecastcompose.ui.navigation.TopLevelDestination
import com.example.weatherforecastcompose.ui.navigation.TopLevelDestination.FAVORITES
import com.example.weatherforecastcompose.ui.navigation.TopLevelDestination.SETTINGS
import com.example.weatherforecastcompose.ui.navigation.TopLevelDestination.WEATHER
import com.example.weatherforecastcompose.ui.screens.search.SEARCH_ROUTE
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

@Composable
fun rememberAppState(
    networkMonitor: NetworkMonitor,
    navController: NavHostController = rememberNavController(),
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
): AppState {
    return remember(
        networkMonitor,
        navController,
        coroutineScope,
    ) {
        AppState(
            networkMonitor = networkMonitor,
            navController = navController,
            coroutineScope = coroutineScope,
        )
    }
}

@Stable
class AppState(
    networkMonitor: NetworkMonitor,
    val navController: NavHostController,
    coroutineScope: CoroutineScope
) {
    val currentDestination: NavDestination?
        @Composable get() = navController.currentBackStackEntryAsState().value?.destination

    val currentTopLevelDestination: TopLevelDestination?
        @Composable get() = when (currentDestination?.route) {
            WEATHER.name -> WEATHER
            FAVORITES.name -> FAVORITES
            SETTINGS.name -> SETTINGS
            else -> null
        }

    val isOffline = networkMonitor.isOnline
        .map(Boolean::not)
        .stateIn(
            scope = coroutineScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = false,
        )

    val topLevelDestinations: List<TopLevelDestination> = TopLevelDestination.entries

    fun navigateToTopLevelDestination(topLevelDestination: TopLevelDestination) {
        val route = when (topLevelDestination) {
            FAVORITES -> FAVORITES.name
            SETTINGS -> SETTINGS.name
            WEATHER -> WEATHER.name
        }
        navController.navigate(route = route) { popUpTo(0) { inclusive = true } }
    }

    fun navigateToSearch() = navController.navigate(SEARCH_ROUTE)
}