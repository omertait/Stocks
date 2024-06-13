package com.example.stocksapp.data.utils

import android.content.Context
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import com.example.stocksapp.R

fun String.formatWithCommas(): String {
    return this.replace(Regex("\\B(?=(\\d{3})+(?!\\d))"), ",")
}

fun getColor(value : Double, context : Context): Int {
    val colorResId = if (value > 0) R.color.up else R.color.down
    return ContextCompat.getColor(context, colorResId)
}