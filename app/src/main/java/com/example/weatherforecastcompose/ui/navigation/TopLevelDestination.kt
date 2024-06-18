package com.example.weatherforecastcompose.ui.navigation

import com.example.weatherforecastcompose.R


enum class TopLevelDestination(
    val selectedIconId: Int,
    val unselectedIconId: Int,
    val iconTextId: Int,
) {
    Weather(
        selectedIconId = R.drawable.ic_sunny_selected,
        unselectedIconId = R.drawable.ic_sunny_unselecred,
        iconTextId = R.string.weather_icon_text,
    ),
    Favorites(
        selectedIconId = R.drawable.ic_heart_selected,
        unselectedIconId = R.drawable.ic_heart_unselected,
        iconTextId = R.string.favorites_icon_text,
    ),
    Settings(
        selectedIconId = R.drawable.ic_settings_selected,
        unselectedIconId = R.drawable.ic_settings_unselected,
        iconTextId = R.string.settings_icon_text,
    ),
}