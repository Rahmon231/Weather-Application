package com.dev.weatherapplication.screens.main

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.dev.weatherapplication.data.WeatherState
import com.dev.weatherapplication.model.Weather
import com.dev.weatherapplication.repository.WeatherRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val weatherRepository: WeatherRepository)
    : ViewModel() {

    val weatherState = mutableStateOf<WeatherState<Weather>>(WeatherState.Loading)

}