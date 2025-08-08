package com.dev.weatherapplication.screens.main

import androidx.lifecycle.ViewModel
import com.dev.weatherapplication.data.CurrentWeatherState
import com.dev.weatherapplication.data.WeatherState
import com.dev.weatherapplication.model.CurrentWeather
import com.dev.weatherapplication.model.Weather
import com.dev.weatherapplication.repository.WeatherRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val weatherRepository: WeatherRepository)
    : ViewModel() {


    suspend fun getWeather(query: String, units: String): WeatherState<Weather> {
        return weatherRepository.getWeather(query, units = units)
    }
    suspend fun getCurrentWeather(query: String, units: String): CurrentWeatherState<CurrentWeather> {
        return weatherRepository.getCurrentWeather(query, units = units)
    }
}