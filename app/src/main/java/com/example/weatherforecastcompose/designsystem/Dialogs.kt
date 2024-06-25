package com.example.weatherforecastcompose.designsystem

import android.Manifest
import androidx.activity.result.ActivityResultLauncher
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.weatherforecastcompose.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PermissionRationaleDialog(
    isDialogShown: MutableState<Boolean>,
    activityPermissionResult: ActivityResultLauncher<String>,
    showWeatherUI: MutableState<Boolean>
) {
    BasicAlertDialog(
        onDismissRequest = { isDialogShown.value = false },
        modifier = Modifier
            .background(MaterialTheme.colorScheme.surface)
    ) {
        Column {
            LargeLabel(
                text = stringResource(R.string.location_rationale_title),
                modifier = Modifier.padding(
                    horizontal = 16.dp,//WeatherAppTheme.dimens.medium,
                    vertical = 8.dp,// WeatherAppTheme.dimens.small
                )
            )

            MediumBody(
                text = stringResource(R.string.location_rationale_description),
                modifier = Modifier.padding(16.dp)
            )

            Row(modifier = Modifier.padding(16.dp)) {
                PositiveButton(
                    text = stringResource(R.string.location_rationale_button_grant),
                    onClick = {
                        isDialogShown.value = false
                        activityPermissionResult.launch(Manifest.permission.ACCESS_COARSE_LOCATION)
                    }
                )
                Spacer(modifier = Modifier.weight(1f))
                NegativeButton(
                    text = stringResource(R.string.location_rationale_button_deny),
                    onClick = {
                        isDialogShown.value = false
                        showWeatherUI.value = false
                    }
                )
            }
        }
    }
}