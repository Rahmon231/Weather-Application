package com.dev.weatherapplication.screens.main

import android.util.Log
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.produceState
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.dev.weatherapplication.data.WeatherState
import com.dev.weatherapplication.model.Weather

@Composable
fun WeatherMainScreen (navController: NavHostController,
                       mainViewModel: MainViewModel = hiltViewModel()) {
    ShowData(mainViewModel)
}

@Composable
fun ShowData(mainViewModel: MainViewModel) {

    val weatherData = produceState<WeatherState<Weather>>(
        initialValue = WeatherState.Loading) {
        value = mainViewModel.getWeather("")
    }.value

    when (weatherData) {
        is WeatherState.Failure -> Text("Failed to load: ${weatherData.throwable.localizedMessage}")
        is WeatherState.Loading -> CircularProgressIndicator()
        is WeatherState.Success -> {
            Text(text = weatherData.data.city.country.toString())
        }
    }
}