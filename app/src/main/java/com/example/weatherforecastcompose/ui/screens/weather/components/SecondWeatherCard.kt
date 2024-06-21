package com.example.weatherforecastcompose.ui.screens.weather.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.weatherforecastcompose.R
import com.example.weatherforecastcompose.model.Coordinates
import com.example.weatherforecastcompose.model.CurrentWeather
import com.example.weatherforecastcompose.model.WeatherType
import com.example.weatherforecastcompose.ui.theme.WeatherForecastComposeTheme

@Composable
internal fun SecondWeatherCard(
    currentWeather: CurrentWeather
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
            value = currentWeather.feelsLike,
            iconId = R.drawable.ic_weather_thermometer
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
            value = currentWeather.humidity,
            iconId = R.drawable.ic_weather_humidity
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
            value = currentWeather.pressure,
            iconId = R.drawable.ic_weather_barometer
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
            value = currentWeather.windSpeed,
            iconId = R.drawable.ic_weather_wind
        )
    }
}

@Preview(showBackground = true)
@Composable
internal fun SecondWeatherCardPreview() {
    WeatherForecastComposeTheme {
        Column(
//            modifier = Modifier.paint(
//                painterResource(id = R.drawable.sky_wallpaper),
//                contentScale = ContentScale.FillBounds
//            )
        ) {
            SecondWeatherCard(
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
                )
            )
        }
    }
}