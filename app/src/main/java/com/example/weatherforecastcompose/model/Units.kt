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
    STANDARD("Kelvin", R.drawable.ic_settings, "standard", "K","m/s"),
    METRIC("Celsius", R.drawable.ic_settings, "metric", "°C","m/s"),
    IMPERIAL("Fahrenheit", R.drawable.ic_settings, "imperial", "°F","mph");

    companion object {
        fun getUnitsSpinnerItem(): Array<SpinnerItem> {
            return entries.map {
                SpinnerItem(it.tempLabel, it.iconResId)
            }.toTypedArray()
        }

        fun getUnitsValue(index: Int): String {
            return entries[index].unitsValue
        }

        fun getIndex(value: String): Int {
            var result = 0
            entries.forEachIndexed { index, units ->
                if (units.unitsValue == value) result = index
            }
            return result
        }

        fun getUnits(value: String): String {
            val index = getIndex(value)
            return entries[index].tempUnits
        }
    }
}