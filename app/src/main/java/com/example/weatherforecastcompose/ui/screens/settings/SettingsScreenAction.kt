package com.example.weatherforecastcompose.ui.screens.settings

import com.example.weatherforecastcompose.model.DarkThemeConfig
import com.example.weatherforecastcompose.model.SupportedLanguage
import com.example.weatherforecastcompose.model.Units


sealed interface SettingsScreenAction {

    data class ChangeLanguage(val selectedLanguage: SupportedLanguage) : SettingsScreenAction

    data class ChangeUnits(val selectedUnits: Units) : SettingsScreenAction

    data class ChangeDarkThemConfig(val selectedConfig: DarkThemeConfig) : SettingsScreenAction
}