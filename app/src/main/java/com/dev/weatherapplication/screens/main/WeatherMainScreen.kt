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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
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
import com.dev.weatherapplication.widgets.WeatherAppBar

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

@Composable
fun WeatherDetailRow(weather: WeatherItem) {
    val imageUrl = "https://openweathermap.org/img/wn/${weather.weather[0].icon}.png"
    Surface(
        modifier = Modifier
            .padding(3.dp)
            .fillMaxWidth(),
        shape = CircleShape.copy(topEnd = CornerSize(6.dp)),
        color = Color.White
    ) {
        Row(modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween) {
            Text(
                formatDate(weather.dt)
                    .split(",")[0]+", ${formatDateTime(weather.dt)}"
                        ,
                modifier = Modifier.padding(start = 5.dp)
            )
            WeatherStateImage(imageUrl = imageUrl)
            Surface(modifier = Modifier.padding(0.dp),
                shape = CircleShape,
                color = Color(0xFFFFC400)
            ) {
                Text(weather.weather[0].description,
                    modifier = Modifier.padding(4.dp),
                    style = MaterialTheme.typography.labelSmall)
            }
        }

    }
}

@Composable
fun SunsetSunriseRow(currentWeather: CurrentWeather) {
    Row(
        modifier = Modifier.padding(top = 15.dp, bottom = 6.dp).fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ){
        Row(modifier = Modifier.padding(4.dp)){
            Icon(
                painter = painterResource(R.drawable.sunrise),
                contentDescription = "sunrise icon",
                modifier = Modifier.size(30.dp)
            )
            Text(text = formatDateTime(currentWeather.sys.sunrise),
                style = MaterialTheme.typography.labelSmall)
        }
        Row(){
            Text(text = "${formatDateTime(currentWeather.sys.sunset)} ",
                style = MaterialTheme.typography.labelSmall)
            Icon(
                painter = painterResource(R.drawable.sunset),
                contentDescription = "sunset icon",
                modifier = Modifier.size(30.dp)
            )

        }
    }
}

@Composable
fun HumidityWindPressureRow(weather: WeatherItem) {
    Row(
        modifier = Modifier.padding(12.dp).fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ){
        Row(modifier = Modifier.padding(4.dp)){
            Icon(
                painter = painterResource(R.drawable.humidity),
                contentDescription = "humidity icon",
                modifier = Modifier.size(20.dp)
            )
            Text(text = "${weather.main.humidity}%",
                style = MaterialTheme.typography.labelSmall)
        }
        Row(){
            Icon(
                painter = painterResource(R.drawable.pressure),
                contentDescription = "pressure icon",
                modifier = Modifier.size(20.dp)
            )
            Text(text = "${weather.main.pressure} psi",
                style = MaterialTheme.typography.labelSmall)
        }
        Row(){
            Icon(
                painter = painterResource(R.drawable.wind),
                contentDescription = "wind icon",
                modifier = Modifier.size(20.dp)
            )
            Text(text = "${weather.wind.speed} mph",
                style = MaterialTheme.typography.labelSmall)
        }

    }
}

@Composable
fun WeatherStateImage(imageUrl: String) {
    Image(painter = rememberAsyncImagePainter(imageUrl),
        contentDescription = "weather icon",
        modifier = Modifier.size(80.dp))
}
