package com.example.weatherforecastcompose.ui.screens.weather

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherforecastcompose.R
import com.example.weatherforecastcompose.data.WeatherRepository
import com.example.weatherforecastcompose.data.local.SettingsRepository
import com.example.weatherforecastcompose.mappers.toResourceId
import com.example.weatherforecastcompose.model.City
import com.example.weatherforecastcompose.model.ErrorType
import com.example.weatherforecastcompose.model.Settings
import com.example.weatherforecastcompose.model.Weather
import com.example.weatherforecastcompose.model.WeatherResult
import com.example.weatherforecastcompose.ui.screens.IntentHandler
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
class WeatherViewModel @Inject constructor(
    private val weatherRepository: WeatherRepository,
    private val settings: SettingsRepository
) : ViewModel(), IntentHandler<WeatherScreenIntent> {

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

    private val _state: MutableStateFlow<WeatherViewState> =
        MutableStateFlow(
            WeatherViewState(
                searchInput = "",
                isLoading = true,
                isRefreshing = false,
                searchError = false,
                errorMessageId = null,
                weatherUiState = null
            )
        )
    val state: StateFlow<WeatherViewState> = _state.asStateFlow()

    init {
        _settingsFlow
            .onEach { obtainIntent(WeatherScreenIntent.SettingsChanged(it)) }
            .launchIn(viewModelScope)
    }

    override fun obtainIntent(intent: WeatherScreenIntent) {
        when (intent) {
            WeatherScreenIntent.ChangeFavorite -> changeFavorite()

            is WeatherScreenIntent.SearchInputChanged -> _state.update {
                it.copy(searchInput = intent.value, searchError = false)
            }

            WeatherScreenIntent.SearchWeatherByCity -> getWeatherByCity()

            is WeatherScreenIntent.SearchWeatherByCoordinates -> {
                viewModelScope.launch { settings.setCoordinates(intent.value) }
            }

            WeatherScreenIntent.RefreshWeather -> refreshWeather()

            is WeatherScreenIntent.SettingsChanged -> {
                _state.update { it.copy(isLoading = true) }
                getWeatherByCoordinates(intent.value)
            }
        }
    }

    private fun changeFavorite() {
        viewModelScope.launch {
            state.value.weatherUiState?.let { weatherUiState ->
                if (weatherUiState.isFavorite) {
                    settings.removeFromFavorite(weatherUiState.weather.currentWeather.id)
                } else {
                    settings.addToFavorite(weatherUiState.weather.currentWeather.id)
                }
            }
        }
    }

    private fun getWeatherByCity() {
        viewModelScope.launch {
            if (state.value.searchInput != "") {
                val result = weatherRepository.getCoordinatesByCity(City(state.value.searchInput))

                when (result) {
                    is WeatherResult.Success -> settings.setCoordinates(result.data)
                    is WeatherResult.Error -> {
                        _state.update {
                            it.copy(
                                isLoading = false,
                                searchError = result.errorType == ErrorType.WRONG_CITY,
                                errorMessageId = result.errorType.toResourceId(),
                            )
                        }
                    }
                }
            } else {
                _state.update {
                    it.copy(
                        searchError = true,
                        errorMessageId = R.string.error_empty_city
                    )
                }
            }
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

    private fun refreshWeather() {
        viewModelScope.launch {
            _state.update { it.copy(isRefreshing = true) }
            getWeatherByCoordinates(_settingsFlow.first())
        }
    }

    private fun processResult(result: WeatherResult<Weather>) {
        when (result) {
            is WeatherResult.Success -> {
                val weatherData = result.data
                _state.update {
                    it.copy(
                        isLoading = false,
                        isRefreshing = false,
                        searchError = false,
                        errorMessageId = null,
                        weatherUiState = WeatherUiState(
                            weather = weatherData,
                            isFavorite = settings.checkToFavorite(weatherData.currentWeather.id)
                        )
                    )
                }
            }

            is WeatherResult.Error -> {
                _state.update {
                    it.copy(
                        isLoading = false,
                        isRefreshing = false,
                        searchError = result.errorType == ErrorType.WRONG_CITY,
                        errorMessageId = result.errorType.toResourceId(),
                    )
                }
            }
        }
    }
}

data class WeatherViewState(
    val searchInput: String,
    val isLoading: Boolean,
    val isRefreshing: Boolean,
    val searchError: Boolean,
    val errorMessageId: Int?,
    val weatherUiState: WeatherUiState?
)

data class WeatherUiState(
    val weather: Weather,
    val isFavorite: Boolean
)