package com.example.weatherapp.models.network

import kotlinx.serialization.Serializable

/**
 * This is a model of the DEMO API response data.
 * This model will only handle time, temperature ("t"), weather code ("Wsymb2")
 *
 * @author Simonms
 */
@Serializable
data class ForecastApiResponse(
    val timeSeries: List<TimeSeriesData>
)

@Serializable
data class TimeSeriesData(
    val validTime: String,
    val parameters: List<Parameter>
)

@Serializable
data class Parameter(
    val name: String,
    val values: List<Float>
)
