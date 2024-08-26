package com.example.weatherforecastcompose.ui.screens.weather

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherforecastcompose.data.FavoritesRepository
import com.example.weatherforecastcompose.data.SettingsRepository
import com.example.weatherforecastcompose.data.WeatherRepository
import com.example.weatherforecastcompose.mappers.toResourceId
import com.example.weatherforecastcompose.model.FavoritesCoordinates
import com.example.weatherforecastcompose.model.Settings
import com.example.weatherforecastcompose.model.Weather
import com.example.weatherforecastcompose.model.AppResult
import com.example.weatherforecastcompose.ui.screens.ActionHandler
import com.example.weatherforecastcompose.ui.screens.favorites.FavoritesUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel @Inject constructor(
    private val weatherRepository: WeatherRepository,
    settings: SettingsRepository,
    private val favorites: FavoritesRepository,
) : ViewModel(), ActionHandler<WeatherScreenAction> {

    private val _settingsFlow = combine(
        settings.getLanguage(),
        settings.getUnits(),
        settings.getCoordinates(),
        favorites.getFavoritesListFlow()
    ) { language, units, coordinates, favoriteSet ->
        Log.e("aaa_settingsFlow", "$language _ $units _ $coordinates")
        Settings(
            language = language,
            units = units,
            coordinates = coordinates,
            favoriteSet = favoriteSet
        )
    }

    private val _state: MutableStateFlow<WeatherUiState> = MutableStateFlow(WeatherUiState())
    val state: StateFlow<WeatherUiState> = _state.asStateFlow()

    init {
        _settingsFlow
            .onEach { loadFavoritesWeather(it) }
            .launchIn(viewModelScope)
    }

    override fun onAction(action: WeatherScreenAction) {
        when (action) {
            WeatherScreenAction.RefreshScreenState -> refreshState()
            is WeatherScreenAction.AddToFavorites -> addToFavorite(action.favoritesCoordinates)
            is WeatherScreenAction.RemoveFromFavorites -> removeFromFavorite(action.id)
        }
    }

    private fun loadFavoritesWeather(settings: Settings) {
        viewModelScope.launch {
            setState { copy(isLoading = true) }
            getWeatherByCoordinates(settings)
        }
    }

    private fun refreshState() {
        viewModelScope.launch {
            setState { copy(isRefreshing = true) }
            getWeatherByCoordinates(_settingsFlow.first())
        }
    }

    private fun addToFavorite(favoritesCoordinates: FavoritesCoordinates) {
        viewModelScope.launch {
            favorites.addToFavorites(favoritesCoordinates)
        }
    }

    private fun removeFromFavorite(id: Long) {
        viewModelScope.launch {
            favorites.removeFromFavorites(id)
        }
    }

    private fun getWeatherByCoordinates(settings: Settings) {
        viewModelScope.launch {
            val result = weatherRepository.getWeather(
                coordinates = settings.coordinates,
                language = settings.language,
                units = settings.units
            )
            processResult(result)
        }
    }

    private fun processResult(result: AppResult<Weather>) {
        viewModelScope.launch {
            when (result) {
                is AppResult.Success -> {
                    val isFavorite = favorites.checkForFavorite(result.data.currentWeather.id)
                    setState {
                        copy(
                            weather = result.data,
                            isFavorite = isFavorite,
                            isLoading = false,
                            isRefreshing = false,
                            errorMessageResId = null,
                        )
                    }
                }

                is AppResult.Error -> {
                    setState {
                        copy(
                            weather = null,
                            isFavorite = false,
                            isLoading = false,
                            isRefreshing = false,
                            errorMessageResId = result.errorType.toResourceId(),
                        )
                    }
                }
            }
        }
    }

    private fun setState(stateReducer: WeatherUiState.() -> WeatherUiState) {
        viewModelScope.launch {
            _state.emit(stateReducer(state.value))
        }
    }
}

data class WeatherUiState(
    val weather: Weather? = null,
    val isFavorite: Boolean = false,
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    val errorMessageResId: Int? = null
)