package com.example.weatherapp.viewModels

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.R
import com.example.weatherapp.models.LocationModel
import com.example.weatherapp.models.WeatherModel
import com.example.weatherapp.repositories.WeatherRepositoryOpenMeteo
import com.example.weatherapp.services.GeolocationService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.Date

/**
 * This is the ViewModel class that handles
 * communication between models, repositories and HomeScreen
 *
 * @author Simonms
 *
 */
interface InterfaceWeatherViewModel {
    val locationModel: StateFlow<LocationModel?>
    fun updateLocationByName(address: String)

}

class WeatherViewModel(application: Application) : AndroidViewModel(application),
    InterfaceWeatherViewModel {
    private var _locationModel = MutableStateFlow<LocationModel?>(null)
    override val locationModel: StateFlow<LocationModel?> = _locationModel

    private val locationService = GeolocationService(application)
    private val repository = WeatherRepositoryOpenMeteo(locationService)

    override fun updateLocationByName(address: String) {
        viewModelScope.launch {
            val data = repository.getWeatherDataByAddress(address)
            _locationModel.value = data
            if (data == null) {
                Log.w("WeatherViewModel", "No data found for $address")
            }
        }
    }
}


class FakeViewModelInterface : InterfaceWeatherViewModel {
    override val locationModel: StateFlow<LocationModel> = MutableStateFlow(
        LocationModel(
            locationName = "Stockholm, Sweden", hourlyWeather = listOf(
                WeatherModel(1f, Date(), "Snowy", R.drawable.ic_26_snoefall),
                WeatherModel(2f, Date(), "Cloudy", R.drawable.ic_04_molnigt),
                WeatherModel(3f, Date(), "Sunny", R.drawable.ic_01_klart),
                WeatherModel(4f, Date(), "Fog", R.drawable.ic_07_dimma),
                WeatherModel(5f, Date(), "Thunderstorm", R.drawable.ic_21_aaska),
            )
        )
    )

    override fun updateLocationByName(address: String) {}
}