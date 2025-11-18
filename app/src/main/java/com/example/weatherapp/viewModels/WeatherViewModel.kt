package com.example.weatherapp.viewModels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.R
import com.example.weatherapp.models.WeatherModel
import com.example.weatherapp.repositories.WeatherRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.Date

interface InterfaceWeatherViewModel {
    val weeklyForecast: StateFlow<List<WeatherModel>>
    fun updateLocation(lon: Float, lat: Float)
}

class WeatherViewModel : ViewModel(), InterfaceWeatherViewModel {
    private val _weeklyForecast = MutableStateFlow<List<WeatherModel>>(emptyList())
    override val weeklyForecast: StateFlow<List<WeatherModel>> = _weeklyForecast

    override fun updateLocation(lon: Float, lat: Float) {
        fetchWeatherData(lon, lat)
    }

    private val repository = WeatherRepository()

    init {
        fetchWeatherData(0.0f, 0.0f)
    }

    private fun fetchWeatherData(lat: Float, lon: Float) {
        viewModelScope.launch {
            val data = repository.getWeatherData(lat, lon)
            _weeklyForecast.value = data
            Log.e("API_DATA", "$data")
        }
    }
}


class FakeViewModelInterface : InterfaceWeatherViewModel {
    override val weeklyForecast: StateFlow<List<WeatherModel>> =
        MutableStateFlow(
            listOf(
                WeatherModel(1f, Date(), "Snowy", R.drawable.ic_26_snoefall),
                WeatherModel(2f, Date(), "Cloudy", R.drawable.ic_04_molnigt),
                WeatherModel(3f, Date(), "Sunny", R.drawable.ic_01_klart),
                WeatherModel(4f, Date(), "Fog", R.drawable.ic_07_dimma),
                WeatherModel(5f, Date(), "Thunderstorm", R.drawable.ic_21_aaska),
            )
        )

    override fun updateLocation(lon: Float, lat: Float) {
    }
}