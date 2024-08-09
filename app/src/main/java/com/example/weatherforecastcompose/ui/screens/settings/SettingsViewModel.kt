package com.example.weatherforecastcompose.ui.screens.settings

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherforecastcompose.data.SettingsRepository
import com.example.weatherforecastcompose.model.DarkThemeConfig
import com.example.weatherforecastcompose.model.SupportedLanguage
import com.example.weatherforecastcompose.model.Units
import com.example.weatherforecastcompose.ui.screens.IntentHandler
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val settingsRepository: SettingsRepository
) : ViewModel(), IntentHandler<SettingsScreenIntent> {

    val settingUiState: StateFlow<SettingsScreenViewState> = combine(
        settingsRepository.getLanguage(),
        settingsRepository.getUnits(),
        settingsRepository.getDarkThemConfig()
    ) { language, units , config->
        Log.e("aaa_settingsFlowSETTINGS", "$language _____ $units")
        SettingsScreenViewState(
            selectedLanguage = language,
            selectedUnit = units,
            selectedDarkThemConfig = config
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = SettingsScreenViewState(),
    )

    override fun obtainIntent(intent: SettingsScreenIntent) {
        when (intent) {
            is SettingsScreenIntent.ChangeLanguage -> {
                viewModelScope.launch {
                    settingsRepository.setLanguage(intent.selectedLanguage)
                }
            }

            is SettingsScreenIntent.ChangeUnits -> {
                viewModelScope.launch {
                    settingsRepository.setUnits(intent.selectedUnits)
                }
            }

            is SettingsScreenIntent.ChangeDarkThemConfig -> {
                viewModelScope.launch {
                    settingsRepository.setDarkThemConfig(intent.selectedConfig)
                }
            }
        }
    }
}

data class SettingsScreenViewState(
    val selectedLanguage: SupportedLanguage = SupportedLanguage.ENGLISH,
    val availableLanguages: List<SupportedLanguage> = SupportedLanguage.entries,
    val selectedUnit: Units = Units.METRIC,
    val availableUnits: List<Units> = Units.entries,
    val selectedDarkThemConfig: DarkThemeConfig = DarkThemeConfig.FOLLOW_SYSTEM,
    val availableDarkThemConfig: List<DarkThemeConfig> = DarkThemeConfig.entries ,
    val error: Throwable? = null
)