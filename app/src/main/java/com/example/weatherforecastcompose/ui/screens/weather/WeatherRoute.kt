package com.example.weatherforecastcompose.ui.screens.weather

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.runtime.getValue
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.weatherforecastcompose.R
import com.example.weatherforecastcompose.model.Air
import com.example.weatherforecastcompose.model.Coordinates
import com.example.weatherforecastcompose.model.Forecast
import com.example.weatherforecastcompose.model.Units
import com.example.weatherforecastcompose.model.Weather
import com.example.weatherforecastcompose.ui.theme.WeatherForecastComposeTheme


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

        when (val state = uiState) {
            is WeatherViewState.Loading -> {
                WeatherLoading(state)
            }

            is WeatherViewState.Display -> {
                WeatherScreen(
                    onFavoriteIconClick = { viewModel.obtainIntent(WeatherScreenIntent.ChangeFavorite) },
                    modifier = modifier,
                    uiState = state,
                )
            }
        }
    }
}