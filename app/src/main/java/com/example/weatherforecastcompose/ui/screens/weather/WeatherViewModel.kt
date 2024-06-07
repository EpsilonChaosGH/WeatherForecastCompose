package com.example.weatherforecastcompose.ui.screens.weather

import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherforecastcompose.Const
import com.example.weatherforecastcompose.data.network.WeatherRepository
import com.example.weatherforecastcompose.mappers.toResourceId
import com.example.weatherforecastcompose.model.AirQuality
import com.example.weatherforecastcompose.model.Air
import com.example.weatherforecastcompose.model.City
import com.example.weatherforecastcompose.model.Coordinates
import com.example.weatherforecastcompose.model.Forecast
import com.example.weatherforecastcompose.model.CurrentWeather
import com.example.weatherforecastcompose.model.Settings
import com.example.weatherforecastcompose.model.SupportedLanguage
import com.example.weatherforecastcompose.model.Units
import com.example.weatherforecastcompose.model.Weather
import com.example.weatherforecastcompose.model.WeatherResult
import com.example.weatherforecastcompose.model.WeatherType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel @Inject constructor(
    private val weatherRepository: WeatherRepository
) : ViewModel() {

    private val _language = MutableStateFlow(SupportedLanguage.RUSSIAN)
    private val _units = MutableStateFlow(Units.STANDARD)

    private val _state = MutableStateFlow(WeatherUiState(isLoading = true))
    val state: StateFlow<WeatherUiState> = _state.asStateFlow()

    init {
        viewModelScope.launch {
            combine(
                _language,
                _units,
            ) { language, units ->
                Settings(units, language)
            }.collect { (units, language) ->
                setState {
                    copy(
                        language = language,
                        units = units,
                        defaultLocation = defaultLocation
                    )
                }.also { processIntent(WeatherScreenIntent.LoadWeatherScreenData) }
            }
        }
    }

    fun processIntent(intent: WeatherScreenIntent) {
        when (intent) {
            is WeatherScreenIntent.LoadWeatherScreenData -> {
                viewModelScope.launch {
                    val result = weatherRepository.getWeather(
                        coordinates = state.value.defaultLocation,
                        language = state.value.language.languageValue,
                        units = state.value.units.unitsValue
                    )
                    processResult(result)
                }
            }

            is WeatherScreenIntent.SearchWeatherByCity -> {
                viewModelScope.launch {
                    val result = weatherRepository.getWeather(
                        city = City(state.value.searchInput),
                        language = state.value.language.languageValue,
                        units = state.value.units.unitsValue
                    )
                    processResult(result)
                }
            }

            is WeatherScreenIntent.SearchWeatherByCoordinates -> {
                viewModelScope.launch {
                    val result = weatherRepository.getWeather(
                        coordinates = intent.coordinates,
                        language = state.value.language.languageValue,
                        units = state.value.units.unitsValue
                    )
                    processResult(result)
                }
            }

            is WeatherScreenIntent.SearchInputChanged -> {
                setState { copy(searchInput = intent.value) }
            }

            is WeatherScreenIntent.ChangeFavorite -> {
                setState { copy(isFavorite = !_state.value.isFavorite) }
            }
        }
    }

    private fun processResult(result: WeatherResult<Weather>) {
        when (result) {
            is WeatherResult.Success -> {
                val weatherData = result.data
                setState {
                    copy(
                        weather = weatherData,
                        isLoading = false,
                        errorMessageId = null
                    )
                }
            }

            is WeatherResult.Error -> {
                setState {
                    copy(
                        isLoading = false,
                        errorMessageId = result.errorType.toResourceId()
                    )
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
    val weather: Weather = Weather.defaultEmptyWeather(),
    val units: Units = Units.METRIC,
    val defaultLocation: Coordinates = Coordinates(Const.DEFAULT_LON, Const.DEFAULT_LAT),
    val language: SupportedLanguage = SupportedLanguage.ENGLISH,
    val searchInput: String = "",
    val isFavorite: Boolean = false,
    val isLoading: Boolean = false,
    @StringRes val errorMessageId: Int? = null
)