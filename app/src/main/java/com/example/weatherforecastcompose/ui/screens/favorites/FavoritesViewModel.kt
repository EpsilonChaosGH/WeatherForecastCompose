package com.example.weatherforecastcompose.ui.screens.favorites

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherforecastcompose.R
import com.example.weatherforecastcompose.data.FavoritesRepository
import com.example.weatherforecastcompose.data.SettingsRepository
import com.example.weatherforecastcompose.data.WeatherRepository
import com.example.weatherforecastcompose.mappers.toResourceId
import com.example.weatherforecastcompose.model.Coordinates
import com.example.weatherforecastcompose.model.CurrentWeather
import com.example.weatherforecastcompose.model.Settings
import com.example.weatherforecastcompose.model.WeatherResult
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
        MutableStateFlow(FavoritesUiState.Loading())
    val state = _state.asStateFlow()


    init {
        _settingsFlow
            .onEach { onAction(FavoritesScreenAction.SettingsChanged(it)) }
            .launchIn(viewModelScope)
    }

    override fun onAction(action: FavoritesScreenAction) {
        when (val state = state.value) {
            is FavoritesUiState.Loading -> reduce(action, state)
            is FavoritesUiState.Success -> reduce(action, state)
            is FavoritesUiState.Error -> reduce(action, state)
        }
    }

    private fun reduce(intent: FavoritesScreenAction, state: FavoritesUiState.Loading) {
        when (intent) {
            FavoritesScreenAction.RefreshScreenState -> {
                _state.value = state.copy(isRefreshing = true)
                getFavoritesCurrentWeather()
            }

            is FavoritesScreenAction.RemoveFromFavorites -> Unit
            is FavoritesScreenAction.SettingsChanged -> getFavoritesCurrentWeather()
            is FavoritesScreenAction.SetCoordinates -> Unit
        }
    }

    private fun reduce(intent: FavoritesScreenAction, state: FavoritesUiState.Success) {
        when (intent) {
            FavoritesScreenAction.RefreshScreenState -> {
                _state.value = state.copy(isRefreshing = true)
                getFavoritesCurrentWeather()
            }

            is FavoritesScreenAction.RemoveFromFavorites -> removeFromFavorite(intent.id)
            is FavoritesScreenAction.SettingsChanged -> getFavoritesCurrentWeather()
            is FavoritesScreenAction.SetCoordinates -> setCoordinates(intent.coordinates)
        }
    }

    private fun reduce(intent: FavoritesScreenAction, state: FavoritesUiState.Error) {
        when (intent) {
            FavoritesScreenAction.RefreshScreenState -> {
                _state.value = state.copy(isRefreshing = true)
                getFavoritesCurrentWeather()
            }

            is FavoritesScreenAction.RemoveFromFavorites -> Unit
            is FavoritesScreenAction.SettingsChanged -> getFavoritesCurrentWeather()
            is FavoritesScreenAction.SetCoordinates -> Unit
        }
    }

    private fun setCoordinates(coordinates: Coordinates) {
        viewModelScope.launch {
            settings.setCoordinates(coordinates)
        }
    }

    private fun removeFromFavorite(id: Long) {
        viewModelScope.launch {
            favorites.removeFromFavorites(id)
        }
    }

    private fun getFavoritesCurrentWeather() {
        viewModelScope.launch {
            val settings = _settingsFlow.first()
            if (settings.favoriteSet.isNotEmpty()) {
                val result = weatherRepository.getFavoritesCurrentWeather(
                    settings.favoriteSet.map { it.coordinates },
                    language = settings.language,
                    units = settings.units
                )
                when (result) {
                    is WeatherResult.Success -> {
                        _state.value = FavoritesUiState.Success(
                            data = FavoritesList(
                                favoritesUiState = result.data.map { it }
                            )
                        )
                    }

                    is WeatherResult.Error -> {
                        _state.value =
                            FavoritesUiState.Error(errorMessageResId = result.errorType.toResourceId())
                    }
                }

            } else {
                _state.value = FavoritesUiState.Error(
                    isRefreshing = false,
                    errorMessageResId = R.string.error_empty_favorites
                )
            }
        }
    }
}


data class FavoritesList(
    val favoritesUiState: List<CurrentWeather>
)

sealed interface FavoritesUiState {
    var isRefreshing: Boolean

    data class Loading(override var isRefreshing: Boolean = false) : FavoritesUiState

    data class Success(
        override var isRefreshing: Boolean = false,
        val data: FavoritesList
    ) : FavoritesUiState

    data class Error(
        override var isRefreshing: Boolean = false,
        val errorMessageResId: Int
    ) : FavoritesUiState
}