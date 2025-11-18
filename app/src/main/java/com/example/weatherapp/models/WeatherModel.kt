package com.example.weatherapp.models

import androidx.annotation.DrawableRes
import java.util.Date

data class WeatherModel(
    val temperature: Float,
    val date: Date,
    val description: String,
    @get:DrawableRes val icon: Int
)