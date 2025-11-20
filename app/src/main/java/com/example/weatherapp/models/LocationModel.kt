package com.example.weatherapp.models

/**
 * Represent a 7-day forecast for a location
 *
 * @param locationName "Display name of the location (city, country)
 * @param hourlyWeather A list of the WeatherModel representing the forecast for the location
 *
 * @author Simonms
 */

data class LocationModel(
    val locationName: String,
    val hourlyWeather: List<WeatherModel>
)