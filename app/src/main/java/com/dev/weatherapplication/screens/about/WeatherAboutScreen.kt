package com.dev.weatherapplication.screens.about

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.NavHostController
import com.dev.weatherapplication.R
import com.dev.weatherapplication.widgets.WeatherAppBar

@Composable
fun WeatherAboutScreen(navController: NavHostController) {
    Scaffold(
        topBar = {
            WeatherAppBar(
                title = "About",
                icon = Icons.AutoMirrored.Filled.ArrowBack,
                isMainScreen = false,
                navController = navController
            ) {
                navController.popBackStack()
            }
        }
    ){
        Surface(modifier = Modifier.padding(it).fillMaxSize()) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,

                ) {
                Text(text = stringResource(R.string.about_info),
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold)

                Text(text = stringResource(R.string.api_used),
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Light)
            }

        }
    }
}