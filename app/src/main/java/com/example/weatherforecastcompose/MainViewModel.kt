package com.example.weatherforecastcompose

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherforecastcompose.data.SettingsRepository
import com.example.weatherforecastcompose.model.Coordinates
import com.example.weatherforecastcompose.model.DarkThemeConfig
import com.example.weatherforecastcompose.ui.screens.ActionHandler
import com.example.weatherforecastcompose.utils.AppSideEffect
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val settings: SettingsRepository
) : ViewModel(), ActionHandler<MainActivityAction> {

    private val _state =
        MutableStateFlow(MainActivityUiState(visiblePermissionDialogQueue = mutableStateListOf()))
    val state: StateFlow<MainActivityUiState> = _state.asStateFlow()

    init {
        settings.getDarkThemConfig()
            .onEach { darkThemeConfig ->
                setState { copy(darkThemeConfig = darkThemeConfig) }
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.Eagerly,
                initialValue = null,
            )
    }

    override fun onAction(action: MainActivityAction) {
        when (action) {
            MainActivityAction.DismissPermissionDialog -> dismissDialog()
            is MainActivityAction.PermissionResult -> onPermissionResult(
                permission = action.permission,
                isGranted = action.isGranted
            )

            is MainActivityAction.ReceiveLocation -> setCoordinatesAndNavigate(action.coordinates)
        }
    }

    private fun setCoordinatesAndNavigate(coordinates: Coordinates) {
        viewModelScope.launch {
            settings.setCoordinates(coordinates)
            setState { copy(navigateToWeatherScreen = AppSideEffect(Unit)) }
        }
    }

    private fun dismissDialog() {
        state.value.visiblePermissionDialogQueue.removeFirst()
    }

    private fun onPermissionResult(
        permission: String,
        isGranted: Boolean
    ) {
        if (!isGranted && !state.value.visiblePermissionDialogQueue.contains(permission)) {
            state.value.visiblePermissionDialogQueue.add(permission)
        }
    }

    private fun setState(stateReducer: MainActivityUiState.() -> MainActivityUiState) {
        viewModelScope.launch {
            _state.emit(stateReducer(state.value))
        }
    }
}

data class MainActivityUiState(
    val darkThemeConfig: DarkThemeConfig = DarkThemeConfig.FOLLOW_SYSTEM,
    val visiblePermissionDialogQueue: SnapshotStateList<String>,
    val navigateToWeatherScreen: AppSideEffect<Unit?> = AppSideEffect(null)
)