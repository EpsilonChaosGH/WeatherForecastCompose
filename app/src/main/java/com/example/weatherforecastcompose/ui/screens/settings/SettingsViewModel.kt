package com.example.weatherforecastcompose.ui.screens.settings

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherforecastcompose.data.local.SettingsRepository
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
    ) { language, units ->
        Log.e("aaa_settingsFlowSETTINGS", language.toString() + units.toString())
        SettingsScreenViewState(
            selectedLanguage = language,
            selectedUnit = units
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
        }
    }
}

data class SettingsScreenViewState(
    val selectedUnit: Units = Units.METRIC,
    val selectedLanguage: SupportedLanguage = SupportedLanguage.ENGLISH,
    val availableUnits: List<Units> = Units.entries,
    val availableLanguages: List<SupportedLanguage> = SupportedLanguage.entries,
    val error: Throwable? = null
)