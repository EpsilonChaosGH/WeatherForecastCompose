package com.example.weatherforecastcompose.ui.screens.favorites

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherforecastcompose.Const
import com.example.weatherforecastcompose.data.WeatherRepository
import com.example.weatherforecastcompose.data.local.SettingsRepository
import com.example.weatherforecastcompose.mappers.toResourceId
import com.example.weatherforecastcompose.model.Coordinates
import com.example.weatherforecastcompose.model.CurrentWeather
import com.example.weatherforecastcompose.model.ErrorType
import com.example.weatherforecastcompose.model.FavoriteCoordinates
import com.example.weatherforecastcompose.model.Settings
import com.example.weatherforecastcompose.model.WeatherResult
import com.example.weatherforecastcompose.model.WeatherType
import com.example.weatherforecastcompose.ui.screens.IntentHandler
import com.example.weatherforecastcompose.ui.screens.weather.WeatherUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoritesViewModel @Inject constructor(
    private val weatherRepository: WeatherRepository,
    private val settings: SettingsRepository
) : ViewModel(), IntentHandler<FavoritesScreenIntent> {

    private val _settingsFlow = combine(
        settings.getLanguage(),
        settings.getUnits(),
        settings.getCoordinates(),
        settings.getFavoriteSet()
    ) { language, units, coordinates, favoriteSet ->
        Log.e("aaa_settingsFlow", "$language _ $units _ $coordinates")
        Settings(
            language = language,
            units = units,
            coordinates = coordinates,
            favoriteSet = favoriteSet
        )
    }

    private val _state: MutableStateFlow<FavoritesViewState> =
        MutableStateFlow(
            FavoritesViewState(
                isLoading = true,
                isRefreshing = false,
                errorMessageId = null,
                favoritesUiState = emptyList()
            )
        )
    val state: StateFlow<FavoritesViewState> = _state.asStateFlow()

    init {
        _settingsFlow
            .onEach { obtainIntent(FavoritesScreenIntent.SettingsChanged(it)) }
            .launchIn(viewModelScope)
    }

    override fun obtainIntent(intent: FavoritesScreenIntent) {
        when (intent) {
            FavoritesScreenIntent.RefreshScreenState -> refreshWeather()
            is FavoritesScreenIntent.AddToFavorites -> addToFavorite(intent.value)
            is FavoritesScreenIntent.RemoveFromFavorites -> removeFromFavorite(intent.value)
            is FavoritesScreenIntent.SettingsChanged -> getFavoritesCurrentWeather()
        }
    }

    private fun addToFavorite(favoriteCoordinates: FavoriteCoordinates) {
        viewModelScope.launch {
            settings.addToFavorite(favoriteCoordinates)
        }
    }

    private fun removeFromFavorite(favoriteCoordinates: FavoriteCoordinates) {
        viewModelScope.launch {
            settings.removeFromFavorite(favoriteCoordinates)
        }
    }

    private fun refreshWeather() {
        viewModelScope.launch {
            _state.update { it.copy(isRefreshing = true) }
            getFavoritesCurrentWeather()
        }
    }

    private fun getFavoritesCurrentWeather() {
        viewModelScope.launch {
            val settings = _settingsFlow.first()
            val response = weatherRepository.getFavoritesCurrentWeather(
                settings.favoriteSet.map { it.coordinates }.toSet(),
                language = settings.language,
                units = settings.units
            )
            processResult(response)
        }
    }

    private fun processResult(result: WeatherResult<List<CurrentWeather>>) {
        when (result) {
            is WeatherResult.Success -> {
                _state.update {
                    it.copy(
                        isLoading = false,
                        isRefreshing = false,
                        errorMessageId = null,
                        favoritesUiState = result.data.map {
                            FavoritesUiState(
                                currentWeather = it,
                                isFavorite = true
                            )
                        }
                    )
                }
            }

            is WeatherResult.Error -> {
                _state.update {
                    it.copy(
                        isLoading = false,
                        isRefreshing = false,
                        errorMessageId = result.errorType.toResourceId(),
                    )
                }
            }
        }
    }
}

data class FavoritesViewState(
    val isLoading: Boolean,
    val isRefreshing: Boolean,
    val errorMessageId: Int?,
    val favoritesUiState: List<FavoritesUiState>
)

data class FavoritesUiState(
    val currentWeather: CurrentWeather,
    val isFavorite: Boolean
)