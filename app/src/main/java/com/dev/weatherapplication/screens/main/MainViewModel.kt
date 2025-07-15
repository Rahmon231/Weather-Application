package com.dev.weatherapplication.screens.main

import androidx.lifecycle.ViewModel
import com.dev.weatherapplication.repository.WeatherRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val weatherRepository: WeatherRepository) : ViewModel() {


}