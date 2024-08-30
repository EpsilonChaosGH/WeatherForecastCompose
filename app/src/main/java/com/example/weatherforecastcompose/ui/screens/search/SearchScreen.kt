package com.example.weatherforecastcompose.ui.screens.search

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsBottomHeight
import androidx.compose.foundation.layout.windowInsetsTopHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.weatherforecastcompose.R
import com.example.weatherforecastcompose.designsystem.components.AppBackground
import com.example.weatherforecastcompose.designsystem.components.ThemePreviews
import com.example.weatherforecastcompose.designsystem.theme.AppTheme

const val SEARCH_ROUTE = "SEARCH_ROUTE"

@Composable
internal fun SearchRoute(
    onBackClick: () -> Unit,
    onSearchClick: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: SearchViewModel = hiltViewModel(),
) {

    val uiState by viewModel.state.collectAsStateWithLifecycle()

    uiState.navigateToWeatherScreen.get()?.let { onSearchClick() }

    SearchScreen(
        uiState = uiState,
        onAction = { action ->
            when (action) {
                is SearchScreenAction.BackButtonClicked -> onBackClick()

                else -> viewModel.onAction(action)
            }
        },
        modifier = modifier,
    )
}

@Composable
internal fun SearchScreen(
    uiState: SearchUiState,
    onAction: (SearchScreenAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        Spacer(Modifier.windowInsetsTopHeight(WindowInsets.safeDrawing))
        SearchToolbar(
            searchQuery = uiState.searchQuery,
            onSearchQueryChanged = { onAction(SearchScreenAction.SearchChanged(it)) },
            onSearchTriggered = { onAction(SearchScreenAction.SearchTriggered(it)) },
            onBackClick = { onAction(SearchScreenAction.BackButtonClicked) },
            isError = (uiState.errorMessageResId != null),
            errorMessageResId = uiState.errorMessageResId
        )

        RecentSearchesBody(
            recentSearchQueries = uiState.recentQueries.map { it.query },
            onClearRecentSearches = { onAction(SearchScreenAction.ClearRecentSearches) },
            onRecentSearchClicked = { onAction(SearchScreenAction.SearchTriggered(it)) },
        )

        if (uiState.isLoading) {
            Box(modifier = Modifier.fillMaxSize()) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center),
                    color = MaterialTheme.colorScheme.surfaceTint
                )
            }
        }
    }
    Spacer(Modifier.windowInsetsBottomHeight(WindowInsets.safeDrawing))
}

@Composable
private fun RecentSearchesBody(
    recentSearchQueries: List<String>,
    onClearRecentSearches: () -> Unit,
    onRecentSearchClicked: (String) -> Unit
) {
    Column {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text(
                text = buildAnnotatedString {
                    withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                        append(stringResource(id = R.string.title_recent_searches))
                    }
                },
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            )
            if (recentSearchQueries.isNotEmpty()) {
                IconButton(
                    onClick = {
                        onClearRecentSearches()
                    },
                    modifier = Modifier.padding(horizontal = 16.dp),
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Close,
                        contentDescription = stringResource(
                            id = R.string.bt_clear_recent_searches,
                        ),
                        tint = MaterialTheme.colorScheme.onSurface,
                    )
                }
            }
        }
        LazyColumn(modifier = Modifier.padding(horizontal = 16.dp)) {
            items(recentSearchQueries) { recentSearch ->
                Text(
                    text = recentSearch,
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier
                        .padding(vertical = 16.dp)
                        .clickable { onRecentSearchClicked(recentSearch) }
                        .fillMaxWidth(),
                )
            }
        }
    }
}

@Composable
private fun SearchToolbar(
    searchQuery: String,
    onSearchQueryChanged: (String) -> Unit,
    onSearchTriggered: (String) -> Unit,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
    isError: Boolean = false,
    errorMessageResId: Int? = null
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier.fillMaxWidth(),
    ) {
        IconButton(onClick = { onBackClick() }) {
            Icon(
                imageVector = Icons.Rounded.ArrowBack,
                contentDescription = stringResource(
                    id = R.string.image_content_description_back_button,
                ),
            )
        }
        SearchTextField(
            onSearchQueryChanged = onSearchQueryChanged,
            onSearchTriggered = onSearchTriggered,
            searchQuery = searchQuery,
            isError = isError,
            errorMessageResId = errorMessageResId

        )
    }
}

@Composable
private fun SearchTextField(
    searchQuery: String,
    onSearchQueryChanged: (String) -> Unit,
    onSearchTriggered: (String) -> Unit,
    isError: Boolean,
    errorMessageResId: Int?
) {
    val focusRequester = remember { FocusRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current

    val onSearchExplicitlyTriggered = {
        keyboardController?.hide()
        onSearchTriggered(searchQuery)
    }

    TextField(
        colors = TextFieldDefaults.colors(
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent,
        ),
        leadingIcon = {
            if (!isError) {
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
        label = {
            if (!isError) {
                Text(
                    text = stringResource(R.string.title_search),
                    style = MaterialTheme.typography.bodyMedium,
                )
            } else {
                errorMessageResId?.let {
                    Text(
                        text = stringResource(it),
                        style = MaterialTheme.typography.bodyMedium,
                    )
                }
            }
        },
        trailingIcon = {
            if (searchQuery.isNotEmpty()) {
                IconButton(
                    onClick = {
                        onSearchQueryChanged("")
                    },
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Close,
                        contentDescription = stringResource(
                            id = R.string.image_content_description_close,
                        ),
                        tint = MaterialTheme.colorScheme.onSurface,
                    )
                }
            }
        },
        onValueChange = {
            if ("\n" !in it) onSearchQueryChanged(it)
        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .focusRequester(focusRequester)
            .onKeyEvent {
                if (it.key == Key.Enter) {
                    onSearchExplicitlyTriggered()
                    true
                } else {
                    false
                }
            }
            .testTag("searchTextField"),
        shape = RoundedCornerShape(32.dp),
        value = searchQuery,
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Search,
        ),
        keyboardActions = KeyboardActions(
            onSearch = {
                onSearchExplicitlyTriggered()
            },
        ),
        maxLines = 1,
        singleLine = true,
        isError = isError
    )
    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }
}

@ThemePreviews
@Composable
private fun SearchToolbarPreview() {
    AppTheme {
        AppBackground {
            SearchToolbar(
                searchQuery = "",
                onBackClick = {},
                onSearchQueryChanged = {},
                onSearchTriggered = {},
            )
        }
    }
}

@ThemePreviews
@Composable
private fun RecentSearchesBodyPreview() {
    AppTheme {
        AppBackground {
            RecentSearchesBody(
                onClearRecentSearches = {},
                onRecentSearchClicked = {},
                recentSearchQueries = listOf("kotlin", "jetpack compose", "testing"),
            )
        }
    }
}