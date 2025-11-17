package com.example.weatherapp.views.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.weatherapp.models.WeatherModel
import java.util.Date

@Composable
fun WeatherListComponent(
    modifier: Modifier = Modifier, forecastData: List<WeatherModel>
) {
    LazyColumn(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        items(forecastData) { weatherItem ->
            WeatherRowItem(weather = weatherItem)
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
fun WeatherRowItem(weather: WeatherModel) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "${weather.date}")
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = weather.description, fontWeight = FontWeight.Bold)
                Text(text = "${weather.temperature}C")
            }
        }

    }
}

@Preview(showBackground = true)
@Composable
fun WeatherListComponentPreview() {
    val fakeForecast = listOf(
        WeatherModel(1f, Date(), "Snowy", "01"),
        WeatherModel(2f, Date(), "Cloudy", "02"),
        WeatherModel(3f, Date(), "Sunny", "03"),
        WeatherModel(4f, Date(), "Cloudy", "02"),
        WeatherModel(5f, Date(), "Snowy", "01"),
    )
    Column {
        WeatherListComponent(forecastData = fakeForecast)
    }
}