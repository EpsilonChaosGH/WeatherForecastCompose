package com.example.weatherforecastcompose.ui.screens.weather.components

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.weatherforecastcompose.R
import com.example.weatherforecastcompose.model.Air
import com.example.weatherforecastcompose.model.AirQuality
import com.example.weatherforecastcompose.ui.theme.WeatherForecastComposeTheme

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

@Preview
@Composable
internal fun AirCardPreview() {
    WeatherForecastComposeTheme {
        Column(
            modifier = Modifier.paint(
                painterResource(id = R.drawable.sky_wallpaper),
                contentScale = ContentScale.FillBounds
            )
        ) {
            AirCard(
                air = Air(
                    no2 = "0μg/m3",
                    no2Quality = AirQuality.GOOD,
                    o3 = "33μg/m3",
                    o3Quality = AirQuality.MODERATE,
                    pm10 = "155μg/m3",
                    pm10Quality = AirQuality.POOR,
                    pm25 = "",
                    pm25Quality = AirQuality.ERROR,
                )
            )
        }
    }
}