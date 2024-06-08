package com.example.stocksapp.data.model

data class StockData(
    val c: Double, // current price
    val h: Double, // high price of the day
    val l: Double, // low price of the day
    val o: Double, // open price of the day
    val pc: Double, // previous close price
    val t: Long    // last trade time
)
