package com.example.weatherforecastcompose.ui.screens.weather

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.weatherforecastcompose.R
import com.example.weatherforecastcompose.model.Coordinates
import com.example.weatherforecastcompose.model.ErrorType
import com.example.weatherforecastcompose.ui.theme.WeatherForecastComposeTheme

@Composable
internal fun WeatherSearch(
    weatherViewState: WeatherViewState,
    onSearchInputChanged: (String) -> Unit,
    onSearchDoneClick: () -> Unit,
    onLocationClick: (coordinates: Coordinates) -> Unit
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManger = LocalFocusManager.current

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                start = 20.dp,
                top = 20.dp,
                end = 20.dp,
            ),
        colors = CardDefaults.cardColors(
            containerColor = Color.Transparent,
        ),
    ) {
        TextField(
            modifier = Modifier
                .fillMaxWidth(),
            value = weatherViewState.searchInput,
            onValueChange = onSearchInputChanged,
            isError = weatherViewState.searchError,
            label = {
                if (!weatherViewState.searchError) {
                    Text(
                        text = stringResource(R.string.title_search),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                } else {
                    weatherViewState.errorMessageId?.let {
                        Text(
                            text = stringResource(it),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            },
            leadingIcon = {
                if (!weatherViewState.searchError) {
                    Icon(
                        modifier = Modifier.size(40.dp),
                        tint = MaterialTheme.colorScheme.onSurface,
                        painter = painterResource(R.drawable.ic_baseline_search),
                        contentDescription = stringResource(R.string.image_content_description_search),
                    )
                } else {
                    Icon(
                        modifier = Modifier.size(40.dp),
                        tint = MaterialTheme.colorScheme.onError,
                        painter = painterResource(R.drawable.ic_error),
                        contentDescription = stringResource(R.string.image_content_description_search_error),
                    )
                }

            },
            trailingIcon = {
                IconButton(onClick = {
                    onLocationClick(
                        Coordinates("0.0", "0.0")
                    )
                }) {
                    Icon(
                        modifier = Modifier.size(40.dp),
                        tint = MaterialTheme.colorScheme.onSurface,
                        painter = painterResource(R.drawable.ic_baseline_my_location),
                        contentDescription = "weather icon",
                    )
                }
            },
            colors = TextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.surface,
                unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                cursorColor = MaterialTheme.colorScheme.onSurface,
                errorContainerColor = MaterialTheme.colorScheme.error,
                unfocusedIndicatorColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
                errorIndicatorColor = Color.Transparent
            ),
            singleLine = true,
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Done,
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    onSearchDoneClick()
                    focusManger.clearFocus()
                    keyboardController?.hide()
                }
            ),
        )
    }
}

@Preview
@Composable
internal fun WeatherSearchPreview() {
    WeatherForecastComposeTheme {
        Box(
            modifier = Modifier.paint(
                painterResource(id = R.drawable.sky_wallpaper),
                contentScale = ContentScale.FillBounds
            )
        ) {
            Column {
                WeatherSearch(
                    weatherViewState = WeatherViewState.Loading(
                        searchInput = "Moscow",
                        isLoading = false,
                        searchError = false,
                        errorMessageId = null
                    ),
                    onSearchInputChanged = {},
                    onSearchDoneClick = {},
                    onLocationClick = {}
                )

                WeatherSearch(
                    weatherViewState = WeatherViewState.Loading(
                        searchInput = "AAAAAAAA",
                        isLoading = false,
                        searchError = true,
                        errorMessageId = R.string.error_wrong_city
                    ),
                    onSearchInputChanged = {},
                    onSearchDoneClick = {},
                    onLocationClick = {}
                )
            }
        }
    }
}