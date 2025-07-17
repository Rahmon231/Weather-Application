package com.dev.weatherapplication.widgets

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import coil3.compose.rememberAsyncImagePainter
import com.dev.weatherapplication.R
import com.dev.weatherapplication.model.CurrentWeather
import com.dev.weatherapplication.model.WeatherItem
import com.dev.weatherapplication.utils.formatDate
import com.dev.weatherapplication.utils.formatDateTime
import com.dev.weatherapplication.utils.formatDecimals

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
            Text(text = buildAnnotatedString {
                withStyle(style = SpanStyle(
                    color = Color.Blue.copy(alpha = 0.7f),
                    fontWeight = FontWeight.SemiBold)
                ){
                    append(formatDecimals(weather.main.temp_max) + "°")
                }
                withStyle(style = SpanStyle(
                    color = Color.LightGray)
                ){
                    append(formatDecimals(weather.main.temp_min) + "°")

                }
            })
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
