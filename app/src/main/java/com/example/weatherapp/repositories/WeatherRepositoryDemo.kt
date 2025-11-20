package com.example.weatherapp.repositories

import android.util.Log
import com.example.weatherapp.R
import com.example.weatherapp.models.WeatherModel
import com.example.weatherapp.models.network.ForecastApiResponse
import com.example.weatherapp.models.network.TimeSeriesData
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
 * This is using the DEMO website
 * to get data by a HTTP request
 *
 * @author Simonms
 */
class WeatherRepositoryDemo {

    private val client = HttpClient {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
                isLenient = true
            })
        }
    }

    suspend fun getWeatherData(lon: Float, lat: Float): List<WeatherModel> {
        val url = "https://maceo.sth.kth.se/weather/forecast?lonLat=lon/$lon/lat/$lat"

        try {
            val apiResponse = client.get(url).body<ForecastApiResponse>()

            return mapResponseToWeatherModel(apiResponse.timeSeries)
        } catch (e: Exception) {
            Log.e("API_ERROR_REQUEST", "url request failed", e)
            e.printStackTrace()
            return emptyList()
        }
    }

    private fun mapResponseToWeatherModel(timeSeries: List<TimeSeriesData>): List<WeatherModel> {
        return timeSeries.filterIndexed { index, _ -> index % 3 == 0 }
            .mapNotNull { dataPoint ->

                val temperature =
                    dataPoint.parameters.find { it.name == "t" }?.values?.firstOrNull()


                val weatherSymbol =
                    dataPoint.parameters.find { it.name == "Wsymb2" }?.values?.firstOrNull()
                        ?.toInt()


                val description = mapSymbolToDescription(weatherSymbol)
                val icon = mapSymbolToIcon(weatherSymbol)

                val date = parseIsoDate(dataPoint.validTime)

                Log.e("API_DATA", "$temperature, $weatherSymbol, $description, $icon, $date")

                if (temperature != null && date != null) {
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
        return try {
            val format = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault())
            format.parse(dateString)
        } catch (e: Exception) {
            Log.e("DATE_ERROR", "Failed to parse date string: $dateString", e)
            null
        }
    }

    private fun mapSymbolToDescription(symbolCode: Int?): String {
        return when (symbolCode) {
            1 -> "Clear sky"
            2 -> "Nearly clear sky"
            3 -> "Variable cloudiness"
            4 -> "Halfclear sky"
            5 -> "Cloudy sky"
            6 -> "Overcast"
            7 -> "Fog"
            8 -> "Light rain showers"
            9 -> "Moderate rain showers"
            10 -> "Heavy rain showers"
            11 -> "Thunderstorm"
            18 -> "Light rain"
            19 -> "Moderate rain"
            20 -> "Heavy rain"
            else -> "Unknown"
        }
    }

    private fun mapSymbolToIcon(symbolCode: Int?): Int {
        return when (symbolCode) {
            1 -> R.drawable.ic_01_klart
            2 -> R.drawable.ic_02_laettmolnighet
            3 -> R.drawable.ic_03_halvklart
            4 -> R.drawable.ic_03_halvklart
            5 -> R.drawable.ic_04_molnigt
            6 -> R.drawable.ic_06_mulet
            7 -> R.drawable.ic_07_dimma
            8 -> R.drawable.ic_08_laettregnskur
            9 -> R.drawable.ic_09_regnskur
            10 -> R.drawable.ic_10_kraftigregnskur
            11 -> R.drawable.ic_11_aaskskur
            18 -> R.drawable.ic_18_laettregn
            19 -> R.drawable.ic_19_regn
            20 -> R.drawable.ic_20_kraftigtregn
            else -> R.drawable.ic_00_unkownweather
        }
    }
}