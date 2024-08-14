package com.example.weatherforecastcompose

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherforecastcompose.MainActivityUiState.Loading
import com.example.weatherforecastcompose.MainActivityUiState.Success
import com.example.weatherforecastcompose.data.SettingsRepository
import com.example.weatherforecastcompose.model.Coordinates
import com.example.weatherforecastcompose.model.DarkThemeConfig
import com.example.weatherforecastcompose.ui.screens.ActionHandler
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
) : ViewModel(), ActionHandler<MainAction> {

    val visiblePermissionDialogQueue = mutableStateListOf<String>()

    val uiState: StateFlow<MainActivityUiState> = settings.getDarkThemConfig().map {
        Success(it)
    }.stateIn(
        scope = viewModelScope,
        initialValue = Loading,
        started = SharingStarted.WhileSubscribed(5_000),
    )

    override fun onAction(action: MainAction) {
        when (val state = uiState.value) {
            Loading -> Unit
            is Success -> reduce(action = action, state = state)
        }
    }

    private fun reduce(action: MainAction, state: Success) {
        when (action) {
            MainAction.DismissPermissionDialog -> dismissDialog()
            is MainAction.PermissionResult -> onPermissionResult(
                permission = action.permission,
                isGranted = action.isGranted
            )

            is MainAction.ReceiveLocation -> setCoordinates(action.coordinates)
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

sealed interface MainAction {

    data object DismissPermissionDialog : MainAction

    data class PermissionResult(val permission: String, val isGranted: Boolean) : MainAction

    data class ReceiveLocation(val coordinates: Coordinates) : MainAction
}

sealed interface MainActivityUiState {
    data object Loading : MainActivityUiState
    data class Success(val data: DarkThemeConfig) : MainActivityUiState
}