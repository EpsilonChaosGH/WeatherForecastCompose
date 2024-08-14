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
import com.example.weatherforecastcompose.model.WeatherResult
import com.example.weatherforecastcompose.ui.screens.ActionHandler
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
    private val settings: SettingsRepository,
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

    private val _state: MutableStateFlow<WeatherUiState> =
        MutableStateFlow(WeatherUiState.Loading())

    val state: StateFlow<WeatherUiState> = _state.asStateFlow()

    init {
        _settingsFlow
            .onEach { onAction(WeatherScreenAction.SettingsChanged(it)) }
            .launchIn(viewModelScope)
    }

    override fun onAction(action: WeatherScreenAction) {
        when (val state = _state.value) {
            is WeatherUiState.Loading -> reduce(action = action, state = state)
            is WeatherUiState.Success -> reduce(action = action, state = state)
            is WeatherUiState.Error -> reduce(action = action, state = state)
        }
    }

    private fun reduce(action: WeatherScreenAction, state: WeatherUiState.Loading) {
        when (action) {
            WeatherScreenAction.SearchWeatherByCoordinates -> Unit
            is WeatherScreenAction.SettingsChanged -> getWeatherByCoordinates(action.settings)

            WeatherScreenAction.RefreshScreenState -> {
                _state.value = state.copy(isRefreshing = true)
                viewModelScope.launch { getWeatherByCoordinates(_settingsFlow.first()) }
            }

            is WeatherScreenAction.AddToFavorites -> Unit
            is WeatherScreenAction.RemoveFromFavorites -> Unit
            WeatherScreenAction.PermissionsDenied -> TODO()
        }
    }

    private fun reduce(action: WeatherScreenAction, state: WeatherUiState.Success) {
        when (action) {
            WeatherScreenAction.SearchWeatherByCoordinates -> Unit

            is WeatherScreenAction.SettingsChanged -> {
                _state.value = WeatherUiState.Success(
                    isRefreshing = false,
                    data = state.data.copy(isLoading = true)
                )
                getWeatherByCoordinates(action.settings)
            }

            WeatherScreenAction.RefreshScreenState -> {
                _state.value = state.copy(isRefreshing = true)
                viewModelScope.launch { getWeatherByCoordinates(_settingsFlow.first()) }
            }

            is WeatherScreenAction.AddToFavorites -> addToFavorite(action.favoritesCoordinates)
            is WeatherScreenAction.RemoveFromFavorites -> removeFromFavorite(action.id)
            WeatherScreenAction.PermissionsDenied -> TODO()
        }
    }

    private fun reduce(action: WeatherScreenAction, state: WeatherUiState.Error) {
        when (action) {
            WeatherScreenAction.SearchWeatherByCoordinates -> Unit
            is WeatherScreenAction.SettingsChanged -> getWeatherByCoordinates(action.settings)
            WeatherScreenAction.RefreshScreenState -> {
                _state.value = state.copy(isRefreshing = true)
                viewModelScope.launch { getWeatherByCoordinates(_settingsFlow.first()) }
            }

            is WeatherScreenAction.AddToFavorites -> Unit
            is WeatherScreenAction.RemoveFromFavorites -> Unit
            WeatherScreenAction.PermissionsDenied -> TODO()
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

//    private fun getWeatherByCity() {
//        viewModelScope.launch {
//            if (state.value.searchInput != "") {
//                val result = weatherRepository.getCoordinatesByCity(City(state.value.searchInput))
//
//                when (result) {
//                    is WeatherResult.Success -> settings.setCoordinates(result.data)
//                    is WeatherResult.Error -> {
//                        _state.update {
//                            it.copy(
//                                isLoading = false,
//                                searchError = result.errorType == ErrorType.WRONG_CITY,
//                                errorMessageResId = result.errorType.toResourceId(),
//                            )
//                        }
//                    }
//                }
//            } else {
//                _state.update {
//                    it.copy(
//                        searchError = true,
//                        errorMessageResId = R.string.error_empty_city
//                    )
//                }
//            }
//        }
//    }

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

//    private fun refreshWeather() {
//        viewModelScope.launch {
//            _state.update { it.copy(isRefreshing = true) }
//            getWeatherByCoordinates(_settingsFlow.first())
//        }
//    }

    private fun processResult(result: WeatherResult<Weather>) {
        viewModelScope.launch {
            when (result) {
                is WeatherResult.Success -> {
                    val weatherData = result.data
                    _state.value = WeatherUiState.Success(
                        data = WeatherViewState(
                            isLoading = false,
                            weather = weatherData,
                            isFavorite = favorites.checkForFavorite(weatherData.currentWeather.id)
                        )
                    )
                }

                is WeatherResult.Error -> {
                    _state.value = WeatherUiState.Error(
                        errorMessageResId = result.errorType.toResourceId()
                    )
                }
            }
        }
    }
}

data class WeatherViewState(
//    val searchInput: String,
//    val searchError: Boolean,
    val isLoading: Boolean,
//    val isRefreshing: Boolean,
//    val errorMessageResId: Int?,
    val weather: Weather,
    val isFavorite: Boolean
)

//data class WeatherUiState1(
//    val weather: Weather,
//    val isFavorite: Boolean
//)

sealed interface WeatherUiState {
    var isRefreshing: Boolean

    data class Loading(override var isRefreshing: Boolean = false) : WeatherUiState

    data class Success(
        override var isRefreshing: Boolean = false,
        val data: WeatherViewState
    ) : WeatherUiState

    data class Error(
        override var isRefreshing: Boolean = false, val errorMessageResId: Int
    ) : WeatherUiState
}

//sealed class SearchState {
//
//    val inputValue: String = ""
//
//    data object Success : SearchState()
//
//    data class Error(val errorMessageResId: Int) : SearchState()
//}