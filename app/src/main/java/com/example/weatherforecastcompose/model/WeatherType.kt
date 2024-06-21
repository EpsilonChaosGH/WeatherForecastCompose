package com.example.weatherforecastcompose.model

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.example.weatherforecastcompose.R

enum class WeatherType(@DrawableRes val iconResId: Int, @StringRes val descResId: Int) {
    //Day icons
    IC_01D(R.drawable.ic_weather_day_sunny, R.string.clear_sky),
    IC_02D(R.drawable.ic_weather_day_cloudy_sun, R.string.few_clouds),
    IC_03D(R.drawable.ic_weather_day_cloud, R.string.scattered_clouds),
    IC_04D(R.drawable.ic_weather_day_cloudy, R.string.broken_clouds),
    IC_09D(R.drawable.ic_weather_day_showers, R.string.shower_rain),
    IC_10D(R.drawable.ic_weather_day_rain, R.string.rain),
    IC_11D(R.drawable.ic_weather_day_thunderstorm, R.string.thunderstorm),
    IC_13D(R.drawable.ic_weather_day_snow, R.string.snow),
    IC_50D(R.drawable.ic_weather_day_fog, R.string.mist),

    //Night icons
    IC_01N(R.drawable.ic_weather_night_clear, R.string.clear_sky_night),
    IC_02N(R.drawable.ic_weather_night_cloudy, R.string.few_clouds_night),
    IC_03N(R.drawable.ic_weather_night_cloudy, R.string.scattered_clouds_night),
    IC_04N(R.drawable.ic_weather_night_cloudy, R.string.broken_clouds_night),
    IC_09N(R.drawable.ic_weather_night_showers, R.string.shower_rain_night),
    IC_10N(R.drawable.ic_weather_night_rain, R.string.rain_night),
    IC_11N(R.drawable.ic_weather_night_thunderstorm, R.string.thunderstorm_night),
    IC_13N(R.drawable.ic_weather_night_snow, R.string.snow_night),
    IC_50N(R.drawable.ic_weather_night_fog, R.string.mist_night),

    IC_UNKNOWN(R.drawable.ic_weather_na, R.string.unknown);

    companion object {
        fun find(value: String): WeatherType {
            for (weather in entries) {
                if (weather.name.equals(value, true)) return weather
            }
            return IC_UNKNOWN
        }
    }
}