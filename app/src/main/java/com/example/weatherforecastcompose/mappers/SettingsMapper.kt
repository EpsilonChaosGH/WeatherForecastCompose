package com.example.weatherforecastcompose.mappers

import com.example.weatherforecastcompose.model.DarkThemeConfig
import com.example.weatherforecastcompose.model.SupportedLanguage
import com.example.weatherforecastcompose.model.Units
import com.example.weatherforecastcompose.ui.screens.settings.components.BottomSheetItem

fun SupportedLanguage.toBottomSheetModel(isSelected: Boolean = false): BottomSheetItem {
    return BottomSheetItem(
        id = this.ordinal,
        name = this.languageName,
        imageResId = this.iconResId,
        isSelected = isSelected
    )
}

fun BottomSheetItem.toSupportedLanguage(): SupportedLanguage {
    return SupportedLanguage.entries.first { it.ordinal == this.id }
}

fun Units.toBottomSheetModel(isSelected: Boolean = false): BottomSheetItem {
    return BottomSheetItem(
        id = this.ordinal,
        name = this.tempLabel,
        imageResId = this.iconResId,
        isSelected = isSelected,
    )
}

fun BottomSheetItem.toUnits(): Units {
    return Units.entries.first { it.ordinal == this.id }
}

fun DarkThemeConfig.toBottomSheetModel(isSelected: Boolean = false): BottomSheetItem {
    return BottomSheetItem(
        id = this.ordinal,
        name = this.configName,
        imageResId = this.iconResId,
        isSelected = isSelected,
    )
}

fun BottomSheetItem.toDarkThemConfig(): DarkThemeConfig {
    return DarkThemeConfig.entries.first { it.ordinal == this.id }
}