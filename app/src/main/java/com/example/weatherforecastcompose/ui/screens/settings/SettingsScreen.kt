package com.example.weatherforecastcompose.ui.screens.settings

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.weatherforecastcompose.R
import com.example.weatherforecastcompose.designsystem.components.AppBackground
import com.example.weatherforecastcompose.designsystem.theme.AppTheme
import com.example.weatherforecastcompose.mappers.toBottomSheetModel
import com.example.weatherforecastcompose.mappers.toDarkThemConfig
import com.example.weatherforecastcompose.mappers.toSupportedLanguage
import com.example.weatherforecastcompose.mappers.toUnits
import com.example.weatherforecastcompose.model.DarkThemeConfig
import com.example.weatherforecastcompose.model.SupportedLanguage
import com.example.weatherforecastcompose.model.Units
import com.example.weatherforecastcompose.ui.DevicePreviews
import com.example.weatherforecastcompose.ui.screens.settings.SettingsUiState.Loading
import com.example.weatherforecastcompose.ui.screens.settings.SettingsUiState.Success
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

@Composable
fun SettingsScreen(
    uiState: SettingsUiState,
    onLanguageChanged: (SupportedLanguage) -> Unit,
    onUnitChanged: (Units) -> Unit,
    onDarkThemConfigChanged: (DarkThemeConfig) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.fillMaxSize()) {

        Text(text = "SETTINGS", fontSize = 66.sp, modifier = Modifier.padding(12.dp))

        when (uiState) {
            Loading -> {
                Box(Modifier.fillMaxSize()) {
                    Text(
                        text = stringResource(R.string.title_loading),
                        modifier = Modifier
                            .padding(vertical = AppTheme.dimens.medium)
                            .align(Alignment.Center)
                    )
                }
            }

            is Success -> {
                SettingsContent(
                    settings = uiState.data,
                    onLanguageChanged = onLanguageChanged,
                    onUnitChanged = onUnitChanged,
                    onDarkThemConfigChanged = onDarkThemConfigChanged
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsContent(
    settings: UserEditableSettings,
    onLanguageChanged: (SupportedLanguage) -> Unit,
    onUnitChanged: (Units) -> Unit,
    onDarkThemConfigChanged: (DarkThemeConfig) -> Unit,
) {
    val scope = rememberCoroutineScope()
    val languageSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    SettingOptionRow(
        optionLabel = stringResource(R.string.title_language),
        optionValue = settings.selectedLanguage.languageName,
        optionIcon = R.drawable.ic_language,
        optionIconValue = settings.selectedLanguage.iconResId,
        onOptionClicked = {
            scope.launch {
                languageSheetState.show()
            }
        },
    )
    if (languageSheetState.isVisible) {
        SingleSelectBottomSheet(
            title = stringResource(id = R.string.title_language),
            sheetState = languageSheetState,
            selectedItem = settings.selectedLanguage.toBottomSheetModel(true),
            items = settings.availableLanguages.map { it.toBottomSheetModel(isSelected = false) },
            onSaveState = { bottomSheet ->
                onLanguageChanged(bottomSheet.toSupportedLanguage())
            }
        )
    }

    val unitsSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    SettingOptionRow(
        optionLabel = stringResource(R.string.title_units),
        optionValue = settings.selectedUnit.tempLabel,
        optionIcon = R.drawable.ic_weather_thermometer,
        optionIconValue = settings.selectedUnit.iconResId,
        onOptionClicked = {
            scope.launch {
                unitsSheetState.show()
            }
        },
    )
    if (unitsSheetState.isVisible) {
        SingleSelectBottomSheet(
            title = stringResource(id = R.string.title_units),
            sheetState = unitsSheetState,
            selectedItem = settings.selectedUnit.toBottomSheetModel(true),
            items = settings.availableUnits.map { it.toBottomSheetModel(isSelected = false) },
            onSaveState = { bottomSheet ->
                onUnitChanged(bottomSheet.toUnits())
            }
        )
    }

    val darkThemConfigSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    SettingOptionRow(
        optionLabel = stringResource(R.string.title_dark_mode),
        optionValue = settings.selectedDarkThemConfig.configName,
        optionIcon = R.drawable.ic_dark_theme,
        optionIconValue = settings.selectedDarkThemConfig.iconResId,
        onOptionClicked = {
            scope.launch {
                darkThemConfigSheetState.show()
            }
        },
    )
    if (darkThemConfigSheetState.isVisible) {
        SingleSelectBottomSheet(
            title = stringResource(id = R.string.title_dark_mode),
            sheetState = darkThemConfigSheetState,
            selectedItem = settings.selectedDarkThemConfig.toBottomSheetModel(true),
            items = settings.availableDarkThemConfig.map { it.toBottomSheetModel(isSelected = false) },
            onSaveState = { bottomSheet ->
                onDarkThemConfigChanged(bottomSheet.toDarkThemConfig())
            }
        )
    }
}

@DevicePreviews
@Composable
fun SettingsScreenLoadingPreview() {
    AppTheme {
        AppBackground {
            SettingsScreen(
                Loading,
                onLanguageChanged = {},
                onUnitChanged = {},
                onDarkThemConfigChanged = {}
            )
        }
    }
}

@DevicePreviews
@Composable
fun SettingsScreenDataPreview() {
    AppTheme {
        AppBackground {
            SettingsScreen(
                Success(
                    data = UserEditableSettings(
                        selectedLanguage = SupportedLanguage.ENGLISH,
                        selectedUnit = Units.METRIC,
                        selectedDarkThemConfig = DarkThemeConfig.FOLLOW_SYSTEM
                    )
                ),
                onLanguageChanged = {},
                onUnitChanged = {},
                onDarkThemConfigChanged = {}
            )
        }
    }
}