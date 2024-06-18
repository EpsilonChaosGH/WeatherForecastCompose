package com.example.weatherforecastcompose.model

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.example.weatherforecastcompose.R


data class Air(
    val no2: String,
    val no2Quality: AirQuality,
    val o3: String,
    val o3Quality: AirQuality,
    val pm10: String,
    val pm10Quality: AirQuality,
    val pm25: String,
    val pm25Quality: AirQuality
)

enum class AirQuality(@DrawableRes val colorResId: Int, @StringRes val iconResId: Int) {
    GOOD(R.color.green, R.drawable.ic_air_good),
    FAIR(R.color.green, R.drawable.ic_air_good),
    MODERATE(R.color.yellow, R.drawable.ic_air_good),
    POOR(R.color.orange, R.drawable.ic_air_poor),
    VERY_POOR(R.color.red, R.drawable.ic_air_poor),
    ERROR(R.color.red, R.drawable.ic_air_poor);
}