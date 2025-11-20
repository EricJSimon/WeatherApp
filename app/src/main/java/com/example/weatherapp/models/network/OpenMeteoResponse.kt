package com.example.weatherapp.models.network

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class OpenMeteoResponse(
    val latitude: Float,
    val longitude: Float,
    @SerialName("hourly")
    val hourlyData: HourlyData
)

@Serializable
data class HourlyData(
    val time: List<String>,

    @SerialName("temperature_2m")
    val temperatures: List<Float>,

    @SerialName("weather_code")
    val weatherCodes: List<Int>
)