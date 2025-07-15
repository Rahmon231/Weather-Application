package com.dev.weatherapplication.repository

import com.dev.weatherapplication.data.WeatherState
import com.dev.weatherapplication.model.Weather
import com.dev.weatherapplication.network.WeatherApi
import javax.inject.Inject

class WeatherRepository @Inject constructor(private val weatherApi: WeatherApi) {
    suspend fun getWeather(query: String) : WeatherState<Weather>{
        return try {
            val response = weatherApi.getWeather(query)
            if (response.isSuccessful) {
                WeatherState.Success(response.body()!!)
            } else {
                WeatherState.Failure(Exception("Failed to fetch weather data"))
            }
        } catch (e: Exception) {
            WeatherState.Failure(e)
        }
    }
}