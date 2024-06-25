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
import androidx.core.app.ActivityCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.weatherforecastcompose.model.Coordinates
import com.example.weatherforecastcompose.ui.CoarseLocationPermissionTextProvider
import com.example.weatherforecastcompose.ui.PermissionDialog
import com.example.weatherforecastcompose.ui.navigation.AppNavHost
import com.example.weatherforecastcompose.ui.theme.WeatherForecastComposeTheme
import com.google.android.gms.location.LocationServices
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModels()

    private val fusedLocationClient by lazy {
        LocationServices.getFusedLocationProviderClient(this)
    }

    private val permissionRequestLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            viewModel.obtainIntent(
                MainIntent.PermissionResult(
                    permission = Manifest.permission.ACCESS_COARSE_LOCATION,
                    isGranted = isGranted
                )
            )
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        setContent {
            WeatherForecastComposeTheme {

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
                            onDismiss = { viewModel.obtainIntent(MainIntent.DismissPermissionDialog) },
                            onOkClick = { viewModel.obtainIntent(MainIntent.DismissPermissionDialog) },
                            onGoToAppSettingsClick = {
                                viewModel.obtainIntent(MainIntent.DismissPermissionDialog)
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
                        viewModel.obtainIntent(MainIntent.ReceiveLocation(coordinates))
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