package com.example.weatherforecastcompose.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.weatherforecastcompose.R


enum class TopLevelDestination(
    val selectedIconId: Int,
    val unselectedIconId: Int,
    val iconTextId: Int,
) {
    Weather(
        selectedIconId = R.drawable.ic_home,
        unselectedIconId = R.drawable.ic_home,
        iconTextId = R.string.weather_icon_text,
    ),
    Favorites(
        selectedIconId = R.drawable.ic_baseline_favorite_24,
        unselectedIconId = R.drawable.ic_baseline_favorite_border_24,
        iconTextId = R.string.favorites_icon_text,
    ),
    Settings(
        selectedIconId = R.drawable.ic_settings,
        unselectedIconId = R.drawable.ic_settings,
        iconTextId = R.string.settings_icon_text,
    ),
}