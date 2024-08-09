package com.example.weatherforecastcompose.ui.screens.favorites.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
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
import com.example.weatherforecastcompose.model.WeatherType

@Composable
fun FavoriteItemCard(
    currentWeather: CurrentWeather,
    onCardClick: () -> Unit,
) {

    Card(
        modifier = Modifier
            .clickable { onCardClick() }
            .fillMaxWidth()
            .padding(
                start = AppTheme.dimens.medium,
                top = AppTheme.dimens.extraSmall,
                bottom = AppTheme.dimens.extraSmall,
                end = AppTheme.dimens.medium,
            ),
    ) {
        Row(
            Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Min),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier
                    .weight(AppTheme.weight.FULL)
                    .padding(start = AppTheme.dimens.small, top = AppTheme.dimens.small),
            ) {
                Row {
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
                Row(modifier = Modifier.padding(top = AppTheme.dimens.extraSmall)) {
                    Icon(
                        modifier = Modifier.size(22.dp),
                        painter = painterResource(id = R.drawable.ic_calendar),
                        contentDescription = stringResource(id = R.string.image_content_description_date)
                    )
                    Spacer(modifier = Modifier.width(AppTheme.dimens.extraSmall))
                    Text(text = currentWeather.data, style = MaterialTheme.typography.titleMedium)
                }
                Row(Modifier.padding(AppTheme.dimens.small)) {
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
            Icon(
                modifier = Modifier.size(180.dp),
                painter = painterResource(
                    currentWeather.icon.iconResId
                ),
                contentDescription = currentWeather.description,
            )
        }
    }
}

@ThemePreviews
@Composable
internal fun FavoritesItemPreview() {
    AppTheme {
        AppBackground {
            FavoriteItemCard(
                currentWeather = CurrentWeather(
                    id = 0,
                    coordinates = Coordinates(
                        lon = "139.6917",
                        lat = "35.6895"
                    ),
                    city = "Moscow",
                    country = "RU",
                    temperature = "22°C",
                    icon = WeatherType.IC_02D,
                    description = "cloudy ",
                    feelsLike = "24°C",
                    humidity = "44%",
                    pressure = "1002hPa",
                    windSpeed = "7m/c",
                    data = "Wed,19 June 15:54",
                    timezone = 0,
                ),
                onCardClick = {}
            )
        }
    }
}