package com.dev.weatherapplication.data

sealed class WeatherState<out T> {
    data object Loading : WeatherState<Nothing>()
    data class Success<T>(val data: T) : WeatherState<T>()
    data class Failure(val throwable: Throwable) : WeatherState<Nothing>()
}