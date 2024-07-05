package com.example.weatherforecastcompose.ui.screens.favorites

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.weatherforecastcompose.ui.navigation.TopLevelDestination

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