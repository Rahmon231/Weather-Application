package com.dev.weatherapplication.screens.main

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dev.weatherapplication.data.WeatherState
import com.dev.weatherapplication.model.Weather
import com.dev.weatherapplication.repository.WeatherRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val weatherRepository: WeatherRepository)
    : ViewModel() {


    suspend fun getWeather(query: String): WeatherState<Weather> {
        return weatherRepository.getWeather(query)
    }
}