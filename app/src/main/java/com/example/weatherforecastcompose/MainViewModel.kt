package com.example.weatherforecastcompose

import android.service.autofill.UserData
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherforecastcompose.MainActivityUiState.*
import com.example.weatherforecastcompose.data.SettingsRepository
import com.example.weatherforecastcompose.model.Coordinates
import com.example.weatherforecastcompose.model.DarkThemeConfig
import com.example.weatherforecastcompose.ui.screens.IntentHandler
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val settings: SettingsRepository
) : ViewModel(), IntentHandler<MainIntent> {

    val visiblePermissionDialogQueue = mutableStateListOf<String>()

    val uiState: StateFlow<MainActivityUiState> = settings.getDarkThemConfig().map {
        Success(it)
    }.stateIn(
        scope = viewModelScope,
        initialValue = Loading,
        started = SharingStarted.WhileSubscribed(5_000),
    )

    override fun obtainIntent(intent: MainIntent) {
        when (intent) {
            MainIntent.DismissPermissionDialog -> dismissDialog()
            is MainIntent.PermissionResult -> onPermissionResult(
                permission = intent.permission,
                isGranted = intent.isGranted
            )

            is MainIntent.ReceiveLocation -> setCoordinates(intent.coordinates)
        }
    }

    private fun setCoordinates(coordinates: Coordinates) {
        viewModelScope.launch {
            settings.setCoordinates(coordinates)
        }
    }

    private fun dismissDialog() {
        visiblePermissionDialogQueue.removeFirst()
    }

    private fun onPermissionResult(
        permission: String,
        isGranted: Boolean
    ) {
        if (!isGranted && !visiblePermissionDialogQueue.contains(permission)) {
            visiblePermissionDialogQueue.add(permission)
        }
    }
}

sealed interface MainIntent {

    data object DismissPermissionDialog : MainIntent

    data class PermissionResult(val permission: String, val isGranted: Boolean) : MainIntent

    data class ReceiveLocation(val coordinates: Coordinates) : MainIntent
}

sealed interface MainActivityUiState {
    data object Loading : MainActivityUiState
    data class Success(val data: DarkThemeConfig) : MainActivityUiState
}