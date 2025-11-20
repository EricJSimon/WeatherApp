package com.example.weatherapp.views.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.weatherapp.models.DailyWeatherModel
import com.example.weatherapp.models.GroupedForecast
import com.example.weatherapp.models.WeatherModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import com.example.weatherapp.R


@Composable
fun GroupedWeatherListComponent(groupedForecast: GroupedForecast) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(vertical = 8.dp)
    ) {
        item {
            Text(
                text = "Today",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
            LazyRow(
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(groupedForecast.todayHourly) { hourlyWeather ->
                    HourlyWeatherItem(weather = hourlyWeather)
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Upcoming Days",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )
        }

        items(groupedForecast.upcomingDays) { dailyWeather ->
            DailyWeatherRowItem(dailyWeather = dailyWeather)
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

// Composable for a single hourly item
@Composable
fun HourlyWeatherItem(weather: WeatherModel) {
    val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = timeFormat.format(weather.date))
        Image(
            painter = painterResource(id = weather.icon),
            contentDescription = weather.description,
            modifier = Modifier.size(40.dp)
        )
        Text(text = "${weather.temperature}°C", fontWeight = FontWeight.Bold)
    }
}

@Composable
fun DailyWeatherRowItem(dailyWeather: DailyWeatherModel) {
    val dayFormat = SimpleDateFormat("EEEE", Locale.getDefault())
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = dayFormat.format(dailyWeather.date),
                style = MaterialTheme.typography.bodyLarge
            )
            Image(
                painter = painterResource(id = dailyWeather.dominantIcon),
                contentDescription = dailyWeather.dominantDescription,
                modifier = Modifier.size(40.dp)
            )
            Text(text = "H: ${dailyWeather.maxTemp}° L: ${dailyWeather.minTemp}°")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GroupedWeatherListComponentPreview() {

    val fakeTodayHourly = listOf(
        WeatherModel(15f, Date(), "Sunny", R.drawable.ic_01_klart),
        WeatherModel(16f,
            Date(System.currentTimeMillis() + 3600000), "Sunny", R.drawable.ic_01_klart),
        WeatherModel(14f,
            Date(System.currentTimeMillis() + 7200000), "Cloudy", R.drawable.ic_04_molnigt)
    )

    val fakeUpcomingDays = listOf(
        DailyWeatherModel(
            date = Date(System.currentTimeMillis() + 86400000),
            maxTemp = 18f,
            minTemp = 10f,
            dominantIcon = R.drawable.ic_02_laettmolnighet,
            dominantDescription = "Partly Cloudy"
        ),
        DailyWeatherModel(
            date = Date(System.currentTimeMillis() + 172800000),
            maxTemp = 20f,
            minTemp = 12f,
            dominantIcon = R.drawable.ic_09_regnskur,
            dominantDescription = "Rain Showers"
        )
    )

    val fakeGroup = GroupedForecast(
        todayHourly = fakeTodayHourly,
        upcomingDays = fakeUpcomingDays
    )

    GroupedWeatherListComponent(groupedForecast = fakeGroup)
}