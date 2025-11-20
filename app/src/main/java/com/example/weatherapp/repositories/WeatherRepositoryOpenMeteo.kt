package com.example.weatherapp.repositories

import android.util.Log
import com.example.weatherapp.R
import com.example.weatherapp.models.LocationModel
import com.example.weatherapp.models.WeatherModel
import com.example.weatherapp.models.network.OpenMeteoResponse
import com.example.weatherapp.services.GeolocationService
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * This is using Open Meteo API to request weather data
 * https://open-meteo.com/ Weather data by Open-Meteo.com
 *
 * @author Simonms
 *
 */
class WeatherRepositoryOpenMeteo(private val locationService: GeolocationService) {

    private val client = HttpClient {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
                isLenient = true
            })
        }
    }

    suspend fun getWeatherDataByAddress(address: String): LocationModel? {
        val coordinates = locationService.getLonLat(address)

        return if (coordinates != null) {
            getWeatherDataByCoordinates(
                lat = coordinates.latitude.toFloat(),
                lon = coordinates.longitude.toFloat()
            )
        } else {
            Log.w("WeatherRepository", "Could not find coordinates for address: $address")
            null
        }
    }

    private suspend fun getWeatherDataByCoordinates(lon: Float, lat: Float): LocationModel? {
        val url =
            "https://api.open-meteo.com/v1/forecast?latitude=$lat&longitude=$lon&hourly=temperature_2m,weather_code"
        Log.d("API_URL", url)

        return try {
            val weatherResponse = client.get(url).body<OpenMeteoResponse>()
            val hourlyWeather = mapOpenMeteoResponseToWeatherModel(weatherResponse)

            val locationName = locationService.getLocationName(lat.toDouble(), lon.toDouble())

            LocationModel(
                locationName = locationName,
                hourlyWeather = hourlyWeather
            )
        } catch (e: Exception) {
            Log.e("API_ERROR_REQUEST","URL request failed for coordinates: ($lat, $lon)",e)
            null
        }
    }

    private fun mapOpenMeteoResponseToWeatherModel(response: OpenMeteoResponse): List<WeatherModel> {
        val hourly = response.hourlyData
        return hourly.time.mapIndexedNotNull { index, timeString ->
            val temperature = hourly.temperatures.getOrNull(index)
            val weatherCode = hourly.weatherCodes.getOrNull(index)

            val date = parseIsoDate(timeString)
            val description = mapSymbolToDescription(weatherCode)
            val icon = mapSymbolToIcon(weatherCode)

            Log.d("API_DATA_MAPPING", "Time: $date, Temp: $temperature, Code: $weatherCode")

            if (temperature != null && date != null && weatherCode != null) {
                WeatherModel(
                    temperature = temperature,
                    date = date,
                    description = description,
                    icon = icon
                )
            } else {
                null
            }
        }
    }

    private fun parseIsoDate(dateString: String): Date? {
        val openMeteoFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm", Locale.getDefault())
        return try {
            openMeteoFormat.parse(dateString)
        } catch (e: Exception) {
            Log.e("DATE_ERROR", "Failed to parse date string: $dateString", e)
            null
        }
    }

    private fun mapSymbolToDescription(symbolCode: Int?): String {
        return when (symbolCode) {
            0 -> "Clear sky"

            1 -> "Mainly clear"
            2 -> "Partly cloudy"
            3 -> "Overcast"

            45 -> "Fog"
            48 -> "Depositing rime fog"

            51 -> "Light drizzle"
            53 -> "Moderate Drizzle"
            55 -> "Dense drizzle"

            56 -> "Light freezing drizzle"
            57 -> "Dense freezing drizzle"

            61 -> "Slight rain"
            63 -> "Moderate rain"
            65 -> "Heavy rain"

            66 -> "Light freezing rain"
            67 -> "Heavy freezing rain"

            71 -> "Slight snow fall"
            73 -> "Moderate snow fall"
            75 -> "Heavy snow fall"

            77 -> "Snow grains"

            80 -> "Slight rain showers"
            81 -> "Moderate rain showers"
            82 -> "Violent rain showers"

            85 -> "Slight snow showers"
            86 -> "Heavy snow showers"

            95 -> "Slight thunderstorm"

            96 -> "Slight hail thunderstorm"
            99 -> "Heavy hail thunderstorm"

            else -> "Unknown"
        }
    }

    private fun mapSymbolToIcon(symbolCode: Int?): Int {
        return when (symbolCode) {
            0 -> R.drawable.ic_01_klart

            1 -> R.drawable.ic_02_laettmolnighet
            2 -> R.drawable.ic_03_halvklart
            3 -> R.drawable.ic_06_mulet

            45 -> R.drawable.ic_07_dimma
            48 -> R.drawable.ic_07_dimma

            51 -> R.drawable.ic_08_laettregnskur
            53 -> R.drawable.ic_09_regnskur
            55 -> R.drawable.ic_10_kraftigregnskur

            56 -> R.drawable.ic_12_laettbyavsnoeochregn
            57 -> R.drawable.ic_14_kraftigbyavregnochsnoe

            61 -> R.drawable.ic_18_laettregn
            63 -> R.drawable.ic_19_regn
            65 -> R.drawable.ic_20_kraftigtregn

            66 -> R.drawable.ic_22_laettsnoeblandatregn
            67 -> R.drawable.ic_24_kraftigtsnoeblandatregn

            71 -> R.drawable.ic_15_laettsnoeby
            73 -> R.drawable.ic_16_snoeby
            75 -> R.drawable.ic_17_kraftigsnoeby

            77 -> R.drawable.ic_13_byavregnochsnoe

            80 -> R.drawable.ic_18_laettregn
            81 -> R.drawable.ic_19_regn
            82 -> R.drawable.ic_20_kraftigtregn

            85 -> R.drawable.ic_25_laettsnoefall
            86 -> R.drawable.ic_27_ymnigtsnoefall

            95 -> R.drawable.ic_21_aaska

            96 -> R.drawable.ic_11_aaskskur
            99 -> R.drawable.ic_11_aaskskur
            else -> R.drawable.ic_00_unkownweather //NEW SYMBOL
        }
    }
}