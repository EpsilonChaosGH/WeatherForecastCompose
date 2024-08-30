package com.example.weatherforecastcompose.ui.navigation

import com.example.weatherforecastcompose.R


enum class TopLevelDestination(
    val selectedIconId: Int,
    val unselectedIconId: Int,
    val iconTextId: Int,
) {
    WEATHER(
        selectedIconId = R.drawable.ic_sunny_selected,
        unselectedIconId = R.drawable.ic_sunny_unselecred,
        iconTextId = R.string.icon_text_weather,
    ),
    FAVORITES(
        selectedIconId = R.drawable.ic_heart_selected,
        unselectedIconId = R.drawable.ic_heart_unselected,
        iconTextId = R.string.icon_text_favorites,
    ),
    SETTINGS(
        selectedIconId = R.drawable.ic_settings_selected,
        unselectedIconId = R.drawable.ic_settings_unselected,
        iconTextId = R.string.icon_text_settings,
    ),
}