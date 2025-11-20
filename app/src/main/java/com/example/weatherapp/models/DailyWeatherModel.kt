package com.example.weatherapp.models

import java.util.Date

data class DailyWeatherModel(
    val date: Date,
    val maxTemp: Float,
    val minTemp: Float,
    val dominantIcon: Int,
    val dominantDescription: String
)

data class GroupedForecast(
    val todayHourly: List<WeatherModel>,
    val upcomingDays: List<DailyWeatherModel>
)