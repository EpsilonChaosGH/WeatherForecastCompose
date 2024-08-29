# Weather Forecast Compose App


Weather Forecast Compose is a simple weather and air pollution forecast application that uses some APIs to get 3 day / 3 hour forecast data from [OpenWeatherMap](https://openweathermap.org/api). 
More info on how to make an api call [here](https://openweathermap.org/api/one-call-3#multi).

### Pre-requisite 📝

You need to create `apikeys.properties` and add your Open Weather API key.

```properties
API_KEY = YOUR KEY
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


### Preview 📱
<p align="center">
<img src="screenshots/Vokoscreen_app.gif" width="32%"/>
<img src="screenshots/Screenshot_weather.png" width="32%"/>
<img src="screenshots/Screenshot_favorites.png" width="32%"/>/>
</p>

### Libraries and tools 🔨

* ViewModel
* Datastore
* RoomDB
* Hilt
* Retrofit
* OkHttp
* Moshi