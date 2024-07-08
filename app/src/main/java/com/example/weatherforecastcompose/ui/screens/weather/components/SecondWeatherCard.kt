package com.example.weatherforecastcompose.ui.screens.weather.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.weatherforecastcompose.R
import com.example.weatherforecastcompose.designsystem.WeatherAppTheme
import com.example.weatherforecastcompose.designsystem.WeatherForecastComposeTheme
import com.example.weatherforecastcompose.model.Coordinates
import com.example.weatherforecastcompose.model.CurrentWeather
import com.example.weatherforecastcompose.model.WeatherType

@Composable
internal fun SecondWeatherCard(currentWeather: CurrentWeather) {
    Row {
        SmallCard(
            value = currentWeather.feelsLike,
            titleResId = R.string.title_feels_like,
            iconResId = R.drawable.ic_weather_thermometer,
            modifier = Modifier
                .weight(WeatherAppTheme.weight.FULL)
                .padding(
                    start = WeatherAppTheme.dimens.medium,
                    top = WeatherAppTheme.dimens.small,
                    end = WeatherAppTheme.dimens.extraSmall,
                ),
        )
        SmallCard(
            value = currentWeather.humidity,
            titleResId = R.string.title_humidity,
            iconResId = R.drawable.ic_weather_humidity,
            modifier = Modifier
                .weight(WeatherAppTheme.weight.FULL)
                .padding(
                    start = WeatherAppTheme.dimens.extraSmall,
                    top = WeatherAppTheme.dimens.small,
                    end = WeatherAppTheme.dimens.medium,
                ),
        )
    }
    Row {
        SmallCard(
            value = currentWeather.pressure,
            titleResId = R.string.title_pressure,
            iconResId = R.drawable.ic_weather_barometer,
            modifier = Modifier
                .weight(WeatherAppTheme.weight.FULL)
                .padding(
                    start = WeatherAppTheme.dimens.medium,
                    top = WeatherAppTheme.dimens.small,
                    end = WeatherAppTheme.dimens.extraSmall,
                ),
        )
        SmallCard(
            value = currentWeather.windSpeed,
            titleResId = R.string.title_wind_speed,
            iconResId = R.drawable.ic_weather_wind,
            modifier = Modifier
                .weight(WeatherAppTheme.weight.FULL)
                .padding(
                    start = WeatherAppTheme.dimens.extraSmall,
                    top = WeatherAppTheme.dimens.small,
                    end = WeatherAppTheme.dimens.medium,
                ),
        )
    }
}

@Preview(showBackground = true)
@Composable
internal fun SecondWeatherCardPreview() {
    WeatherForecastComposeTheme {
        Column {
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