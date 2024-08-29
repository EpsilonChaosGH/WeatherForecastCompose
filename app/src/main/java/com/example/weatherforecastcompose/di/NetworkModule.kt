package com.example.weatherforecastcompose.di

import android.content.Context
import com.example.weatherforecastcompose.BuildConfig
import com.example.weatherforecastcompose.data.network.ConnectivityManagerNetworkMonitor
import com.example.weatherforecastcompose.data.network.NetworkMonitor
import com.example.weatherforecastcompose.data.network.services.AirService
import com.example.weatherforecastcompose.data.network.services.ForecastService
import com.example.weatherforecastcompose.data.network.services.GeocodingService
import com.example.weatherforecastcompose.data.network.services.WeatherService
import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun providesServiceGeocoding(retrofit: Retrofit): GeocodingService {
        return retrofit.create(GeocodingService::class.java)
    }

    @Provides
    @Singleton
    fun providesServiceWeather(retrofit: Retrofit): WeatherService {
        return retrofit.create(WeatherService::class.java)
    }

    @Provides
    @Singleton
    fun providesServiceForecast(retrofit: Retrofit): ForecastService {
        return retrofit.create(ForecastService::class.java)
    }

    @Provides
    @Singleton
    fun providesServiceAir(retrofit: Retrofit): AirService {
        return retrofit.create(AirService::class.java)
    }

    @Provides
    @Singleton
    fun provideInterceptor(): Interceptor {
        return Interceptor { chain ->
            val request = chain.request()
            val url =
                request.url.newBuilder().addQueryParameter("appid", BuildConfig.API_KEY).build()
            chain.proceed(request.newBuilder().url(url).build())
        }
    }

    @Provides
    @Singleton
    fun provideMoshi(): Moshi {
        return Moshi.Builder().build()
    }

    @Provides
    @Singleton
    fun provideClient(interceptor: Interceptor): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(client: OkHttpClient, moshi: Moshi): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://api.openweathermap.org/")
            .client(client)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
    }

    @Provides
    fun provideNetworkMonitor(@ApplicationContext context: Context): NetworkMonitor {
        return ConnectivityManagerNetworkMonitor(context = context)
    }
}