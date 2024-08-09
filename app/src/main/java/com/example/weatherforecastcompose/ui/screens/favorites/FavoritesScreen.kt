package com.example.weatherforecastcompose.ui.screens.favorites

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.navigation.NavController
import com.example.weatherforecastcompose.R
import com.example.weatherforecastcompose.model.Coordinates
import com.example.weatherforecastcompose.model.FavoritesCoordinates
import com.example.weatherforecastcompose.ui.navigation.TopLevelDestination
import com.example.weatherforecastcompose.ui.screens.favorites.components.FavoriteItemCard
import com.example.weatherforecastcompose.ui.screens.favorites.components.SwipeToDeleteContainer


@Composable
fun FavoritesRoute(
    modifier: Modifier,
    navController: NavController,
    viewModel: FavoritesViewModel = hiltViewModel()
) {

    val uiState by viewModel.state.collectAsStateWithLifecycle()

    FavoritesScreen(
        favoritesViewState = uiState,
        onItemClick = {
            viewModel.obtainIntent(FavoritesScreenIntent.SetCoordinates(it))
            navController.navigate(TopLevelDestination.Weather.name) {
                popUpTo(0) { inclusive = true }
            }
        },
        onFavoriteIconClick = { favoritesCoordinates, isFavorite ->
            if (isFavorite) viewModel.obtainIntent(
                FavoritesScreenIntent.RemoveFromFavorites(
                    favoritesCoordinates
                )
            )
            else viewModel.obtainIntent(FavoritesScreenIntent.AddToFavorites(favoritesCoordinates))
        },
        onRefresh = { viewModel.obtainIntent(FavoritesScreenIntent.RefreshScreenState) },
        modifier = modifier
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun FavoritesScreen(
    favoritesViewState: FavoritesViewState,
    onItemClick: (coordinates: Coordinates) -> Unit,
    onFavoriteIconClick: (favoritesCoordinates: FavoritesCoordinates, isFavorite: Boolean) -> Unit,
    onRefresh: () -> Unit,
    modifier: Modifier = Modifier,
    lazyListState: LazyListState = rememberLazyListState()
) {

    val pullToRefreshState = rememberPullToRefreshState()
    Box(
        modifier = modifier
            .nestedScroll(pullToRefreshState.nestedScrollConnection)
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        if (favoritesViewState.favoritesUiState.isNotEmpty()) {
            LazyColumn(
                state = lazyListState,
                modifier = Modifier.fillMaxSize()
            ) {
                items(
                    items = favoritesViewState.favoritesUiState,
                    key = { it.currentWeather.id },
                ) { favoritesUiState ->

                    SwipeToDeleteContainer(
                        item = favoritesUiState,
                        onDelete = {
                            onFavoriteIconClick(
                                FavoritesCoordinates(
                                    id = favoritesUiState.currentWeather.id,
                                    coordinates = favoritesUiState.currentWeather.coordinates,
                                ), favoritesUiState.isFavorite
                            )
                        }) {
                        FavoriteItemCard(currentWeather = it.currentWeather, onCardClick = {
                            onItemClick(favoritesUiState.currentWeather.coordinates)
                        })
                    }
                }
            }
        }

        if (favoritesViewState.favoritesUiState.isEmpty() && !favoritesViewState.isLoading){
            Text(text = stringResource(id = R.string.error_empty_favorites))
        }

        if (favoritesViewState.isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center),
                color = MaterialTheme.colorScheme.surfaceTint
            )
        }

        if (pullToRefreshState.isRefreshing) {
            LaunchedEffect(true) {
                onRefresh()
            }
        }

        LaunchedEffect(favoritesViewState.isRefreshing) {
            if (favoritesViewState.isRefreshing) {
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