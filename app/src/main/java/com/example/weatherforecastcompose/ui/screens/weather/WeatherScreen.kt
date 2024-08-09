package com.example.weatherforecastcompose.ui.screens.weather

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshContainer
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.weatherforecastcompose.designsystem.theme.AppTheme
import com.example.weatherforecastcompose.model.FavoritesCoordinates
import com.example.weatherforecastcompose.ui.screens.weather.components.AirCard
import com.example.weatherforecastcompose.ui.screens.weather.components.ForecastCard
import com.example.weatherforecastcompose.ui.screens.weather.components.SecondWeatherCard
import com.example.weatherforecastcompose.ui.screens.weather.components.WeatherCard
import com.example.weatherforecastcompose.ui.screens.weather.components.WeatherSearch


@Composable
internal fun WeatherRoute(
    onLocationClick: () -> Unit,
    modifier: Modifier,
    viewModel: WeatherViewModel = hiltViewModel()
) {

    val uiState by viewModel.state.collectAsStateWithLifecycle()

    WeatherScreen(
        weatherViewState = uiState,
        searchInput = uiState.searchInput,
        searchError = uiState.searchError,
        errorMessageResId = uiState.errorMessageResId,
        modifier = modifier,
        onSearchInputChanged = { searchInputValue ->
            viewModel.obtainIntent(WeatherScreenIntent.SearchInputChanged(searchInputValue))
        },
        onSearchDoneClick = { viewModel.obtainIntent(WeatherScreenIntent.SearchWeatherByCity) },
        onLocationClick = onLocationClick,
        onFavoriteIconClick = { favoriteCoordinate, isFavorite ->
            if (isFavorite) viewModel.obtainIntent(
                WeatherScreenIntent.RemoveFromFavorites(favoriteCoordinate)
            )
            else viewModel.obtainIntent(WeatherScreenIntent.AddToFavorites(favoriteCoordinate))
        },
        onRefresh = { viewModel.obtainIntent(WeatherScreenIntent.RefreshScreenState) }
    )

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun WeatherScreen(
    weatherViewState: WeatherViewState,
    searchInput: String,
    searchError: Boolean,
    errorMessageResId: Int?,
    modifier: Modifier = Modifier,
    onSearchInputChanged: (String) -> Unit,
    onSearchDoneClick: () -> Unit,
    onLocationClick: () -> Unit,
    onFavoriteIconClick: (favoriteCoordinate: FavoritesCoordinates, isFavorite: Boolean) -> Unit,
    onRefresh: () -> Unit,
    lazyListState: LazyListState = rememberLazyListState()
) {

    val pullToRefreshState = rememberPullToRefreshState()

    Column(modifier = modifier) {
        WeatherSearch(
            searchInput = searchInput,
            searchError = searchError,
            errorMessageResId = errorMessageResId,
            onSearchInputChanged = onSearchInputChanged,
            onSearchDoneClick = onSearchDoneClick,
            onLocationClick = onLocationClick,
        )
        Box(
            modifier = Modifier
                .nestedScroll(pullToRefreshState.nestedScrollConnection)
                .fillMaxSize()
        ) {
            if (weatherViewState.weatherUiState != null) {
                LazyColumn(
                    state = lazyListState,
                ) {
                    item {
                        WeatherCard(
                            currentWeather = weatherViewState.weatherUiState.weather.currentWeather,
                            weatherViewState.weatherUiState.isFavorite,
                            onFavoriteIconClick = onFavoriteIconClick
                        )
                    }
                    item {
                        SecondWeatherCard(currentWeather = weatherViewState.weatherUiState.weather.currentWeather)
                    }
                    item {
                        Text(
                            text = "Air pollution:",
                            fontSize = 24.sp,
                            modifier = Modifier.padding(start = 22.dp, top = 22.dp)
                        )
                    }
                    item {
                        AirCard(air = weatherViewState.weatherUiState.weather.air)
                    }
                    item {
                        Text(
                            text = "Weather forecast:",
                            fontSize = 24.sp,
                            modifier = Modifier.padding(start = 22.dp, top = 22.dp)
                        )
                    }
                    item {
                        ForecastCard(forecastList = weatherViewState.weatherUiState.weather.forecast)
                    }
                }
            }

            if (weatherViewState.errorMessageResId != null) {
//            ErrorScreen(state.errorMessageId, onTryAgainClicked)
            }

            if (weatherViewState.isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center),
                    color = MaterialTheme.colorScheme.surfaceTint
                )
            }

            if (pullToRefreshState.isRefreshing) {
                LaunchedEffect(true) {
                    onRefresh()
                    Log.e("aaaLK", "onRefresh()")
                }
            }

            LaunchedEffect(weatherViewState.isRefreshing) {
                if (weatherViewState.isRefreshing) {
                    pullToRefreshState.startRefresh()
                } else {
                    pullToRefreshState.endRefresh()
                }
            }

            if (pullToRefreshState.progress > 0 || pullToRefreshState.isRefreshing) {
                PullToRefreshContainer(
                    state = pullToRefreshState,
                    modifier = Modifier
                        .align(Alignment.TopCenter),
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
internal fun ForecastCardPreview() {
    AppTheme {
        WeatherScreen(
            weatherViewState = WeatherViewState(
                searchInput = "",
                searchError = false,
                isLoading = false,
                isRefreshing = false,
                errorMessageResId = null,
                weatherUiState = null
            ),
            searchInput = "",
            searchError = false,
            errorMessageResId = null,
            modifier = Modifier,
            onSearchInputChanged = {},
            onSearchDoneClick = {},
            onLocationClick = {},
            onFavoriteIconClick = { _, _ -> },
            onRefresh = {}
        )
    }
}