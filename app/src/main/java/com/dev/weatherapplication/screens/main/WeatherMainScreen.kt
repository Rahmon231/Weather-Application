package com.dev.weatherapplication.screens.main


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
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
import com.dev.weatherapplication.screens.settings.SettingsViewModel
import com.dev.weatherapplication.utils.formatDate
import com.dev.weatherapplication.utils.formatDecimals
import com.dev.weatherapplication.widgets.*

@Composable
fun WeatherMainScreen(
    navController: NavController,
    mainViewModel: MainViewModel = hiltViewModel(),
    city: String?,
    settingsViewModel: SettingsViewModel = hiltViewModel()
) {

    val unitFromDb = settingsViewModel.unitList.collectAsState().value

    var unit by remember { mutableStateOf("imperial") }
    var isImperial by remember { mutableStateOf(false) }

    if (unitFromDb.isNotEmpty()) {
        unit = unitFromDb[0].unit.split(" ")[0].lowercase()
        isImperial = unit == "imperial"

        val weatherData = produceState<WeatherState<Weather>>(
            initialValue = WeatherState.Loading
        ) { value = mainViewModel.getWeather(city.toString(), units = unit) }.value

        val currentWeatherData = produceState<CurrentWeatherState<CurrentWeather>>(
            initialValue = CurrentWeatherState.Loading
        ) { value = mainViewModel.getCurrentWeather(city.toString(), units = unit) }.value

        when {
            weatherData is WeatherState.Loading || currentWeatherData is CurrentWeatherState.Loading -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
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
                    isImperial = isImperial
                )
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
    Scaffold(
        topBar = {
            WeatherAppBar(
                title = weather.city.name + ", ${weather.city.country}",
                elevation = 10.dp,
                navController = navController,
                onAddActionClicked = { navController.navigate("SearchScreen") }
            )
        }
    ) { padding ->
        Surface(modifier = Modifier.padding(padding)) {
            MainContent(data = weather, currentData = currentWeather, isImperial = isImperial)
        }
    }
}

@Composable
fun MainContent(data: Weather, currentData: CurrentWeather, isImperial: Boolean) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Date
        Text(
            text = formatDate(data.list[0].dt),
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.primary
            ),
            modifier = Modifier.padding(top = 4.dp, bottom = 8.dp)
        )

        // Current Weather Card with Gradient
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp), // reduced height
            shape = RoundedCornerShape(20.dp),
            elevation = CardDefaults.cardElevation(8.dp),
            colors = CardDefaults.cardColors(containerColor = Color.Transparent)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(Color(0xFF64B5F6), Color(0xFF1976D2))
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    WeatherStateImage(
                        imageUrl = "https://openweathermap.org/img/wn/${data.list[0].weather[0].icon}.png"
                    )
                    Text(
                        text = formatDecimals(data.list[0].main.temp) + "°",
                        style = MaterialTheme.typography.displaySmall.copy(
                            fontWeight = FontWeight.ExtraBold,
                            color = Color.White
                        )
                    )
                    Text(
                        text = data.list[0].weather[0].main,
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = Color.White,
                            fontStyle = FontStyle.Italic
                        )
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Humidity, Wind, Pressure Row
        HumidityWindPressureRow(weather = data.list[0], isImperial = isImperial)

        Spacer(modifier = Modifier.height(6.dp))

        // Sunrise & Sunset Row
        SunsetSunriseRow(currentWeather = currentData)

        Spacer(modifier = Modifier.height(10.dp))

        // Weekly Forecast Title
        Text(
            text = "Weekly Forecast",
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.secondary
            ),
            modifier = Modifier.padding(vertical = 6.dp)
        )

        // Weekly Forecast List
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f), // ensures list takes remaining space
            contentPadding = PaddingValues(vertical = 4.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(data.list) { item ->
                ForecastCard(item)
            }
        }
    }
}

@Composable
fun ForecastCard(item: WeatherItem) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(70.dp),
        shape = RoundedCornerShape(14.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFE3F2FD))
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = formatDate(item.dt),
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium)
            )
            WeatherStateImage(
                imageUrl = "https://openweathermap.org/img/wn/${item.weather[0].icon}.png"
            )
            Column(
                horizontalAlignment = Alignment.End
            ) {
                Text(
                    text = "Max: ${formatDecimals(item.main.temp_max)}°",
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = "Min: ${formatDecimals(item.main.temp_min)}°",
                    style = MaterialTheme.typography.bodySmall.copy(color = Color.Gray)
                )
            }
        }
    }
}
