package com.dev.weatherapplication.screens.settings

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.dev.weatherapplication.widgets.WeatherAppBar

@Composable
fun WeatherSettingsScreen(navController: NavHostController,
                          settingsViewModel: SettingsViewModel = hiltViewModel()
) {
    Scaffold(
        topBar = {
            WeatherAppBar(
                title = "Settings",
                navController = navController,
                icon = Icons.AutoMirrored.Filled.ArrowBack,
                isMainScreen = false
            ){
                navController.popBackStack()
            }
        }
    ) {
        Surface(modifier = Modifier.padding(it)) {

    }
    }
}