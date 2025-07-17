package com.dev.weatherapplication.network

import com.dev.weatherapplication.model.CurrentWeather
import com.dev.weatherapplication.utils.Constants.API_KEY
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import javax.inject.Singleton

@Singleton
interface CurrentWeatherApi {

    @GET("data/2.5/weather")
    suspend fun getCurrentWeather(
        @Query("q") query: String,
        @Query("appid") appid: String = API_KEY,
        @Query("units") units: String = "imperial"
    ): Response<CurrentWeather>
}