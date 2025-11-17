package com.example.weatherapp.models.network

import kotlinx.serialization.Serializable

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
