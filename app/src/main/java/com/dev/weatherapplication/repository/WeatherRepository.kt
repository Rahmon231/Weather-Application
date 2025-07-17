package com.dev.weatherapplication.repository

import android.util.Log
import com.dev.weatherapplication.data.CurrentWeatherState
import com.dev.weatherapplication.data.WeatherState
import com.dev.weatherapplication.model.CurrentWeather
import com.dev.weatherapplication.model.Weather
import com.dev.weatherapplication.network.CurrentWeatherApi
import com.dev.weatherapplication.network.WeatherApi
import javax.inject.Inject

class WeatherRepository @Inject constructor(
    private val weatherApi: WeatherApi,
    private val currentWeatherApi: CurrentWeatherApi
) {
    suspend fun getWeather(query: String) : WeatherState<Weather>{
        return try {
            val response = weatherApi.getWeather(query)
            if (response.isSuccessful) {
                WeatherState.Success(response.body()!!)
            } else {
                WeatherState.Failure(Exception("Error: ${response.code()} ${response.message()}"))
            }
        } catch (e: Exception) {
            WeatherState.Failure(e)
        }
    }
    suspend fun getCurrentWeather(query: String) : CurrentWeatherState<CurrentWeather>{
        return try {
            val response = currentWeatherApi.getCurrentWeather(query)
            if (response.isSuccessful) {
                CurrentWeatherState.Success(response.body()!!)
            } else {
                CurrentWeatherState.Failure(Exception("Error: ${response.code()} ${response.message()}"))
            }
        } catch (e: Exception) {
            CurrentWeatherState.Failure(e)
        }
    }
}