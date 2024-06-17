package com.example.stocksapp.data.utils

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.stocksapp.R
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.tasks.Task

fun String.formatWithCommas(): String {
    return this.replace(Regex("\\B(?=(\\d{3})+(?!\\d))"), ",")
}

fun getColor(value : Double, context : Context): Int {
    val colorResId = if (value > 0) R.color.up else R.color.down
    return ContextCompat.getColor(context, colorResId)
}

interface LocationCallback {
    fun onLocationRetrieved(country: String)
}



fun getLastLocation(
    fragment: Fragment,
    context: Context,
    fusedLocationClient: FusedLocationProviderClient,
    callback: LocationCallback
) {
    val permissionLauncher :ActivityResultLauncher<String> =
        fragment.registerForActivityResult(ActivityResultContracts.RequestPermission()) {
            if(it){
                val locationResult: Task<Location> = fusedLocationClient.lastLocation
                getLocation(locationResult, callback)
            } else{
                callback.onLocationRetrieved("Unknown")
                Toast.makeText(context,R.string.Permission_Declined, Toast.LENGTH_SHORT ).show()
            }
        }

    if (ActivityCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) != PackageManager.PERMISSION_GRANTED
    ) {
        permissionLauncher.launch(Manifest.permission.ACCESS_COARSE_LOCATION)
    }
    else{
        val locationResult: Task<Location> = fusedLocationClient.lastLocation
        getLocation(locationResult, callback)

    }

}

private fun getLocation(
    locationResult : Task<Location>,
    callback: LocationCallback
){
    locationResult.addOnCompleteListener { task ->
        if (task.isSuccessful) {
            val location: Location? = task.result
            location?.let {
                val country = getCountryFromLocation(it.latitude, it.longitude)
                callback.onLocationRetrieved(country)
            } ?: callback.onLocationRetrieved("Unknown")
        } else {
            callback.onLocationRetrieved("Unknown")
        }
    }
}


private fun getCountryFromLocation(latitude: Double, longitude: Double): String {
    return if (latitude in 29.5..33.3 && longitude in 34.3..35.9) {
        "Israel"
    } else if (latitude in 24.396308..49.384358 && longitude in -125.0..-66.93457) {
        "USA"
    } else {
        "Other"
    }
}

fun convertCurrency(amount: Double, country: String): Double {
    val conversionRate = conversionRates[country] ?: return amount // Return the same amount if conversion rate not found
    return (amount * conversionRate)
}

fun getCurrencySymbol(country: String) : String {
    return currencySymbols[country] ?: "$"
}
private val conversionRates = mapOf(
    "Israel" to 3.45, // Example rate for USD to ILS
    "USA" to 1.0   // No conversion needed for USD to USD
)

private val currencySymbols = mapOf(
    "Israel" to "₪",
    "USA" to "$"
)


