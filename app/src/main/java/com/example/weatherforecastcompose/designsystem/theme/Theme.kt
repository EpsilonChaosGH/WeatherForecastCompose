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
//    primary = Purple40,
//    onPrimary = Color.White,
//    primaryContainer = Purple90,
//    onPrimaryContainer = Purple10,
//    secondary = Orange40,
//    onSecondary = Color.White,
//    secondaryContainer = Orange90,
//    onSecondaryContainer = Orange10,
//    tertiary = Blue40,
//    onTertiary = Color.White,
//    tertiaryContainer = Blue90,
//    onTertiaryContainer = Blue10,
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
//    inverseSurface = DarkPurpleGray20,
//    inverseOnSurface = DarkPurpleGray95,
//    outline = PurpleGray50,
)

@VisibleForTesting
val DarkColorScheme = darkColorScheme(
//    primary = Purple80,
//    onPrimary = Purple20,
//    primaryContainer = Purple30,
//    onPrimaryContainer = Purple90,
//    secondary = Orange80,
//    onSecondary = Orange20,
//    secondaryContainer = Orange30,
//    onSecondaryContainer = Orange90,
//    tertiary = Blue80,
//    onTertiary = Blue20,
//    tertiaryContainer = Blue30,
//    onTertiaryContainer = Blue90,
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
//    inverseSurface = DarkPurpleGray90,
//    inverseOnSurface = Color.Black,
//    outline = PurpleGray60,
)

@Composable
fun AppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
//        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
//            val context = LocalContext.current
//            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
//        }

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