package com.example.weatherforecastcompose.model

import androidx.annotation.DrawableRes
import com.example.weatherforecastcompose.R


enum class Units(
    val tempLabel: String,
    @DrawableRes val iconResId: Int,
    val unitsValue: String,
    val tempUnits: String,
    val windUnits: String
) {
    STANDARD("Kelvin", R.drawable.ic_units_standard, "standard", "K", "m/s"),
    METRIC("Celsius", R.drawable.ic_units_metric, "metric", "°C", "m/s"),
    IMPERIAL("Fahrenheit", R.drawable.ic_units_imperial, "imperial", "°F", "mph");
}