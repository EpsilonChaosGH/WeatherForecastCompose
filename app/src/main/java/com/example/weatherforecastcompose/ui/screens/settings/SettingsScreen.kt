package com.example.weatherforecastcompose.ui.screens.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.weatherforecastcompose.R
import com.example.weatherforecastcompose.mappers.toBottomSheetModel
import com.example.weatherforecastcompose.mappers.toSupportedLanguage
import com.example.weatherforecastcompose.mappers.toUnits
import com.example.weatherforecastcompose.model.SupportedLanguage
import com.example.weatherforecastcompose.model.Units
import com.example.weatherforecastcompose.ui.screens.settings.components.SettingOptionRow
import com.example.weatherforecastcompose.ui.screens.settings.components.SingleSelectBottomSheet
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    modifier: Modifier,
    uiState: SettingsScreenViewState,
    onLanguageChanged: (SupportedLanguage) -> Unit,
    onUnitChanged: (Units) -> Unit,
) {
    Column(modifier = modifier) {

        val scope = rememberCoroutineScope()

        Text(text = "SETTINGS", fontSize = 66.sp, modifier = Modifier.padding(12.dp))

        val languageSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
        SettingOptionRow(
            optionLabel = stringResource(R.string.title_language),
            optionValue = uiState.selectedLanguage.languageName,
            optionIcon = R.drawable.ic_language,
            optionIconValue = uiState.selectedLanguage.iconResId,
        ) {
            scope.launch {
                languageSheetState.show()
            }
        }
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
            optionIcon = R.drawable.ic_weather_thermometer,
            optionIconValue = uiState.selectedUnit.iconResId,
        ) {
            scope.launch {
                unitsSheetState.show()
            }
        }
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
    }
}