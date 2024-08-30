package com.example.weatherforecastcompose.designsystem.theme

import androidx.annotation.VisibleForTesting
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color


@VisibleForTesting
val LightColorScheme = lightColorScheme(
    error = Red40,
    onError = Color.White,
    errorContainer = Red90,
    onErrorContainer = Red10,
    background = Color.White,
    onBackground = Color.Black,
    surface = Color.White,
    onSurface = Color.Black,
    surfaceVariant = Gray40,
    onSurfaceVariant = Color.Black,
    surfaceContainerLowest = Gray10
)

@VisibleForTesting
val DarkColorScheme = darkColorScheme(
    error = Red80,
    onError = Red20,
    errorContainer = Red30,
    onErrorContainer = Red90,
    background = Color.Black,
    onBackground = Gray30,
    surface = Color.Black,
    onSurface = Gray30,
    surfaceVariant = Black10,
    onSurfaceVariant = Gray30,
    surfaceContainerLowest = Gray10
)

@Composable
fun AppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}

object AppTheme {
    val dimens: Dimensions
        @Composable
        get() = LocalDimens.current

    val weight: Weight
        @Composable
        get() = LocalWeight.current
}