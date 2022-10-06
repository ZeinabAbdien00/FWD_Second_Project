package com.udacity.asteroidradar.main

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build

fun checkForInternetConnection(context: Context): Boolean {
    val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        val internetNetwork = connectivityManager.activeNetwork ?: return false
        val activeInternetNetwork = connectivityManager.getNetworkCapabilities(internetNetwork) ?: return false
        return when {
            activeInternetNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            activeInternetNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            else -> false
        }
    } else {
        @Suppress("DEPRECATION") val networkInfo =
            connectivityManager.activeNetworkInfo ?: return false
        @Suppress("DEPRECATION")
        return networkInfo.isConnected
    }
}