package com.example.weatherforecastcompose.ui.screens.weather

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.weatherforecastcompose.ui.screens.weather.components.WeatherSearch


@Composable
internal fun WeatherRoute(
    modifier: Modifier,
    navController: NavController,
    viewModel: WeatherViewModel = hiltViewModel()
) {

    val uiState by viewModel.state.collectAsStateWithLifecycle()

    Column(modifier = modifier) {

        WeatherSearch(
            weatherViewState = uiState,
            onSearchInputChanged = { searchInputValue ->
                viewModel.obtainIntent(
                    WeatherScreenIntent.SearchInputChanged(
                        searchInputValue
                    )
                )
            },
            onSearchDoneClick = { viewModel.obtainIntent(WeatherScreenIntent.SearchWeatherByCity) },
            onLocationClick = { coordinates ->
                viewModel.obtainIntent(WeatherScreenIntent.SearchWeatherByCoordinates(coordinates))
            }
        )

        WeatherScreen(
            onFavoriteIconClick = { favoriteCoordinate, isFavorite ->
                if (isFavorite) viewModel.obtainIntent(WeatherScreenIntent.RemoveFromFavorites(favoriteCoordinate))
                else viewModel.obtainIntent(WeatherScreenIntent.AddToFavorites(favoriteCoordinate))
            },
            onRefresh = { viewModel.obtainIntent(WeatherScreenIntent.RefreshScreenState) },
            weatherViewState = uiState
        )
    }
}