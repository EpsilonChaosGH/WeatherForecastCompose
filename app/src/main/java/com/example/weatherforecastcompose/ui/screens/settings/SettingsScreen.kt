package com.example.weatherforecastcompose.ui.screens.settings

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
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
import com.example.weatherforecastcompose.ui.screens.settings.components.SettingOptionRow
import com.example.weatherforecastcompose.ui.screens.settings.components.SingleSelectBottomSheet
import kotlinx.coroutines.launch

@Composable
fun SettingsRoute(
    modifier: Modifier = Modifier,
    viewModel: SettingsViewModel = hiltViewModel()
) {
    val uiState by viewModel.settingUiState.collectAsStateWithLifecycle()

    SettingsScreen(
        uiState = uiState,
        onAction = viewModel::onAction,
        modifier = modifier,
    )
}

@Composable
internal fun SettingsScreen(
    uiState: SettingsUiState?,
    onAction: (SettingsScreenAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(modifier = modifier.fillMaxSize()) {
        uiState?.let {
            SettingsContent(
                settings = uiState,
                onAction = onAction
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun SettingsContent(
    settings: SettingsUiState,
    onAction: (SettingsScreenAction) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                start = AppTheme.dimens.medium,
                top = AppTheme.dimens.extraSmall,
                end = AppTheme.dimens.medium,
            ),
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
                    onAction(SettingsScreenAction.ChangeLanguage(bottomSheet.toSupportedLanguage()))
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
                    onAction(SettingsScreenAction.ChangeUnits((bottomSheet.toUnits())))
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
                    onAction(SettingsScreenAction.ChangeDarkThemConfig(bottomSheet.toDarkThemConfig()))
                }
            )
        }
    }
}

@DevicePreviews
@Composable
fun SettingsScreenSuccessPreview() {
    AppTheme {
        AppBackground {
            SettingsScreen(
                uiState = SettingsUiState(
                    selectedLanguage = SupportedLanguage.ENGLISH,
                    selectedUnit = Units.METRIC,
                    selectedDarkThemConfig = DarkThemeConfig.FOLLOW_SYSTEM
                ),
                onAction = {}
            )
        }
    }
}