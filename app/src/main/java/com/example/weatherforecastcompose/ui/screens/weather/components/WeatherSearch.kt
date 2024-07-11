package com.example.weatherforecastcompose.ui.screens.weather.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.weatherforecastcompose.R
import com.example.weatherforecastcompose.designsystem.WeatherAppTheme
import com.example.weatherforecastcompose.designsystem.WeatherForecastComposeTheme

@Composable
internal fun WeatherSearch(
    searchInput: String,
    searchError: Boolean,
    errorMessageResId: Int?,
    onSearchInputChanged: (String) -> Unit,
    onSearchDoneClick: () -> Unit,
    onLocationClick: () -> Unit
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManger = LocalFocusManager.current

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                start = WeatherAppTheme.dimens.medium,
                top = WeatherAppTheme.dimens.small,
                end = WeatherAppTheme.dimens.medium,
            )
    ) {
        TextField(
            modifier = Modifier
                .fillMaxWidth(),
            value = searchInput,
            onValueChange = onSearchInputChanged,
            isError = searchError,
            label = {
                if (!searchError) {
                    Text(
                        text = stringResource(R.string.title_search),
                        style = MaterialTheme.typography.bodySmall,
                    )
                } else {
                    errorMessageResId?.let {
                        Text(
                            text = stringResource(it),
                            style = MaterialTheme.typography.bodySmall,
                        )
                    }
                }
            },
            leadingIcon = {
                if (!searchError) {
                    Icon(
                        modifier = Modifier.size(40.dp),
                        painter = painterResource(R.drawable.ic_search),
                        contentDescription = stringResource(R.string.image_content_description_search),
                    )
                } else {
                    Icon(
                        modifier = Modifier.size(40.dp),
                        tint = MaterialTheme.colorScheme.error,
                        painter = painterResource(R.drawable.ic_error),
                        contentDescription = stringResource(R.string.image_content_description_search_error),
                    )
                }
            },
            trailingIcon = {
                IconButton(onClick = onLocationClick) {
                    Icon(
                        modifier = Modifier.size(40.dp),
                        painter = painterResource(R.drawable.ic_my_location),
                        contentDescription = "weather icon",
                    )
                }
            },
            colors = TextFieldDefaults.colors(
                unfocusedIndicatorColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
                errorIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent
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

@Preview(showBackground = true)
@Composable
internal fun WeatherSearchPreview() {
    WeatherForecastComposeTheme {
        Box {
            Column {
                WeatherSearch(
                    searchInput = "Moscow",
                    searchError = false,
                    errorMessageResId = null,
                    onSearchInputChanged = {},
                    onSearchDoneClick = {},
                    onLocationClick = {}
                )

                WeatherSearch(
                    searchInput = "AAAAAAAAAAA",
                    searchError = true,
                    errorMessageResId = R.string.error_wrong_city,
                    onSearchInputChanged = {},
                    onSearchDoneClick = {},
                    onLocationClick = {}
                )
            }
        }
    }
}