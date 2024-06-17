package com.example.weatherforecastcompose.ui.screens.settings

import com.example.weatherforecastcompose.model.SupportedLanguage
import com.example.weatherforecastcompose.model.Units


sealed class SettingsScreenIntent {

    data class ChangeLanguage(val selectedLanguage: SupportedLanguage) : SettingsScreenIntent()

    data class ChangeUnits(val selectedUnits: Units) : SettingsScreenIntent()

}