package com.dev.weatherapplication.screens.main

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.dev.weatherapplication.data.CurrentWeatherState
import com.dev.weatherapplication.data.WeatherState
import com.dev.weatherapplication.model.CurrentWeather
import com.dev.weatherapplication.model.Weather
import com.dev.weatherapplication.model.WeatherItem
import com.dev.weatherapplication.navigation.WeatherScreens
import com.dev.weatherapplication.screens.settings.SettingsViewModel
import com.dev.weatherapplication.utils.formatDate
import com.dev.weatherapplication.utils.formatDecimals
import com.dev.weatherapplication.widgets.HumidityWindPressureRow
import com.dev.weatherapplication.widgets.SunsetSunriseRow
import com.dev.weatherapplication.widgets.WeatherAppBar
import com.dev.weatherapplication.widgets.WeatherDetailRow
import com.dev.weatherapplication.widgets.WeatherStateImage

@Composable
fun WeatherMainScreen (
    navController: NavController,
    mainViewModel: MainViewModel = hiltViewModel(),
    city: String?,
    settingsViewModel: SettingsViewModel = hiltViewModel()
) {

    val unitFromDb = settingsViewModel.unitList.collectAsState().value

    var unit by remember {
        mutableStateOf("imperial")
    }
    var isImperial by remember {
        mutableStateOf(false)
    }


    if (unitFromDb.isNotEmpty()){
        unit = unitFromDb[0].unit.split(" ")[0].lowercase()

        isImperial = unit == "imperial"
        Log.d("MainScreenUnit", "WeatherMainScreen: $unit")
        val weatherData = produceState<WeatherState<Weather>>(
            initialValue = WeatherState.Loading) {
            value = mainViewModel.getWeather(city.toString(), units = unit)
        }.value

        val currentWeatherData = produceState<CurrentWeatherState<CurrentWeather>>(
            initialValue = CurrentWeatherState.Loading) {
            value = mainViewModel.getCurrentWeather(city.toString(), units = unit)
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
                    navController = navController,
                    isImperial = isImperial)
            }
        }
    }



}

@Composable
fun MainScaffold(
    weather: Weather,
    currentWeather: CurrentWeather,
    navController: NavController,
    isImperial: Boolean
) {

    Scaffold(topBar = {
        WeatherAppBar(
            title = weather.city.name + ", ${weather.city.country}",
            elevation = 100.dp,
            navController =  navController,
            onAddActionClicked = {
                navController.navigate(WeatherScreens.SearchScreen.name)
            }){
           //OnBackArrowClicked or favourite impl
        }
    }) {
        Surface(modifier = Modifier.padding(it)){
            MainContent(data = weather, currentData = currentWeather, isImperial = isImperial)
        }

    }

}

@Composable
fun MainContent(data: Weather, currentData: CurrentWeather, isImperial: Boolean) {
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
        HumidityWindPressureRow(weather = data.list[0], isImperial = isImperial)
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

