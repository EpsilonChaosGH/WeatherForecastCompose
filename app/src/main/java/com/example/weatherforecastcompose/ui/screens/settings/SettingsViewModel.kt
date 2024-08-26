package com.example.weatherforecastcompose.ui.screens.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherforecastcompose.data.SettingsRepository
import com.example.weatherforecastcompose.model.DarkThemeConfig
import com.example.weatherforecastcompose.model.SupportedLanguage
import com.example.weatherforecastcompose.model.Units
import com.example.weatherforecastcompose.ui.screens.ActionHandler
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
) : ViewModel(), ActionHandler<SettingsScreenAction> {

    val settingUiState: StateFlow<SettingsUiState?> = combine(
        settingsRepository.getLanguage(),
        settingsRepository.getUnits(),
        settingsRepository.getDarkThemConfig()
    ) { language, units, config ->
        SettingsUiState(
            selectedLanguage = language,
            selectedUnit = units,
            selectedDarkThemConfig = config
        )
    }.stateIn(
        scope = viewModelScope,
        started = WhileSubscribed(5.seconds.inWholeMilliseconds),
        initialValue = null
    )

    override fun onAction(action: SettingsScreenAction) {
        when (action) {
            is SettingsScreenAction.ChangeLanguage -> {
                viewModelScope.launch { settingsRepository.setLanguage(action.selectedLanguage) }
            }

            is SettingsScreenAction.ChangeUnits -> {
                viewModelScope.launch { settingsRepository.setUnits(action.selectedUnits) }
            }

            is SettingsScreenAction.ChangeDarkThemConfig -> {
                viewModelScope.launch { settingsRepository.setDarkThemConfig(action.selectedConfig) }
            }
        }
    }
}

data class SettingsUiState(
    val selectedLanguage: SupportedLanguage,
    val selectedUnit: Units,
    val selectedDarkThemConfig: DarkThemeConfig,
    val availableLanguages: List<SupportedLanguage> = SupportedLanguage.entries,
    val availableUnits: List<Units> = Units.entries,
    val availableDarkThemConfig: List<DarkThemeConfig> = DarkThemeConfig.entries,
)