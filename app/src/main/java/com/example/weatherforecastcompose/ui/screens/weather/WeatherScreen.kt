package com.example.weatherforecastcompose.ui.screens.weather

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.weatherforecastcompose.R
import com.example.weatherforecastcompose.model.Air
import com.example.weatherforecastcompose.model.CurrentWeather
import com.example.weatherforecastcompose.model.Forecast
import com.example.weatherforecastcompose.model.Weather

@Composable
internal fun WeatherScreen(
    onFavoriteIconClick: () -> Unit,
    modifier: Modifier,
    uiState: WeatherViewState.Display,
) {
//    val statusBarPadding = WindowInsets.statusBars.asPaddingValues().calculateTopPadding()

    LazyColumn {
        item {
            WeatherCard(
                uiState = uiState.weatherUiState,
                onFavoriteIconClick = onFavoriteIconClick
            )
        }
        item {
            SecondWeatherCard(weather = uiState.weatherUiState.weather.currentWeather)
        }
        item {
            AirCard(air = uiState.weatherUiState.weather.air)
        }
        item {
            ForecastCard(forecastList = uiState.weatherUiState.weather.forecast)
        }
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
    weather: CurrentWeather
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
            value = weather.feelsLike,
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
            value = weather.humidity,
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
            value = weather.pressure,
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
            value = weather.windSpeed,
            iconId = R.drawable.ic_wind
        )
    }
}


@SuppressLint("ResourceType")
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
            iconColor = colorResource(id = air.no2Quality.colorResId)
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
            iconColor = colorResource(id = air.o3Quality.colorResId)
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
            iconColor = colorResource(id = air.pm10Quality.colorResId)
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
            iconColor = colorResource(id = air.pm25Quality.colorResId)
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
        Row(
            modifier = Modifier
                .align(Alignment.CenterVertically)
        ) {
            Text(text = temperature)
            Icon(
                modifier = Modifier.size(24.dp),
                tint = MaterialTheme.colorScheme.onSurface,
                painter = painterResource(R.drawable.ic_temperature),
                contentDescription = "weather icon",
            )
        }
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
                Text(text = value, fontSize = 20.sp)
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
            Text(modifier = Modifier
                .align(Alignment.BottomCenter),
                fontSize = 26.sp,
                text = stringResource(id = it)
            )
        }
    }
}