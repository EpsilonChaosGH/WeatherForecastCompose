package com.example.weatherforecastcompose

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.weatherforecastcompose.MainActivityUiState.*
import com.example.weatherforecastcompose.model.Coordinates
import com.example.weatherforecastcompose.designsystem.components.CoarseLocationPermissionTextProvider
import com.example.weatherforecastcompose.designsystem.components.PermissionDialog
import com.example.weatherforecastcompose.ui.navigation.AppNavHost
import com.example.weatherforecastcompose.designsystem.theme.AppTheme
import com.example.weatherforecastcompose.model.DarkThemeConfig
import com.google.android.gms.location.LocationServices
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModels()

    private val fusedLocationClient by lazy {
        LocationServices.getFusedLocationProviderClient(this)
    }

    private val permissionRequestLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            viewModel.onAction(
                MainAction.PermissionResult(
                    permission = Manifest.permission.ACCESS_COARSE_LOCATION,
                    isGranted = isGranted
                )
            )
            if (isGranted) getCurrentCoordinate()
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        var uiState: MainActivityUiState by mutableStateOf(Loading)

        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState
                    .onEach { uiState = it }
                    .collect()
            }
        }

        enableEdgeToEdge()

        setContent {
            val darkTheme = shouldUseDarkTheme(uiState)

            AppTheme(darkTheme = darkTheme) {

                val viewModel = viewModel<MainViewModel>()

                AppNavHost(onLocationClick = ::getCurrentCoordinate)

                viewModel.visiblePermissionDialogQueue
                    .reversed()
                    .forEach { permission ->
                        PermissionDialog(
                            permissionTextProvider = when (permission) {
                                Manifest.permission.ACCESS_COARSE_LOCATION -> {
                                    CoarseLocationPermissionTextProvider()
                                }

                                else -> return@forEach
                            },
                            isPermanentlyDeclined = !shouldShowRequestPermissionRationale(
                                permission
                            ),
                            onDismiss = { viewModel.onAction(MainAction.DismissPermissionDialog) },
                            onOkClick = { viewModel.onAction(MainAction.DismissPermissionDialog) },
                            onGoToAppSettingsClick = {
                                viewModel.onAction(MainAction.DismissPermissionDialog)
                                openAppSettings()
                            }
                        )
                    }
            }
        }
    }

    private fun getCurrentCoordinate() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            permissionRequestLauncher.launch(Manifest.permission.ACCESS_COARSE_LOCATION)
        } else {
            fusedLocationClient.lastLocation
                .addOnSuccessListener { location: Location? ->
                    if (location != null) {
                        val coordinates = Coordinates(
                            lat = location.latitude.toString(),
                            lon = location.longitude.toString()
                        )
                        viewModel.onAction(MainAction.ReceiveLocation(coordinates))
                    }
                }
        }
    }
}

fun Activity.openAppSettings() {
    Intent(
        Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
        Uri.fromParts("package", packageName, null)
    ).also(::startActivity)
}

@Composable
private fun shouldUseDarkTheme(
    uiState: MainActivityUiState,
): Boolean = when (uiState) {
    Loading -> isSystemInDarkTheme()
    is Success -> when (uiState.data) {
        DarkThemeConfig.FOLLOW_SYSTEM -> isSystemInDarkTheme()
        DarkThemeConfig.LIGHT -> false
        DarkThemeConfig.DARK -> true
    }
}