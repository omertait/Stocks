package com.example.stocksapp.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.stocksapp.R
import com.example.stocksapp.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//
//        enableEdgeToEdge()
//
//        _binding = ActivityMainBinding.inflate(layoutInflater)
//        setContentView(binding.root)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


    }
}



