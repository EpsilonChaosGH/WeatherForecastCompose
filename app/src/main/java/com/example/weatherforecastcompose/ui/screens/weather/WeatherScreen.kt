package com.example.weatherforecastcompose.ui.screens.weather

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.sp
import com.example.weatherforecastcompose.ui.screens.weather.components.AirCard
import com.example.weatherforecastcompose.ui.screens.weather.components.ForecastCard
import com.example.weatherforecastcompose.ui.screens.weather.components.SecondWeatherCard
import com.example.weatherforecastcompose.ui.screens.weather.components.WeatherCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun WeatherScreen(
    onFavoriteIconClick: () -> Unit,
    onRefresh: () -> Unit,
    weatherViewState: WeatherViewState,
    modifier: Modifier = Modifier,
    lazyListState: LazyListState = rememberLazyListState()
) {

    val pullToRefreshState = rememberPullToRefreshState()
    Box(modifier = modifier
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
                    AirCard(air = weatherViewState.weatherUiState.weather.air)
                }
                item {
                    ForecastCard(forecastList = weatherViewState.weatherUiState.weather.forecast)
                }
            }
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

@Composable
internal fun WeatherLoading(
    state: WeatherViewState
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        if (state.isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center),
                color = MaterialTheme.colorScheme.surfaceTint
            )
        }
        state.errorMessageId?.let {
            Text(
                modifier = Modifier
                    .align(Alignment.BottomCenter),
                fontSize = 26.sp,
                text = stringResource(id = it)
            )
        }
    }
}