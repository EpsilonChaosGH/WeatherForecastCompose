package com.example.weatherforecastcompose.ui.screens.weather.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.weatherforecastcompose.R
import com.example.weatherforecastcompose.designsystem.components.AppBackground
import com.example.weatherforecastcompose.designsystem.components.ThemePreviews
import com.example.weatherforecastcompose.designsystem.theme.AppTheme
import com.example.weatherforecastcompose.model.Coordinates
import com.example.weatherforecastcompose.model.CurrentWeather
import com.example.weatherforecastcompose.model.FavoritesCoordinates
import com.example.weatherforecastcompose.model.WeatherType

@Composable
internal fun WeatherCard(
    currentWeather: CurrentWeather,
    isFavorite: Boolean,
    onFavoriteIconClick: (favoriteCoordinate: FavoritesCoordinates, isFavorite: Boolean) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                start = AppTheme.dimens.medium,
                top = AppTheme.dimens.small,
                end = AppTheme.dimens.medium,
            )
    ) {
        Row(
            Modifier
                .padding(AppTheme.dimens.small)
                .fillMaxWidth()
                .height(IntrinsicSize.Min)
        ) {
            Column(
                modifier = Modifier.weight(AppTheme.weight.FULL),
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        modifier = Modifier.size(22.dp),
                        painter = painterResource(id = R.drawable.ic_location),
                        contentDescription = stringResource(id = R.string.image_content_description_city)
                    )
                    Spacer(modifier = Modifier.width(AppTheme.dimens.extraSmall))
                    Text(
                        text = currentWeather.city,
                        style = MaterialTheme.typography.headlineMedium
                    )
                }
                Row(
                    modifier = Modifier.padding(top = AppTheme.dimens.extraSmall),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        modifier = Modifier.size(22.dp),
                        painter = painterResource(id = R.drawable.ic_calendar),
                        contentDescription = stringResource(id = R.string.image_content_description_date)
                    )
                    Spacer(modifier = Modifier.width(AppTheme.dimens.extraSmall))
                    Text(
                        text = currentWeather.data,
                        style = MaterialTheme.typography.titleSmall
                    )
                }
                Row(Modifier.padding(top = AppTheme.dimens.small)) {
                    Text(
                        text = currentWeather.description,
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
                Spacer(modifier = Modifier.weight(AppTheme.weight.FULL))
                Text(
                    text = currentWeather.temperature,
                    style = MaterialTheme.typography.displayLarge
                )
            }
            Column(
                modifier = Modifier.fillMaxHeight(),
                horizontalAlignment = Alignment.End,
            ) {
                Icon(
                    modifier = Modifier.size(140.dp),
                    painter = painterResource(
                        currentWeather.icon.iconResId
                    ),
                    contentDescription = currentWeather.description,
                )
                IconButton(onClick = {
                    onFavoriteIconClick(
                        FavoritesCoordinates(
                            currentWeather.id,
                            currentWeather.coordinates
                        ),
                        isFavorite
                    )
                }) {
                    Icon(
                        modifier = Modifier.size(40.dp),
                        painter = painterResource(
                            if (isFavorite) R.drawable.ic_heart_selected
                            else R.drawable.ic_heart_unselected
                        ),
                        contentDescription = stringResource(id = R.string.image_content_description_is_favorite),
                    )
                }
            }
        }
    }
}

@ThemePreviews
@Composable
internal fun WeatherCardPreview() {
    AppTheme {
        AppBackground(modifier = Modifier.fillMaxWidth()) {
            WeatherCard(
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
                isFavorite = true,
                onFavoriteIconClick = { _, _ -> }
            )
        }
    }
}