package com.example.weatherapp.models.network

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * This is a model of Open Meteo API response data.
 * This will only handle time, temperature_2m and weather_code
 *
 * @author Simonms
 */
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