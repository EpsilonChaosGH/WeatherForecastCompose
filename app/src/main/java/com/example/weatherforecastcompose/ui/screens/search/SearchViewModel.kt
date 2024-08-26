package com.example.weatherforecastcompose.ui.screens.search

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherforecastcompose.R
import com.example.weatherforecastcompose.data.RecentSearchRepository
import com.example.weatherforecastcompose.data.SettingsRepository
import com.example.weatherforecastcompose.data.WeatherRepository
import com.example.weatherforecastcompose.mappers.toResourceId
import com.example.weatherforecastcompose.model.AppResult
import com.example.weatherforecastcompose.model.City
import com.example.weatherforecastcompose.model.RecentSearchQuery
import com.example.weatherforecastcompose.ui.screens.ActionHandler
import com.example.weatherforecastcompose.utils.AppSideEffect
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val weatherRepository: WeatherRepository,
    private val settings: SettingsRepository,
    private val recentSearchRepository: RecentSearchRepository,
) : ViewModel(), ActionHandler<SearchScreenAction> {

    private val _state = MutableStateFlow(SearchUiState())
    val state: StateFlow<SearchUiState> = _state.asStateFlow()

    init {
        recentSearchRepository.getRecentSearchQueries(10)
            .onEach { searchQueries ->
                setState { copy(recentQueries = searchQueries) }
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.Eagerly,
                initialValue = null,
            )
    }

    override fun onAction(action: SearchScreenAction) {
        when (action) {
            is SearchScreenAction.SearchChanged -> searchChanged(action.searchValue)
            is SearchScreenAction.SearchTriggered -> searchTriggered(action.searchQuery)
            SearchScreenAction.ClearRecentSearches -> clearRecentSearches()
            SearchScreenAction.BackButtonClicked -> Unit
        }
    }

    private fun searchChanged(searchValue: String) {
        setState { copy(searchQuery = searchValue, errorMessageResId = null) }
    }

    private fun searchTriggered(query: String) {
        if (query == "") {
            setState { copy(errorMessageResId = R.string.error_empty_search) }
        } else {
            viewModelScope.launch {
                setState { copy(isLoading = true) }
                when (val response = weatherRepository.getCoordinatesByCity(City(query))) {
                    is AppResult.Success -> {
                        if (response.data != null) {
                            settings.setCoordinates(response.data)
                            recentSearchRepository.insertOrReplaceRecentSearch(searchQuery = query)
                            setState { copy(navigateToWeatherScreen = AppSideEffect(Unit)) }
                        } else setState { copy(errorMessageResId = R.string.error_wrong_city) }
                    }

                    is AppResult.Error -> {
                        setState { copy(errorMessageResId = response.errorType.toResourceId()) }
                    }
                }
                setState { copy(isLoading = false) }
            }
        }
    }

    private fun clearRecentSearches() {
        viewModelScope.launch {
            recentSearchRepository.clearRecentSearches()
        }
    }

    private fun setState(stateReducer: SearchUiState.() -> SearchUiState) {
        viewModelScope.launch {
            _state.emit(stateReducer(state.value))
        }
    }
}

data class SearchUiState(
    val searchQuery: String = "",
    val recentQueries: List<RecentSearchQuery> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessageResId: Int? = null,
    val navigateToWeatherScreen: AppSideEffect<Unit?> = AppSideEffect(null)
)