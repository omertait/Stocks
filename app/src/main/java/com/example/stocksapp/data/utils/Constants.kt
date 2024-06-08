package com.example.stocksapp.data.utils

import com.example.stocksapp.BuildConfig

class Constants {

    companion object {
        const val BASE_URL = "https://finnhub.io/api/v1/"
        const val API_KEY = BuildConfig.FINNHUB_API_KEY
    }
}