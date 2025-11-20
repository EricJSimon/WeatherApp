package com.example.weatherapp.views

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.weatherapp.viewModels.FakeViewModelInterface
import com.example.weatherapp.viewModels.InterfaceWeatherViewModel
import com.example.weatherapp.views.components.WeatherListComponent

/**
 * Main HomeScreen for this APP
 *
 * @author Simonms
 *
 */
@Composable
fun HomeScreen(weatherViewModel: InterfaceWeatherViewModel) {
    val forecast by weatherViewModel.locationModel.collectAsState()
    val snackBarHostState = remember { SnackbarHostState() }

    var addressText by remember { mutableStateOf("") }
    val isButtonEnabled = addressText.isNotBlank()

    Scaffold(snackbarHost = { SnackbarHost(snackBarHostState) }, bottomBar = {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .height(IntrinsicSize.Min),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = addressText,
                onValueChange = { addressText = it },
                label = { Text("City, Country") },
                modifier = Modifier.weight(1f),
                singleLine = true
            )
            Button(
                onClick = {
                    weatherViewModel.updateLocationByName(addressText)
                },
                enabled = isButtonEnabled,
                shape = RoundedCornerShape(10),
                modifier = Modifier
                    .fillMaxHeight()
                    .width(IntrinsicSize.Min)
            ) {
                Text(
                    text = ("Submit")
                )
            }
        }
    }) { innerPadding ->
        Box(
            modifier = Modifier.padding(innerPadding), contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = forecast?.locationName ?: "Weather Forecast",
                    style = MaterialTheme.typography.headlineMedium,
                    modifier = Modifier.padding(16.dp)
                )
                forecast?.hourlyWeather?.let { weatherList ->
                    WeatherListComponent(forecastData = weatherList)
                }
            }
        }
    }
}

@Preview
@Composable
fun HomeScreenPreview() {
    Surface {
        HomeScreen(weatherViewModel = FakeViewModelInterface())
    }
}
