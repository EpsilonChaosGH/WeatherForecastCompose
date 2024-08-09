package com.example.weatherforecastcompose.model

import androidx.annotation.DrawableRes
import com.example.weatherforecastcompose.R

enum class DarkThemeConfig(
    val configName: String,
    @DrawableRes val iconResId: Int
) {
    FOLLOW_SYSTEM("System" , R.drawable.ic_android_dark),
    LIGHT("Light", R.drawable.ic_android_light),
    DARK("Dark", R.drawable.ic_android_dark),
}