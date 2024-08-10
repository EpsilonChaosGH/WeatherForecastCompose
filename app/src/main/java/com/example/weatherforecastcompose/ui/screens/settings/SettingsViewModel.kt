package com.example.weatherforecastcompose.ui.screens.settings

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherforecastcompose.data.SettingsRepository
import com.example.weatherforecastcompose.model.DarkThemeConfig
import com.example.weatherforecastcompose.model.SupportedLanguage
import com.example.weatherforecastcompose.model.Units
import com.example.weatherforecastcompose.ui.screens.IntentHandler
import com.example.weatherforecastcompose.ui.screens.settings.SettingsUiState.Loading
import com.example.weatherforecastcompose.ui.screens.settings.SettingsUiState.Success
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.time.Duration.Companion.seconds

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val settingsRepository: SettingsRepository
) : ViewModel(), IntentHandler<SettingsScreenIntent> {

    val settingUiState: StateFlow<SettingsUiState> = combine(
        settingsRepository.getLanguage(),
        settingsRepository.getUnits(),
        settingsRepository.getDarkThemConfig()
    ) { language, units, config ->
        Log.e("aaa_settings", "$language ___ $units ___ $config")
        Success(
            data = UserEditableSettings(
                selectedLanguage = language,
                selectedUnit = units,
                selectedDarkThemConfig = config
            ),
        )
    }.stateIn(
        scope = viewModelScope,
        started = WhileSubscribed(5.seconds.inWholeMilliseconds),
        initialValue = Loading
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

data class UserEditableSettings(
    val selectedLanguage: SupportedLanguage,
    val selectedUnit: Units,
    val selectedDarkThemConfig: DarkThemeConfig,
    val availableLanguages: List<SupportedLanguage> = SupportedLanguage.entries,
    val availableUnits: List<Units> = Units.entries,
    val availableDarkThemConfig: List<DarkThemeConfig> = DarkThemeConfig.entries,
    val error: Throwable? = null
)

sealed interface SettingsUiState {
    data object Loading : SettingsUiState
    data class Success(val data: UserEditableSettings) : SettingsUiState
}