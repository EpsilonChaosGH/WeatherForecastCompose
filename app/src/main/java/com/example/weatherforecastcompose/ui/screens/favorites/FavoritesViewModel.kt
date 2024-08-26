package com.example.weatherforecastcompose.ui.screens.favorites

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherforecastcompose.R
import com.example.weatherforecastcompose.data.FavoritesRepository
import com.example.weatherforecastcompose.data.SettingsRepository
import com.example.weatherforecastcompose.data.WeatherRepository
import com.example.weatherforecastcompose.mappers.toResourceId
import com.example.weatherforecastcompose.model.AppResult
import com.example.weatherforecastcompose.model.Coordinates
import com.example.weatherforecastcompose.model.CurrentWeather
import com.example.weatherforecastcompose.model.Settings
import com.example.weatherforecastcompose.ui.screens.ActionHandler
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoritesViewModel @Inject constructor(
    private val weatherRepository: WeatherRepository,
    private val settings: SettingsRepository,
    private val favorites: FavoritesRepository
) : ViewModel(), ActionHandler<FavoritesScreenAction> {

    private val _settingsFlow = combine(
        settings.getLanguage(),
        settings.getUnits(),
        settings.getCoordinates(),
        favorites.getFavoritesListFlow()
    ) { language, units, coordinates, favoriteSet ->
        Log.e("aaa_settingsFlowF", "$language _ $units _ $coordinates")
        Settings(
            language = language,
            units = units,
            coordinates = coordinates,
            favoriteSet = favoriteSet
        )
    }

    private val _state: MutableStateFlow<FavoritesUiState> =
        MutableStateFlow(FavoritesUiState(favoritesItems = emptyList()))
    val state = _state.asStateFlow()

    init {
        _settingsFlow
            .onEach { settings -> loadFavoritesWeather(settings) }
            .launchIn(viewModelScope)
    }

    override fun onAction(action: FavoritesScreenAction) {
        when (action) {
            FavoritesScreenAction.RefreshScreenState -> refreshState()
            is FavoritesScreenAction.RemoveFromFavorites -> removeFromFavorite(action.id)
            is FavoritesScreenAction.FavoritesItemClicked -> setCoordinates(action.coordinates)
        }
    }

    private fun loadFavoritesWeather(settings: Settings) {
        viewModelScope.launch {
            setState { copy(isLoading = true) }
            getFavoritesCurrentWeather(settings)
        }
    }

    private fun refreshState() {
        viewModelScope.launch {
            setState { copy(isRefreshing = true) }
            getFavoritesCurrentWeather(_settingsFlow.first())
        }
    }

    private fun removeFromFavorite(id: Long) {
        viewModelScope.launch {
            favorites.removeFromFavorites(id)
        }
    }

    private fun setCoordinates(coordinates: Coordinates) {
        viewModelScope.launch {
            settings.setCoordinates(coordinates)
        }
    }

    private suspend fun getFavoritesCurrentWeather(settings: Settings) {
        if (settings.favoriteSet.isNotEmpty()) {
            val result = weatherRepository.getFavoritesCurrentWeather(
                settings.favoriteSet.map { it.coordinates },
                language = settings.language,
                units = settings.units
            )
            when (result) {
                is AppResult.Success -> {
                    setState {
                        copy(
                            favoritesItems = result.data,
                            isLoading = false,
                            isRefreshing = false,
                            errorMessageResId = null,
                        )
                    }
                }

                is AppResult.Error -> {
                    setState {
                        copy(
                            isLoading = false,
                            isRefreshing = false,
                            errorMessageResId = result.errorType.toResourceId(),
                        )
                    }
                }
            }
        } else {
            setState {
                copy(
                    isLoading = false,
                    isRefreshing = false,
                    errorMessageResId = R.string.error_empty_favorites,
                )
            }
        }
    }

    private fun setState(stateReducer: FavoritesUiState.() -> FavoritesUiState) {
        viewModelScope.launch {
            _state.emit(stateReducer(state.value))
        }
    }
}

data class FavoritesUiState(
    val favoritesItems: List<CurrentWeather>,
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    val errorMessageResId: Int? = null
)