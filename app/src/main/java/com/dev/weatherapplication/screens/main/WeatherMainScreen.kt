package com.dev.weatherapplication.screens.main

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.produceState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil3.compose.rememberAsyncImagePainter
import com.dev.weatherapplication.R
import com.dev.weatherapplication.data.CurrentWeatherState
import com.dev.weatherapplication.data.WeatherState
import com.dev.weatherapplication.model.CurrentWeather
import com.dev.weatherapplication.model.Weather
import com.dev.weatherapplication.model.WeatherItem
import com.dev.weatherapplication.utils.formatDate
import com.dev.weatherapplication.utils.formatDateTime
import com.dev.weatherapplication.utils.formatDecimals
import com.dev.weatherapplication.widgets.HumidityWindPressureRow
import com.dev.weatherapplication.widgets.SunsetSunriseRow
import com.dev.weatherapplication.widgets.WeatherAppBar
import com.dev.weatherapplication.widgets.WeatherDetailRow
import com.dev.weatherapplication.widgets.WeatherStateImage

@Composable
fun WeatherMainScreen (navController: NavHostController,
                       mainViewModel: MainViewModel = hiltViewModel()) {
    val weatherData = produceState<WeatherState<Weather>>(
        initialValue = WeatherState.Loading) {
        value = mainViewModel.getWeather("Derby")
    }.value

    val currentWeatherData = produceState<CurrentWeatherState<CurrentWeather>>(
        initialValue = CurrentWeatherState.Loading) {
        value = mainViewModel.getCurrentWeather("Derby")
    }.value

    when {
        weatherData is WeatherState.Loading || currentWeatherData is CurrentWeatherState.Loading -> {
            CircularProgressIndicator()
        }

        weatherData is WeatherState.Failure -> {
            Text("Forecast failed: ${weatherData.throwable.localizedMessage}")
        }

        currentWeatherData is CurrentWeatherState.Failure -> {
            Text("Current weather failed: ${currentWeatherData.throwable.localizedMessage}")
        }

        weatherData is WeatherState.Success && currentWeatherData is CurrentWeatherState.Success -> {
            MainScaffold(
                weather = weatherData.data,
                currentWeather = currentWeatherData.data,
                navController = navController)
        }
    }

}

@Composable
fun MainScaffold(weather: Weather, currentWeather: CurrentWeather, navController: NavHostController) {

    Scaffold(topBar = {
        WeatherAppBar(
            title = weather.city.name + ", ${weather.city.country}",
            icon = Icons.AutoMirrored.Filled.ArrowBack,
            elevation = 100.dp){
            navController.popBackStack()
        }
    }) {
        Surface(modifier = Modifier.padding(it)){
            MainContent(data = weather, currentData = currentWeather)
        }

    }

}

@Composable
fun MainContent(data: Weather, currentData : CurrentWeather) {
    Column(
        modifier = Modifier
            .padding(4.dp)
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Text(
            text = formatDate(data.list[0].dt),
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.secondary,
            modifier = Modifier.padding(6.dp)
        )

        Surface(
            modifier = Modifier
                .padding(4.dp)
                .size(200.dp),
            shape = CircleShape,
            color = Color(0xFFFFC400)
        ) {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                WeatherStateImage(imageUrl = "https://openweathermap.org/img/wn/${data.list[0].weather[0].icon}.png")

                Text(
                    text = formatDecimals(data.list[0].main.temp) + "°",
                    style = MaterialTheme.typography.displayLarge,
                    fontWeight = FontWeight.ExtraBold
                )

                Text(text = data.list[0].weather[0].main,
                    style = TextStyle(fontStyle = FontStyle.Italic))
            }
        }
        HumidityWindPressureRow(weather = data.list[0])
        HorizontalDivider()
        SunsetSunriseRow(currentWeather = currentData)
        Text(text = "This Week",
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold)
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxSize(),
            color = Color(0xFFEEF1EF),
            shape = RoundedCornerShape(size = 14.dp),
            shadowElevation = 5.dp
        ){
            LazyColumn(modifier = Modifier.padding(2.dp),
                contentPadding = PaddingValues(1.dp)
            ){
              items(items = data.list){ item : WeatherItem ->

                  WeatherDetailRow(weather = item)
              }
            }
        }
    }
}

