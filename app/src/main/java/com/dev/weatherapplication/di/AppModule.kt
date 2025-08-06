package com.dev.weatherapplication.di

import android.content.Context
import androidx.room.Room
import com.dev.weatherapplication.data.WeatherDao
import com.dev.weatherapplication.data.WeatherDatabase
import com.dev.weatherapplication.network.CurrentWeatherApi
import com.dev.weatherapplication.network.WeatherApi
import com.dev.weatherapplication.utils.Constants.BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun providesWeatherDao(weatherDatabase: WeatherDatabase) : WeatherDao
    = weatherDatabase.weatherDao()

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context) : WeatherDatabase
    = Room.databaseBuilder(
        context,
        WeatherDatabase::class.java,
        "weather_database")
        .fallbackToDestructiveMigration(false)
        .build()

    @Provides
    @Singleton
    fun provideOpenWeatherApi() : WeatherApi{
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(WeatherApi::class.java)
    }
    @Provides
    @Singleton
    fun provideCurrentWeatherApi() : CurrentWeatherApi{
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(CurrentWeatherApi::class.java)
    }
}