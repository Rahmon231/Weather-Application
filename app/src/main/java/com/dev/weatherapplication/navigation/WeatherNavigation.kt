package com.dev.weatherapplication.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.dev.weatherapplication.screens.main.WeatherMainScreen
import com.dev.weatherapplication.screens.splash.WeatherSplashScreen

@Composable
fun WeatherNavigation() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = WeatherScreens.SplashScreen.name){
        composable(WeatherScreens.SplashScreen.name){
           WeatherSplashScreen(navController = navController)
        }
        composable(WeatherScreens.MainScreen.name){
            WeatherMainScreen(navController = navController)
        }
    }
}


