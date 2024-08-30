# Weather Forecast Compose App

<img src="screenshots/app_icon.png"  alt="app icon"/>

Weather Forecast Compose is a simple weather and air pollution forecast application that uses some
APIs to get 3 day / 3 hour forecast data from [OpenWeatherMap](https://openweathermap.org/api).
More info on how to make an api call [here](https://openweathermap.org/api/one-call-3#multi).

### Pre-requisite 📝

You need to create `apikeys.properties` and add your Open Weather API key.

```properties
API_KEY=YOUR KEY
```

Check for one under  [`Api Keys`](https://home.openweathermap.org/api_keys)

*Environment*

- Built on Android Studio Koala+

### Architecture 📐

* MVI
* [Kotlin](https://kotlinlang.org/)
* [Compose](https://developer.android.com/compose)
* [Coroutines](https://github.com/Kotlin/kotlinx.coroutines)
* [Flow](https://kotlinlang.org/docs/flow.html)

### Preview 📷

|                                 Light mode                                  |                                Dark mode                                 |
|:---------------------------------------------------------------------------:|:------------------------------------------------------------------------:|
|   ![Weather Light](screenshots/Screenshot_weather_light.png "Home Light")   |   ![Weather Dark](screenshots/Screenshot_weather_dark.png "Home Dark")   |
| ![Favorites Light](screenshots/Screenshot_favorites_light.png "Home Light") | ![Favorites Dark](screenshots/Screenshot_favorites_dark.png "Home Dark") |
|  ![Settings Light](screenshots/Screenshot_settings_light.png "Home Light")  |  ![Settings Dark](screenshots/Screenshot_settings_dark.png "Home Dark")  |
|    ![Search Light](screenshots/Screenshot_search_light.png "Home Light")    |    ![Search Dark](screenshots/Screenshot_search_dark.png "Home Dark")    |

### Features 📱

<p align="center">
<img src="screenshots/Screen_recording_weather.webm" width="32%" alt="weather"/>
<img src="screenshots/Screen_recording_settings.webm" width="32%" alt="favorites"/>
<img src="screenshots/Screen_recording_search_errors.webm" width="32%" alt="settings"/>
</p>

### Libraries and tools 🔨

* ViewModel
* Datastore
* RoomDB
* Hilt
* Retrofit
* OkHttp
* Moshi