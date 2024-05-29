package com.example.stocksapp

import Stock
import StockAdapter
import android.os.Bundle
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val listView: ListView = findViewById(R.id.listView)

        val stockList = listOf(
            Stock(R.drawable.default_image, "AAPL", "Apple", "$145.09", "+2.34%"),
            Stock(R.drawable.default_image, "GOOGL", "Alphabet", "$2745.80", "+1.12%"),
            Stock(R.drawable.default_image, "AMZN", "Amazon", "$3401.46", "-0.57%"),

        )

        val adapter = StockAdapter(this, stockList)
        listView.adapter = adapter
    }
}