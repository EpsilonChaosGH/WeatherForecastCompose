package com.example.weatherforecastcompose.ui.screens.weather

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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.runtime.getValue
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
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

    WeatherScreen(
        onSearchDoneClick = { viewModel.processIntent(WeatherScreenIntent.SearchWeatherByCity) },
        onLocationClick = { coordinates ->
            viewModel.processIntent(WeatherScreenIntent.SearchWeatherByCoordinates(coordinates))
        },
        onSearchInputChanged = { searchInputValue ->
            viewModel.processIntent(
                WeatherScreenIntent.SearchInputChanged(
                    searchInputValue
                )
            )
        },
        onFavoriteIconClick = { viewModel.processIntent(WeatherScreenIntent.ChangeFavorite) },
        modifier = modifier,
//        navController = navController,
        uiState = uiState,
    )
}

@Composable
internal fun WeatherScreen(
    onSearchDoneClick: () -> Unit,
    onLocationClick: (coordinates: Coordinates) -> Unit,
    onSearchInputChanged: (String) -> Unit,
    onFavoriteIconClick: () -> Unit,
    modifier: Modifier,
    uiState: WeatherUiState,
//    navController: NavController,
) {
//    val statusBarPadding = WindowInsets.statusBars.asPaddingValues().calculateTopPadding()

    LazyColumn(modifier = modifier) {

        item {
            WeatherSearch(
                searchInput = uiState.searchInput,
                onSearchInputChanged = onSearchInputChanged,
                onSearchDoneClick = onSearchDoneClick,
                onLocationClick = onLocationClick
            )
        }
        item {
            WeatherCard(
                uiState = uiState,
                onFavoriteIconClick = onFavoriteIconClick
            )
        }
        item {
            SecondWeatherCard(weather = uiState.weather)
        }
        item {
            AirCard(air = uiState.weather.air)
        }
        item {
            ForecastCard(forecastList = uiState.weather.forecast)
        }
    }
}


@Composable
internal fun WeatherSearch(
    searchInput: String = "",
    onSearchInputChanged: (String) -> Unit,
    onSearchDoneClick: () -> Unit,
    onLocationClick: (coordinates: Coordinates) -> Unit
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManger = LocalFocusManager.current

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                start = 20.dp,
                top = 20.dp,
                end = 20.dp,
            ),
        colors = CardDefaults.cardColors(
            containerColor = Color.Transparent,
        ),
    ) {
        TextField(
            modifier = Modifier
                .fillMaxWidth(),
            value = searchInput,
            onValueChange = onSearchInputChanged,
            label = {
                Text(
                    text = stringResource(R.string.title_search),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
            },
            leadingIcon = {
                Icon(
                    modifier = Modifier.size(40.dp),
                    tint = MaterialTheme.colorScheme.onSurface,
                    painter = painterResource(R.drawable.ic_baseline_search),
                    contentDescription = "weather icon",
                )
            },
            trailingIcon = {
                IconButton(onClick = {
                    onLocationClick(
                        Coordinates("0.0", "0.0")
                    )
                }) {
                    Icon(
                        modifier = Modifier.size(40.dp),
                        tint = MaterialTheme.colorScheme.onSurface,
                        painter = painterResource(R.drawable.ic_baseline_my_location),
                        contentDescription = "weather icon",
                    )
                }
            },
            colors = TextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.surface,
                unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                cursorColor = MaterialTheme.colorScheme.onSurface,
                errorContainerColor = MaterialTheme.colorScheme.errorContainer,
                unfocusedIndicatorColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent
            ),
            singleLine = true,
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Done,
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    onSearchDoneClick()
                    focusManger.clearFocus()
                    keyboardController?.hide()
                }
            ),
        )
    }
}


@Composable
internal fun WeatherCard(
    uiState: WeatherUiState,
    onFavoriteIconClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                start = 20.dp,
                top = 10.dp,
                end = 20.dp,
            ),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
        ),
    ) {
        Row(
            Modifier
                .padding(10.dp)
                .fillMaxWidth()
                .height(IntrinsicSize.Min)
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Row {
                    Image(
                        modifier = Modifier.size(22.dp),
                        painter = painterResource(id = R.drawable.ic_baseline_location_on),
                        contentDescription = stringResource(id = R.string.image_content_description_city)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(text = uiState.weather.currentWeather.city)
                }
                Row(modifier = Modifier.padding(top = 6.dp)) {
                    Image(
                        modifier = Modifier.size(22.dp),
                        painter = painterResource(id = R.drawable.ic_calendar),
                        contentDescription = stringResource(id = R.string.image_content_description_date)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(text = uiState.weather.currentWeather.data)
                }
                Row(Modifier.padding(top = 10.dp)) {
                    Text(
                        text = uiState.weather.currentWeather.description,
                        fontSize = 22.sp
                    )
                }
                Text(text = uiState.weather.currentWeather.temperature, fontSize = 44.sp)
            }
            Column(
                modifier = Modifier.fillMaxHeight(),
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.SpaceBetween,
            ) {
                Icon(
                    modifier = Modifier.size(100.dp),
                    tint = MaterialTheme.colorScheme.onSurface,
                    painter = painterResource(
                        uiState.weather.currentWeather.icon.iconResId
                    ),
                    contentDescription = uiState.weather.currentWeather.description,
                )
                IconButton(onClick = onFavoriteIconClick) {
                    Icon(
                        modifier = Modifier.size(40.dp),
                        tint = MaterialTheme.colorScheme.onSurface,
                        painter = painterResource(
                            if (uiState.isFavorite) R.drawable.ic_baseline_favorite_24
                            else R.drawable.ic_baseline_favorite_border_24
                        ),
                        contentDescription = stringResource(id = R.string.image_content_description_is_favorite),
                    )
                }
            }
        }
    }
}

@Composable
internal fun SecondWeatherCard(
    weather: Weather
) {
    Row {
        SmallCard(
            modifier = Modifier
                .weight(1f)
                .padding(
                    start = 20.dp,
                    top = 10.dp,
                    end = 5.dp,
                ),
            titleResId = R.string.title_feels_like,
            value = weather.currentWeather.feelsLike,
            iconId = R.drawable.ic_temperature
        )
        SmallCard(
            modifier = Modifier
                .weight(1f)
                .padding(
                    start = 5.dp,
                    top = 10.dp,
                    end = 20.dp,
                ),
            titleResId = R.string.title_humidity,
            value = weather.currentWeather.humidity,
            iconId = R.drawable.ic_humidity
        )
    }

    Row {
        SmallCard(
            modifier = Modifier
                .weight(1f)
                .padding(
                    start = 20.dp,
                    top = 10.dp,
                    end = 5.dp,
                ),
            titleResId = R.string.title_pressure,
            value = weather.currentWeather.pressure,
            iconId = R.drawable.ic_pressure
        )
        SmallCard(
            modifier = Modifier
                .weight(1f)
                .padding(
                    start = 5.dp,
                    top = 10.dp,
                    end = 20.dp,
                ),
            titleResId = R.string.title_wind_speed,
            value = weather.currentWeather.windSpeed,
            iconId = R.drawable.ic_wind
        )
    }
}


@Composable
internal fun AirCard(air: Air) {
    Row {
        SmallCard(
            modifier = Modifier
                .weight(1f)
                .padding(
                    start = 20.dp,
                    top = 10.dp,
                    end = 5.dp,
                ),
            titleResId = R.string.title_no2,
            value = air.no2,
            iconId = air.no2Quality.iconResId,
            iconColor = Color(air.no2Quality.colorResId)
        )
        SmallCard(
            modifier = Modifier
                .weight(1f)
                .padding(
                    start = 5.dp,
                    top = 10.dp,
                    end = 20.dp,
                ),
            titleResId = R.string.title_o3,
            value = air.o3,
            iconId = air.o3Quality.iconResId,
            iconColor = Color(air.o3Quality.colorResId)
        )
    }
    Row {
        SmallCard(
            modifier = Modifier
                .weight(1f)
                .padding(
                    start = 20.dp,
                    top = 10.dp,
                    end = 5.dp,
                ),
            titleResId = R.string.title_pm10,
            value = air.pm10,
            iconId = air.pm10Quality.iconResId,
            iconColor = Color(air.pm10Quality.colorResId)
        )
        SmallCard(
            modifier = Modifier
                .weight(1f)
                .padding(
                    start = 5.dp,
                    top = 10.dp,
                    end = 20.dp,
                ),
            titleResId = R.string.title_pm25,
            value = air.pm25,
            iconId = air.pm25Quality.iconResId,
            iconColor = Color(air.pm25Quality.colorResId)
        )
    }
}

@Composable
internal fun ForecastCard(forecastList: List<Forecast>) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                start = 20.dp,
                top = 10.dp,
                end = 20.dp,
                bottom = 10.dp
            ),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
        ),
    ) {
        forecastList.forEachIndexed { index, item ->
            ForecastItemCard(
                iconId = item.weatherType.iconResId,
                day = item.data,
                humidity = item.humidity,
                temperature = item.temperature
            )
            if (forecastList.size != index + 1) {
                HorizontalDivider(
                    modifier = Modifier.padding(start = 20.dp, end = 20.dp),
                    color = MaterialTheme.colorScheme.onSurface,
                    thickness = 1.dp
                )
            }
        }
    }
}

@Composable
internal fun ForecastItemCard(
    iconId: Int,
    day: String,
    humidity: String,
    temperature: String,
) {

    Row(
        modifier = Modifier
            .padding(10.dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Icon(
            modifier = Modifier
                .size(60.dp),
            tint = MaterialTheme.colorScheme.onSurface,
            painter = painterResource(iconId),
            contentDescription = "weather icon",
        )
        Text(
            text = day,
            modifier = Modifier
                .align(Alignment.CenterVertically)
        )
        Row(
            modifier = Modifier
                .align(Alignment.CenterVertically)
        ) {
            Text(text = humidity)
            Icon(
                modifier = Modifier.size(24.dp),
                tint = MaterialTheme.colorScheme.onSurface,
                painter = painterResource(R.drawable.ic_humidity),
                contentDescription = "weather icon",
            )
        }
        Text(
            text = temperature,
            modifier = Modifier
        )
    }

}


@Composable
internal fun SmallCard(
    modifier: Modifier,
    titleResId: Int,
    value: String,
    iconId: Int,
    iconColor: Color = MaterialTheme.colorScheme.onSurface
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
    ) {
        Row(
            modifier = Modifier.padding(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(text = stringResource(id = titleResId))
                Text(text = value, fontSize = 24.sp)
            }
            Icon(
                modifier = Modifier.size(40.dp),
                tint = iconColor,
                painter = painterResource(iconId),
                contentDescription = stringResource(id = titleResId),
            )
        }
    }
}

@Composable
internal fun WeatherLoading() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {

        CircularProgressIndicator(
            modifier = Modifier.align(Alignment.Center),
            color = MaterialTheme.colorScheme.surfaceTint
        )
    }
}

@Preview
@Composable
internal fun WeatherScreenPreview() {
    WeatherForecastComposeTheme {
        Box(
            modifier = Modifier.paint(
                painterResource(id = R.drawable.sky_wallpaper),
                contentScale = ContentScale.FillBounds
            )
        )
        WeatherScreen(
            onSearchDoneClick = {},
            onLocationClick = {},
            onSearchInputChanged = {},
            onFavoriteIconClick = {},
            modifier = Modifier,
            uiState = WeatherUiState(
                weather = Weather.defaultEmptyWeather(),
            ),
        )
    }
}