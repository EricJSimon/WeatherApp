package com.example.weatherapp.models

import androidx.annotation.DrawableRes
import java.util.Date

/**
 * This is a representation of the model data
 * that is displayed in the UI component.
 *
 * @author Simonms
 *
 */

data class WeatherModel(
    val temperature: Float,
    val date: Date,
    val description: String,
    @get:DrawableRes val icon: Int
)