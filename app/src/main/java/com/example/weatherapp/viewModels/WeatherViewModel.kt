package com.example.weatherapp.viewModels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.models.WeatherModel
import com.example.weatherapp.repositories.WeatherRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.Date

interface InterfaceWeatherViewModel {
    val weeklyForecast: StateFlow<List<WeatherModel>>
}

class WeatherViewModel : ViewModel(), InterfaceWeatherViewModel {
    private val _weeklyForecast = MutableStateFlow<List<WeatherModel>>(emptyList())
    override val weeklyForecast: StateFlow<List<WeatherModel>> = _weeklyForecast

    private val repository = WeatherRepository()

    init {
        fetchWeatherData()
    }

    private fun fetchWeatherData() {
        viewModelScope.launch {
            val data = repository.getWeatherData()
            _weeklyForecast.value = data
            Log.e("API_DATA", "$data")
        }
    }
}


class FakeViewModelInterface : InterfaceWeatherViewModel {
    override val weeklyForecast: StateFlow<List<WeatherModel>> =
        MutableStateFlow(
            listOf(
                WeatherModel(1f, Date(), "Snowy", "01"),
                WeatherModel(2f, Date(), "Cloudy", "02"),
                WeatherModel(3f, Date(), "Sunny", "03"),
                WeatherModel(4f, Date(), "Cloudy", "02"),
                WeatherModel(5f, Date(), "Snowy", "01"),
            )
        )
}