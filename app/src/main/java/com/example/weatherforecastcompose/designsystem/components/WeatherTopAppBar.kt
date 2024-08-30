@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.weatherforecastcompose.designsystem.components

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.LocationOn
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import com.example.weatherforecastcompose.designsystem.theme.AppTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WeatherTopAppBar(
    @StringRes titleRes: Int,
    navigationIcon: ImageVector,
    navigationIconContentDescription: String,
    actionIcon: ImageVector,
    actionIconContentDescription: String,
    modifier: Modifier = Modifier,
    colors: TopAppBarColors = TopAppBarDefaults.centerAlignedTopAppBarColors(),
    onNavigationClick: () -> Unit = {},
    onActionClick: () -> Unit = {},
) {
    CenterAlignedTopAppBar(
        title = { Text(text = stringResource(id = titleRes)) },
        navigationIcon = {
            Box(modifier = Modifier.padding(start = AppTheme.dimens.small)) {
                IconButton(onClick = onActionClick) {
                    Icon(
                        modifier = Modifier.fillMaxSize(),
                        imageVector = actionIcon,
                        contentDescription = actionIconContentDescription,
                        tint = MaterialTheme.colorScheme.onSurface,
                    )
                }
            }
        },
        actions = {
            Box(modifier = Modifier.padding(end = AppTheme.dimens.small)) {
                IconButton(onClick = onNavigationClick) {
                    Icon(
                        modifier = Modifier.fillMaxSize(),
                        imageVector = navigationIcon,
                        contentDescription = navigationIconContentDescription,
                        tint = MaterialTheme.colorScheme.onSurface,
                    )
                }
            }
        },
        colors = colors,
        modifier = modifier.testTag("niaTopAppBar"),
    )
}

@ThemePreviews
@Composable
private fun TopAppBarPreview() {
    AppTheme {
        AppBackground {
            WeatherTopAppBar(
                titleRes = android.R.string.untitled,
                navigationIcon = Icons.Rounded.Search,
                navigationIconContentDescription = "Navigation icon",
                actionIcon = Icons.Rounded.LocationOn,
                actionIconContentDescription = "Action icon",
            )
        }
    }
}