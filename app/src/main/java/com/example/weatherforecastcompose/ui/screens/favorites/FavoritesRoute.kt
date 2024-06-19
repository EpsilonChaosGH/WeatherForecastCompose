package com.example.weatherforecastcompose.ui.screens.favorites

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController

@Composable
fun FavoritesRoute(
    modifier: Modifier,
    navController: NavController,
    viewModel: FavoritesViewModel = hiltViewModel()
) {

    val uiState by viewModel.state.collectAsStateWithLifecycle()

    FavoritesScreen(
        favoritesViewState = uiState,
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