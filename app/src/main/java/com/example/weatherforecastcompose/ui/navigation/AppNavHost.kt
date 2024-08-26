package com.example.weatherforecastcompose.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.weatherforecastcompose.ui.AppState
import com.example.weatherforecastcompose.ui.screens.favorites.FavoritesRoute
import com.example.weatherforecastcompose.ui.screens.search.SEARCH_ROUTE
import com.example.weatherforecastcompose.ui.screens.search.SearchRoute
import com.example.weatherforecastcompose.ui.screens.settings.SettingsRoute
import com.example.weatherforecastcompose.ui.screens.weather.WeatherRoute

@Composable
fun AppNavHost(
    appState: AppState,
    startDestination: String = TopLevelDestination.WEATHER.name,
) {
    val navController = appState.navController

    NavHost(
        navController = navController,
        startDestination = startDestination,
    ) {
        composable(TopLevelDestination.WEATHER.name) {
            WeatherRoute()
        }

        composable(TopLevelDestination.FAVORITES.name) {
            FavoritesRoute(
                onFavoritesClick = { appState.navigateToTopLevelDestination(TopLevelDestination.WEATHER) }
            )
        }

        composable(TopLevelDestination.SETTINGS.name) {
            SettingsRoute()
        }

        composable(route = SEARCH_ROUTE) {
            SearchRoute(
                onBackClick = navController::popBackStack,
                onSearchClick = {
                    appState.navigateToTopLevelDestination(TopLevelDestination.WEATHER)
                }
            )
        }
    }
}