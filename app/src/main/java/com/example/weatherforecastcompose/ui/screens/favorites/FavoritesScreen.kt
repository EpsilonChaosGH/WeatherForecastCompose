package com.example.weatherforecastcompose.ui.screens.favorites

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.weatherforecastcompose.designsystem.components.AppBackground
import com.example.weatherforecastcompose.designsystem.theme.AppTheme
import com.example.weatherforecastcompose.model.Coordinates
import com.example.weatherforecastcompose.model.CurrentWeather
import com.example.weatherforecastcompose.model.WeatherType
import com.example.weatherforecastcompose.ui.DevicePreviews
import com.example.weatherforecastcompose.ui.screens.favorites.components.FavoriteItemCard
import com.example.weatherforecastcompose.ui.screens.favorites.components.SwipeToDeleteContainer


@Composable
fun FavoritesRoute(
    onFavoritesClick: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: FavoritesViewModel = hiltViewModel()
) {

    val uiState by viewModel.state.collectAsStateWithLifecycle()

    FavoritesScreen(
        uiState = uiState,
        onAction = { action: FavoritesScreenAction ->
            when (action) {
                is FavoritesScreenAction.FavoritesItemClicked -> {
                    viewModel.onAction(action)
                    onFavoritesClick()
                }

                else -> viewModel.onAction(action)
            }
        },
        modifier = modifier
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun FavoritesScreen(
    uiState: FavoritesUiState,
    onAction: (FavoritesScreenAction) -> Unit,
    modifier: Modifier = Modifier
) {

    val pullToRefreshState = rememberPullToRefreshState()
    Box(
        modifier = modifier
            .nestedScroll(pullToRefreshState.nestedScrollConnection)
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {

        if (uiState.errorMessageResId == null) {
            FavoritesContent(favoritesItems = uiState.favoritesItems, onAction = onAction)
        } else {
            FavoritesError(uiState.errorMessageResId)
        }

        if (uiState.isLoading) {
            FavoritesLoading()
        }

        if (pullToRefreshState.isRefreshing) {
            LaunchedEffect(true) {
                onAction(FavoritesScreenAction.RefreshScreenState)
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
internal fun FavoritesLoading() {
    Box(modifier = Modifier.fillMaxSize()) {
        CircularProgressIndicator(
            modifier = Modifier.align(Alignment.Center),
            color = MaterialTheme.colorScheme.surfaceTint
        )
    }
}

@Composable
private fun FavoritesError(errorMessageResId: Int) {
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

@Composable
internal fun FavoritesContent(
    favoritesItems: List<CurrentWeather>,
    onAction: (FavoritesScreenAction) -> Unit,
    lazyListState: LazyListState = rememberLazyListState()
) {
    LazyColumn(
        state = lazyListState,
        modifier = Modifier
            .fillMaxSize()
            .padding(top = AppTheme.dimens.extraSmall)
    ) {
        items(
            items = favoritesItems,
            key = { it.id },
        ) { favoritesUiState ->

            SwipeToDeleteContainer(
                item = favoritesUiState,
                onDelete = {
                    onAction(FavoritesScreenAction.RemoveFromFavorites(favoritesUiState.id))
                }) { data: CurrentWeather ->
                FavoriteItemCard(currentWeather = data, onCardClick = {
                    onAction(FavoritesScreenAction.FavoritesItemClicked(favoritesUiState.coordinates))
                })
            }
        }
    }
}

@Composable
@DevicePreviews
fun FavoritesSuccessPreview() {
    AppTheme {
        AppBackground {
            FavoritesScreen(
                uiState = FavoritesUiState(
                    favoritesItems =
                    listOf(
                        CurrentWeather(
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
                        )
                    )
                ),
                onAction = {}
            )
        }
    }
}