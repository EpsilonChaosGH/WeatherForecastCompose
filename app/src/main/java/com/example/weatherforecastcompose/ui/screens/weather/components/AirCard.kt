package com.example.weatherforecastcompose.ui.screens.weather.components

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import com.example.weatherforecastcompose.R
import com.example.weatherforecastcompose.designsystem.components.AppBackground
import com.example.weatherforecastcompose.designsystem.components.ThemePreviews
import com.example.weatherforecastcompose.designsystem.theme.AppTheme
import com.example.weatherforecastcompose.model.Air
import com.example.weatherforecastcompose.model.AirQuality

@SuppressLint("ResourceType")
@Composable
internal fun AirCard(air: Air) {
    Column {
        Row {
            SmallCard(
                value = air.no2,
                titleResId = R.string.title_no2,
                iconResId = air.no2Quality.iconResId,
                modifier = Modifier
                    .weight(AppTheme.weight.FULL)
                    .padding(
                        start = AppTheme.dimens.medium,
                        top = AppTheme.dimens.small,
                        end = AppTheme.dimens.extraSmall,
                    ),
                iconColor = colorResource(id = air.no2Quality.colorResId)
            )
            SmallCard(
                value = air.o3,
                titleResId = R.string.title_o3,
                iconResId = air.o3Quality.iconResId,
                modifier = Modifier
                    .weight(AppTheme.weight.FULL)
                    .padding(
                        start = AppTheme.dimens.extraSmall,
                        top = AppTheme.dimens.small,
                        end = AppTheme.dimens.medium,
                    ),
                iconColor = colorResource(id = air.o3Quality.colorResId)
            )
        }
        Row {
            SmallCard(
                value = air.pm10,
                titleResId = R.string.title_pm10,
                iconResId = air.pm10Quality.iconResId,
                modifier = Modifier
                    .weight(AppTheme.weight.FULL)
                    .padding(
                        start = AppTheme.dimens.medium,
                        top = AppTheme.dimens.small,
                        end = AppTheme.dimens.extraSmall,
                    ),
                iconColor = colorResource(id = air.pm10Quality.colorResId)
            )
            SmallCard(
                value = air.pm25,
                titleResId = R.string.title_pm25,
                iconResId = air.pm25Quality.iconResId,
                modifier = Modifier
                    .weight(AppTheme.weight.FULL)
                    .padding(
                        start = AppTheme.dimens.extraSmall,
                        top = AppTheme.dimens.small,
                        end = AppTheme.dimens.medium,
                    ),
                iconColor = colorResource(id = air.pm25Quality.colorResId)
            )
        }
    }
}

@ThemePreviews
@Composable
internal fun AirCardPreview() {
    AppTheme {
        AppBackground {
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