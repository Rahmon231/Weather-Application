package com.dev.weatherapplication.model

data class Weather(
    val city: City,
    val cnt: Int,
    val cod: String,
    val list: List<Item8>,
    val message: Int
)