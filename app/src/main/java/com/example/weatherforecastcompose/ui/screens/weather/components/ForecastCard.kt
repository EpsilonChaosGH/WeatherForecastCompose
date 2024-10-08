package com.example.weatherforecastcompose.ui.screens.weather.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.weatherforecastcompose.R
import com.example.weatherforecastcompose.designsystem.components.AppBackground
import com.example.weatherforecastcompose.designsystem.components.ThemePreviews
import com.example.weatherforecastcompose.designsystem.theme.AppTheme
import com.example.weatherforecastcompose.model.Forecast
import com.example.weatherforecastcompose.model.WeatherType

@Composable
internal fun ForecastCard(forecastList: List<Forecast>) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                start = AppTheme.dimens.medium,
                top = AppTheme.dimens.small,
                end = AppTheme.dimens.medium,
                bottom = AppTheme.dimens.small
            )
    ) {
        forecastList.forEachIndexed { index, item ->
            ForecastItemCard(
                iconResId = item.weatherType.iconResId,
                descriptionResId = item.weatherType.descResId,
                date = item.data,
                humidity = item.humidity,
                temperature = item.temperature
            )
            if (forecastList.size != index + 1) {
                HorizontalDivider(
                    modifier = Modifier.padding(horizontal = AppTheme.dimens.medium),
                    thickness = 1.dp
                )
            }
        }
    }
}

@Composable
internal fun ForecastItemCard(
    iconResId: Int,
    descriptionResId: Int,
    date: String,
    humidity: String,
    temperature: String,
) {
    Row(
        modifier = Modifier
            .padding(AppTheme.dimens.small)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Icon(
            modifier = Modifier.size(60.dp),
            painter = painterResource(iconResId),
            contentDescription = stringResource(descriptionResId),
        )
        Text(
            text = date,
            style = MaterialTheme.typography.titleMedium
        )
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = humidity,
                style = MaterialTheme.typography.titleMedium,
            )
            Icon(
                modifier = Modifier.size(AppTheme.dimens.large),
                painter = painterResource(R.drawable.ic_weather_humidity),
                contentDescription = stringResource(R.string.image_content_description_humidity),
            )
        }
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = temperature,
                style = MaterialTheme.typography.titleMedium
            )
            Icon(
                modifier = Modifier.size(AppTheme.dimens.large),
                painter = painterResource(R.drawable.ic_weather_thermometer),
                contentDescription = stringResource(R.string.image_content_description_temperature),
            )
        }
    }
}

@ThemePreviews
@Composable
internal fun ForecastCardPreview() {
    AppTheme {
        AppBackground(modifier = Modifier.fillMaxWidth()) {
            ForecastCard(
                listOf(
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
                )
            )
        }
    }
}