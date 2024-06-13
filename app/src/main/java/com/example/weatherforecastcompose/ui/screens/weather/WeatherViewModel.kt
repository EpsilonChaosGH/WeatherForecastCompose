package com.example.weatherforecastcompose.ui.screens.weather

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherforecastcompose.R
import com.example.weatherforecastcompose.ui.screens.IntentHandler
import com.example.weatherforecastcompose.data.local.SettingsRepository
import com.example.weatherforecastcompose.data.WeatherRepository
import com.example.weatherforecastcompose.mappers.toResourceId
import com.example.weatherforecastcompose.model.City
import com.example.weatherforecastcompose.model.Coordinates
import com.example.weatherforecastcompose.model.ErrorType
import com.example.weatherforecastcompose.model.Settings
import com.example.weatherforecastcompose.model.Weather
import com.example.weatherforecastcompose.model.WeatherResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
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
        settings.getDefaultLocation(),
        settings.getFavoriteSet()
    ) { language, units, defaultLocation, favoriteSet ->
        Settings(units, language, defaultLocation, favoriteSet)
    }

    private val _state: MutableStateFlow<WeatherViewState> =
        MutableStateFlow(WeatherViewState.Loading())
    val state: StateFlow<WeatherViewState> = _state.asStateFlow()

    init {
        _settingsFlow
            .onStart { obtainIntent(WeatherScreenIntent.LoadWeatherScreenData(_settingsFlow.first())) }
            .onEach { obtainIntent(WeatherScreenIntent.SettingsChanged(it)) }
            .launchIn(viewModelScope)
    }

    override fun obtainIntent(intent: WeatherScreenIntent) {
        when (val state = state.value) {
            is WeatherViewState.Loading -> reduce(intent, state)
            is WeatherViewState.Display -> reduce(intent, state)
        }
    }

    private fun reduce(intent: WeatherScreenIntent, currentState: WeatherViewState.Loading) {
        when (intent) {
            WeatherScreenIntent.ChangeFavorite -> Unit

            is WeatherScreenIntent.LoadWeatherScreenData -> {
                viewModelScope.launch {
                    val result = getWeatherByCoordinates(
                        coordinates = intent.value.defaultLocation,
                        settings = intent.value
                    )
                    processResult(result, currentState)
                }
            }

            is WeatherScreenIntent.SearchInputChanged -> _state.update {
                currentState.copy(
                    searchInput = intent.value,
                    searchError = false
                )
            }

            WeatherScreenIntent.SearchWeatherByCity -> {
                if (currentState.searchInput != "") {
                    viewModelScope.launch {
                        val result = getWeatherByCity(
                            city = City(currentState.searchInput),
                            settings = _settingsFlow.first()
                        )
                        processResult(result, currentState)
                    }
                } else {
                    _state.update { currentState.copy(searchError = true) }
                }
            }

            is WeatherScreenIntent.SearchWeatherByCoordinates -> {
                viewModelScope.launch {
                    val result = getWeatherByCoordinates(
                        coordinates = intent.coordinates,
                        settings = _settingsFlow.first()
                    )
                    processResult(result, currentState)
                }
            }

            is WeatherScreenIntent.SettingsChanged -> Unit
        }
    }

    private fun reduce(intent: WeatherScreenIntent, currentState: WeatherViewState.Display) {
        when (intent) {
            WeatherScreenIntent.ChangeFavorite -> {
                viewModelScope.launch {
                    if (currentState.weatherUiState.isFavorite) {
                        settings.removeFromFavorite(currentState.weatherUiState.weather.currentWeather.id)
                    } else {
                        settings.addToFavorite(currentState.weatherUiState.weather.currentWeather.id)
                    }
                }
            }

            is WeatherScreenIntent.LoadWeatherScreenData -> Unit

            is WeatherScreenIntent.SearchInputChanged -> _state.update {
                currentState.copy(
                    searchInput = intent.value,
                    searchError = false
                )
            }

            WeatherScreenIntent.SearchWeatherByCity -> {
                if (currentState.searchInput != "") {
                    viewModelScope.launch {
                        val result = getWeatherByCity(
                            city = City(currentState.searchInput),
                            settings = _settingsFlow.first()
                        )
                        processResult(result, currentState)
                    }
                } else {
                    _state.update { currentState.copy(searchError = true, errorMessageId = R.string.error_empty_city) }
                }
            }

            is WeatherScreenIntent.SearchWeatherByCoordinates -> {
                viewModelScope.launch {
                    val result = getWeatherByCoordinates(
                        coordinates = intent.coordinates,
                        settings = _settingsFlow.first()
                    )
                    processResult(result, currentState)
                }
            }

            is WeatherScreenIntent.SettingsChanged -> {
                viewModelScope.launch {
                    val result = getWeatherByCoordinates(
                        coordinates = currentState.weatherUiState.weather.currentWeather.coordinates,
                        settings = _settingsFlow.first()
                    )
                    processResult(result, currentState)
                }
            }
        }
    }

    private suspend fun getWeatherByCity(city: City, settings: Settings): WeatherResult<Weather> {
        return weatherRepository.getWeather(
            city = city,
            units = settings.units,
            language = settings.language
        )
    }

    private suspend fun getWeatherByCoordinates(
        coordinates: Coordinates,
        settings: Settings
    ): WeatherResult<Weather> {
        return weatherRepository.getWeather(
            coordinates = coordinates,
            units = settings.units,
            language = settings.language
        )
    }

    private fun processResult(result: WeatherResult<Weather>, weatherViewState: WeatherViewState) {
        when (result) {
            is WeatherResult.Success -> {
                val weatherData = result.data
                _state.value = WeatherViewState.Display(
                    searchInput = weatherViewState.searchInput,
                    isLoading = false,
                    searchError = false,
                    errorMessageId = null,
                    weatherUiState = WeatherUiState(
                        weather = weatherData,
                        isFavorite = settings.checkToFavorite(weatherData.currentWeather.id)
                    )
                )
            }

            is WeatherResult.Error -> {

                _state.value = WeatherViewState.Loading(
                    searchInput = weatherViewState.searchInput,
                    isLoading = false,
                    searchError = result.errorType == ErrorType.WRONG_CITY,
                    errorMessageId = result.errorType.toResourceId()
                )
            }
        }
    }
}

sealed interface WeatherViewState {

    val searchInput: String
    val isLoading: Boolean
    val searchError: Boolean
    val errorMessageId: Int?

    data class Loading(
        override val searchInput: String = "",
        override val isLoading: Boolean = true,
        override val searchError: Boolean = false,
        override val errorMessageId: Int? = null
    ) : WeatherViewState

    data class Display(
        override val searchInput: String,
        override val isLoading: Boolean,
        override val searchError: Boolean,
        override val errorMessageId: Int?,
        val weatherUiState: WeatherUiState,
    ) : WeatherViewState
}

data class WeatherUiState(
    val weather: Weather,
    val isFavorite: Boolean
)