package com.example.weatherforecastcompose

import com.example.weatherforecastcompose.model.Coordinates

sealed interface MainActivityAction {

    data object DismissPermissionDialog : MainActivityAction

    data class PermissionResult(val permission: String, val isGranted: Boolean) : MainActivityAction

    data class ReceiveLocation(val coordinates: Coordinates) : MainActivityAction
}