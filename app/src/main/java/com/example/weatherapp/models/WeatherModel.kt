package com.example.weatherapp.models

import java.util.Date

data class WeatherModel(
    val temperature: Float,
    val date: Date,
    val description: String,
    val icon: String
)