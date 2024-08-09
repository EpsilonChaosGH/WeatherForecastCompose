package com.example.weatherforecastcompose.ui.screens.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.weatherforecastcompose.R
import com.example.weatherforecastcompose.mappers.toBottomSheetModel
import com.example.weatherforecastcompose.mappers.toDarkThemConfig
import com.example.weatherforecastcompose.mappers.toSupportedLanguage
import com.example.weatherforecastcompose.mappers.toUnits
import com.example.weatherforecastcompose.model.DarkThemeConfig
import com.example.weatherforecastcompose.model.SupportedLanguage
import com.example.weatherforecastcompose.model.Units
import com.example.weatherforecastcompose.ui.screens.settings.components.SettingOptionRow
import com.example.weatherforecastcompose.ui.screens.settings.components.SingleSelectBottomSheet
import kotlinx.coroutines.launch


@Composable
fun SettingsRoute(
    modifier: Modifier,
    viewModel: SettingsViewModel = hiltViewModel()
) {
    val uiState by viewModel.settingUiState.collectAsStateWithLifecycle()

    SettingsScreen(
        uiState = uiState,
        onLanguageChanged = { viewModel.obtainIntent(SettingsScreenIntent.ChangeLanguage(it)) },
        onUnitChanged = { viewModel.obtainIntent(SettingsScreenIntent.ChangeUnits(it)) },
        onDarkThemConfigChanged = {
            viewModel.obtainIntent(SettingsScreenIntent.ChangeDarkThemConfig(it))
        },
        modifier = modifier,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    uiState: SettingsScreenViewState,
    onLanguageChanged: (SupportedLanguage) -> Unit,
    onUnitChanged: (Units) -> Unit,
    onDarkThemConfigChanged: (DarkThemeConfig) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {

        val scope = rememberCoroutineScope()

        Text(text = "SETTINGS", fontSize = 66.sp, modifier = Modifier.padding(12.dp))

        val languageSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
        SettingOptionRow(
            optionLabel = stringResource(R.string.title_language),
            optionValue = uiState.selectedLanguage.languageName,
            onOptionClicked = {
                scope.launch {
                    languageSheetState.show()
                }
            },
            optionIcon = R.drawable.ic_language,
            optionIconValue = uiState.selectedLanguage.iconResId,
        )
        if (languageSheetState.isVisible) {
            SingleSelectBottomSheet(
                title = stringResource(id = R.string.title_language),
                sheetState = languageSheetState,
                selectedItem = uiState.selectedLanguage.toBottomSheetModel(true),
                items = uiState.availableLanguages.map { it.toBottomSheetModel(isSelected = false) },
                onSaveState = { bottomSheet ->
                    onLanguageChanged(bottomSheet.toSupportedLanguage())
                }
            )
        }

        val unitsSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
        SettingOptionRow(
            optionLabel = stringResource(R.string.title_units),
            optionValue = uiState.selectedUnit.tempLabel,
            onOptionClicked = {
                scope.launch {
                    unitsSheetState.show()
                }
            },
            optionIcon = R.drawable.ic_weather_thermometer,
            optionIconValue = uiState.selectedUnit.iconResId,
        )
        if (unitsSheetState.isVisible) {
            SingleSelectBottomSheet(
                title = stringResource(id = R.string.title_units),
                sheetState = unitsSheetState,
                selectedItem = uiState.selectedUnit.toBottomSheetModel(true),
                items = uiState.availableUnits.map { it.toBottomSheetModel(isSelected = false) },
                onSaveState = { bottomSheet ->
                    onUnitChanged(bottomSheet.toUnits())
                }
            )
        }

        val darkThemConfigSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
        SettingOptionRow(
            optionLabel = stringResource(R.string.title_dark_mode),
            optionValue = uiState.selectedDarkThemConfig.configName,
            onOptionClicked = {
                scope.launch {
                    darkThemConfigSheetState.show()
                }
            },
            optionIcon = R.drawable.ic_dark_theme,
            optionIconValue = uiState.selectedDarkThemConfig.iconResId,
        )
        if (darkThemConfigSheetState.isVisible) {
            SingleSelectBottomSheet(
                title = stringResource(id = R.string.title_dark_mode),
                sheetState = darkThemConfigSheetState,
                selectedItem = uiState.selectedDarkThemConfig.toBottomSheetModel(true),
                items = uiState.availableDarkThemConfig.map { it.toBottomSheetModel(isSelected = false) },
                onSaveState = { bottomSheet ->
                    onDarkThemConfigChanged(bottomSheet.toDarkThemConfig())
                }
            )
        }
    }
}