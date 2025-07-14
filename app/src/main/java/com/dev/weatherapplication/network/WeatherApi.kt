package com.dev.weatherapplication.network

import com.dev.weatherapplication.model.Weather
import com.dev.weatherapplication.utils.Constants.API_KEY
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import javax.inject.Singleton

@Singleton
interface WeatherApi {
    @GET(value = "data/2.5/forecast")
    suspend fun getWeather(
        @Query("q") query : String,
        @Query("appid") appid: String = API_KEY,
        @Query("units") units: String = "imperial"
    ) : Response<Weather>
}