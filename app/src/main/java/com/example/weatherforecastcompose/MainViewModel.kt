package com.example.weatherforecastcompose

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherforecastcompose.data.SettingsRepository
import com.example.weatherforecastcompose.model.Coordinates
import com.example.weatherforecastcompose.ui.screens.IntentHandler
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val settings: SettingsRepository
) : ViewModel(), IntentHandler<MainIntent> {

    val visiblePermissionDialogQueue = mutableStateListOf<String>()

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