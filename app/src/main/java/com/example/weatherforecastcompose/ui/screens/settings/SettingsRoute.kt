package com.example.weatherforecastcompose.ui.screens.settings

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController

@Composable
fun SettingsScreen(
    modifier: Modifier,
    viewModel: SettingsViewModel = hiltViewModel()
) {
    val uiState by viewModel.settingUiState.collectAsStateWithLifecycle()

    SettingsScreen(
        modifier = modifier,
        uiState = uiState,
        onLanguageChanged = { viewModel.obtainIntent(SettingsScreenIntent.ChangeLanguage(it)) },
        onUnitChanged = { viewModel.obtainIntent(SettingsScreenIntent.ChangeUnits(it)) }
    )
}