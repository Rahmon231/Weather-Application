package com.dev.weatherapplication.data

sealed class CurrentWeatherState<out T> {
    data object Loading : CurrentWeatherState<Nothing>()
    data class Success<T>(val data: T) : CurrentWeatherState<T>()
    data class Failure(val throwable: Throwable) : CurrentWeatherState<Nothing>()
}