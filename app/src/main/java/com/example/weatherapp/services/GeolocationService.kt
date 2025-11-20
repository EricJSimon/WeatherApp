package com.example.weatherapp.services

import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import java.util.Locale
import kotlin.coroutines.resume

/**
 * A service class responsible for handling location-based operations
 * It uses the Android Geocoder to get location data.
 *
 * @author Simonms
 *
 */
data class Coordinates(val latitude: Double, val longitude: Double)

class GeolocationService(context: Context) {
    private val geocoder = Geocoder(context, Locale.getDefault())

    suspend fun getLocationName(lat: Double, lon: Double): String {
        return withContext(Dispatchers.IO) {
            suspendCancellableCoroutine { continuation ->
                val listener = object : Geocoder.GeocodeListener {
                    override fun onGeocode(addresses: List<Address>) {
                        val locationName = if (addresses.isNotEmpty()) {
                            formatAddress(addresses[0])
                        } else {
                            "Unknown Location"
                        }
                        if (continuation.isActive) {
                            continuation.resume(locationName)
                        }
                    }

                    override fun onError(errorMessage: String?) {
                        Log.e("LocationService", "GeocodeListener error: $errorMessage")
                        if (continuation.isActive) {
                            continuation.resume("Location not found")
                        }
                    }
                }
                geocoder.getFromLocation(lat, lon, 1, listener)
            }
        }
    }

    suspend fun getLonLat(address: String): Coordinates? {
        return withContext(Dispatchers.IO) {
            suspendCancellableCoroutine { continuation ->
                val listener = object : Geocoder.GeocodeListener {
                    override fun onGeocode(addresses: List<Address>) {
                        if (addresses.isNotEmpty()) {
                            val bestResult = addresses[0]
                            val coordinates = Coordinates(
                                latitude = bestResult.latitude,
                                longitude = bestResult.longitude
                            )
                            Log.d("LocationService", "Found coordinates for $address: $coordinates")
                            if (continuation.isActive) continuation.resume(coordinates)
                        } else {
                            Log.w("LocationService", "No coordinates found for address: $address")
                            if (continuation.isActive) continuation.resume(null)
                        }
                    }

                    override fun onError(errorMessage: String?) {
                        Log.e(
                            "LocationService",
                            "Forward geocoding error for $address: $errorMessage"
                        )
                        if (continuation.isActive) continuation.resume(null)
                    }
                }
                geocoder.getFromLocationName(address, 1, listener)
            }
        }
    }

    private fun formatAddress(address: Address): String {
        val locationName = address.locality
            ?: address.subAdminArea
            ?: address.adminArea
            ?: address.featureName
            ?: "Unknown Area"
        val country = address.countryName ?: ""
        Log.d("LocationService", "Found address: $locationName, $country")

        return if (locationName != "Unknown Area" && country.isNotBlank()) {
            "$locationName, $country"
        } else {
            locationName
        }
    }
}