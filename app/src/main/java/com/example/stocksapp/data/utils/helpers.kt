package com.example.stocksapp.data.utils

fun String.formatWithCommas(): String {
    return this.replace(Regex("\\B(?=(\\d{3})+(?!\\d))"), ",")
}