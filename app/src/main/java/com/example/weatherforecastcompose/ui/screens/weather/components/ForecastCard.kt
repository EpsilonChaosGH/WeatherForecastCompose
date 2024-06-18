package com.example.weatherforecastcompose.ui.screens.weather.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.weatherforecastcompose.R
import com.example.weatherforecastcompose.model.Forecast
import com.example.weatherforecastcompose.model.WeatherType
import com.example.weatherforecastcompose.ui.theme.WeatherForecastComposeTheme

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
//        colors = CardDefaults.cardColors(
//            containerColor = MaterialTheme.colorScheme.surface,
//        ),
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
//                    color = MaterialTheme.colorScheme.onSurface,
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
//            tint = MaterialTheme.colorScheme.onSurface,
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
//                tint = MaterialTheme.colorScheme.onSurface,
                painter = painterResource(R.drawable.ic_weather_humidity),
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
//                tint = MaterialTheme.colorScheme.onSurface,
                painter = painterResource(R.drawable.ic_weather_thermometer),
                contentDescription = "weather icon",
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
internal fun ForecastCardPreview() {
    WeatherForecastComposeTheme {
        Column(
//            modifier = Modifier.paint(
//                painterResource(id = R.drawable.sky_wallpaper),
//                contentScale = ContentScale.FillBounds
//            )
        ) {
            ForecastCard(
                listOf(
                    Forecast(
                        temperature = "22",
                        data = "15.06.2024",
                        humidity = "44",
                        weatherType = WeatherType.IC_02D
                    ),
                    Forecast(
                        temperature = "15",
                        data = "15.06.2024",
                        humidity = "33",
                        weatherType = WeatherType.IC_01N
                    ),
                    Forecast(
                        temperature = "-4",
                        data = "15.06.2024",
                        humidity = "77",
                        weatherType = WeatherType.IC_01D
                    )
                )
            )
        }
    }
}