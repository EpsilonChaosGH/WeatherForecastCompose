package com.example.weatherforecastcompose.ui.screens.weather

import androidx.compose.foundation.layout.Box
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.weatherforecastcompose.designsystem.theme.AppTheme
import com.example.weatherforecastcompose.model.Air
import com.example.weatherforecastcompose.model.AirQuality
import com.example.weatherforecastcompose.model.Coordinates
import com.example.weatherforecastcompose.model.CurrentWeather
import com.example.weatherforecastcompose.model.Forecast
import com.example.weatherforecastcompose.model.Weather
import com.example.weatherforecastcompose.model.WeatherType
import com.example.weatherforecastcompose.ui.screens.weather.components.AirCard
import com.example.weatherforecastcompose.ui.screens.weather.components.ForecastCard
import com.example.weatherforecastcompose.ui.screens.weather.components.SecondWeatherCard
import com.example.weatherforecastcompose.ui.screens.weather.components.WeatherCard


@Composable
internal fun WeatherRoute(
    modifier: Modifier = Modifier,
    viewModel: WeatherViewModel = hiltViewModel()
) {

    val uiState by viewModel.state.collectAsStateWithLifecycle()

    WeatherScreen(
        uiState = uiState,
        onAction = viewModel::onAction,
        modifier = modifier,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun WeatherScreen(
    uiState: WeatherUiState,
    onAction: (WeatherScreenAction) -> Unit,
    modifier: Modifier = Modifier,
) {

    val pullToRefreshState = rememberPullToRefreshState()

    Box(
        modifier = modifier
            .nestedScroll(pullToRefreshState.nestedScrollConnection)
            .fillMaxSize()
    ) {

        if (uiState.errorMessageResId == null) {
            uiState.weather?.let {
                WeatherContent(
                    weather = uiState.weather,
                    isFavorite = uiState.isFavorite,
                    onAction = onAction
                )
            }
        } else {
            WeatherErrorScreen(uiState.errorMessageResId)
        }

        if (uiState.isLoading) {
            WeatherLoading()
        }

        if (pullToRefreshState.isRefreshing) {
            LaunchedEffect(true) {
                onAction(WeatherScreenAction.RefreshScreenState)
            }
        }

        LaunchedEffect(uiState.isRefreshing) {
            if (uiState.isRefreshing) {
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
internal fun WeatherContent(
    weather: Weather,
    isFavorite: Boolean,
    onAction: (WeatherScreenAction) -> Unit
) {
    val lazyListState: LazyListState = rememberLazyListState()

    LazyColumn(
        state = lazyListState,
    ) {
        item {
            WeatherCard(
                currentWeather = weather.currentWeather,
                isFavorite = isFavorite,
                onFavoriteIconClick = { favoritesCoordinates, isFavorites ->
                    if (isFavorites) {
                        onAction(WeatherScreenAction.RemoveFromFavorites(favoritesCoordinates.id))
                    } else {
                        onAction(WeatherScreenAction.AddToFavorites(favoritesCoordinates))
                    }
                }
            )
        }
        item {
            SecondWeatherCard(currentWeather = weather.currentWeather)
        }
        item {
            Text(
                text = "Air pollution:",
                fontSize = 24.sp,
                modifier = Modifier.padding(start = 22.dp, top = 22.dp)
            )
        }
        item {
            AirCard(air = weather.air)
        }
        item {
            Text(
                text = "Weather forecast:",
                fontSize = 24.sp,
                modifier = Modifier.padding(start = 22.dp, top = 22.dp)
            )
        }
        item {
            ForecastCard(forecastList = weather.forecast)
        }
    }
}

@Composable
internal fun WeatherLoading() {
    Box(modifier = Modifier.fillMaxSize()) {
        CircularProgressIndicator(
            modifier = Modifier.align(Alignment.Center),
            color = MaterialTheme.colorScheme.surfaceTint
        )
    }
}

@Composable
private fun WeatherErrorScreen(errorMessageResId: Int) {
    LazyColumn(modifier = Modifier.fillMaxSize()) {
        item {
            Box(modifier = Modifier.fillParentMaxSize()) {
                Box(modifier = Modifier.fillMaxSize()) {
                    Text(
                        text = stringResource(id = errorMessageResId),
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
internal fun WeatherScreenPreview() {
    AppTheme {
        WeatherScreen(
            uiState = WeatherUiState(
                weather = Weather(
                    currentWeather = CurrentWeather(
                        id = 0,
                        coordinates = Coordinates(
                            lon = "139.6917",
                            lat = "35.6895"
                        ),
                        city = "Moscow",
                        country = "RU",
                        temperature = "22°C",
                        icon = WeatherType.IC_UNKNOWN,
                        description = "cloudy",
                        feelsLike = "24°C",
                        humidity = "44%",
                        pressure = "1002hPa",
                        windSpeed = "7m/c",
                        data = "15.06.2024",
                        timezone = 0,
                    ),
                    forecast = listOf(
                        Forecast(
                            temperature = "22",
                            data = "Tue, 9 July 05:00",
                            humidity = "44",
                            weatherType = WeatherType.IC_02D
                        ),
                        Forecast(
                            temperature = "15",
                            data = "Tue, 9 July 08:00",
                            humidity = "33",
                            weatherType = WeatherType.IC_01N
                        ),
                        Forecast(
                            temperature = "-4",
                            data = "Tue, 9 July 11:00",
                            humidity = "77",
                            weatherType = WeatherType.IC_01D
                        )
                    ),
                    air = Air(
                        no2 = "0μg/m3",
                        no2Quality = AirQuality.GOOD,
                        o3 = "33μg/m3",
                        o3Quality = AirQuality.MODERATE,
                        pm10 = "155μg/m3",
                        pm10Quality = AirQuality.POOR,
                        pm25 = "",
                        pm25Quality = AirQuality.ERROR,
                    )
                ),
                isRefreshing = false,
                isLoading = false,

                isFavorite = true
            ),
            onAction = {}
        )
    }
}