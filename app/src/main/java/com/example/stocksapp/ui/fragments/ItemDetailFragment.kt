package com.example.stocksapp.ui.fragments

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.stocksapp.R
import com.example.stocksapp.data.remote_db.StockRemoteDataSource
import com.example.stocksapp.data.utils.Constants
import com.example.stocksapp.data.utils.Error
import com.example.stocksapp.data.utils.LocationCallback
import com.example.stocksapp.data.utils.Success
import com.example.stocksapp.data.utils.convertCurrency
import com.example.stocksapp.databinding.ItemStockDetailFragmentBinding
import com.example.stocksapp.ui.classes.MainFragmentViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.Task
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject
import com.example.stocksapp.data.utils.formatWithCommas
import com.example.stocksapp.data.utils.getColor
import com.example.stocksapp.data.utils.getCurrencySymbol
import com.example.stocksapp.data.utils.getLastLocation

@AndroidEntryPoint
class ItemDetailFragment : Fragment(), LocationCallback {

    private var _binding: ItemStockDetailFragmentBinding? = null
    private val binding get() = _binding!!

    private val viewModel: MainFragmentViewModel by activityViewModels()

    @Inject
    lateinit var stockRemoteDataSource: StockRemoteDataSource

    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private var location : String = "Unknown"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = ItemStockDetailFragmentBinding.inflate(inflater, container, false)

        // Initialize the FusedLocationProviderClient
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())

        // Navigate to EditItemFragment when edit button is clicked
        binding.editBtn.setOnClickListener {
            findNavController().navigate(R.id.action_itemDetailFragment_to_editItemFragment)
        }

        getLastLocation(this, requireContext(), fusedLocationClient, this)

        return binding.root
    }

    override fun onLocationRetrieved(country: String) {
        Log.d("location", country)
        location = country
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        // Observe the chosenItem LiveData in the ViewModel
        viewModel.chosenItem.observe(viewLifecycleOwner) { item ->
            // Update UI with the item details
            binding.itemName.text = item.stockName
            binding.stockSymbol.text = item.stockSymbol
            binding.itemAmountTotal.text = (item.stockAmount.toFloat() * item.currPrice.toFloat()).toString()
            binding.itemPrice.text = item.stockPrice.toFloat().toString()
            binding.itemAmount.text = item.stockAmount.toString()

            // Load the stock image using Glide
            Glide.with(requireContext()).load(item.stockImage).circleCrop().into(binding.itemImage)

            // Perform API call to get latest stock data
            val stockSymbol = item.stockSymbol
            val token = Constants.API_KEY
            Log.d("PRICES", "Initiating API request")
            lifecycleScope.launch(Dispatchers.IO) {
                try {
                    val response = stockRemoteDataSource.getQuote(stockSymbol, token)

                    if (response.status is Success) {
                        val currPrice = response.status.data?.c
                        val openingPrice = response.status.data?.o

                        Log.d("PRICES", "currPrice, $stockSymbol: $$currPrice")
                        Log.d("PRICES", "openingPrice, $stockSymbol: $$openingPrice")

                        withContext(Dispatchers.Main) {

                            if (currPrice != null) {


                                // Update item properties
                                item.currPrice = currPrice
                                item.lastUpdateDate = System.currentTimeMillis()

                                // Update UI immediately
                                val convertedCurrPrice = convertCurrency(currPrice, location)
                                val convertedPrice = convertCurrency(item.stockPrice, location)
                                val currencySymbol = getCurrencySymbol(location)
                                binding.itemPrice.text = "${currencySymbol}%.2f".format(convertedCurrPrice).formatWithCommas()
                                binding.itemAmountTotal.text = "${currencySymbol}%.2f".format(convertedCurrPrice * item.stockAmount).formatWithCommas()

                                // Calculate profit
                                val totalPriceDiff = convertedCurrPrice - convertedPrice
                                val profit = totalPriceDiff * item.stockAmount
                                binding.itemProfit.text = "${currencySymbol}%.2f".format(profit).formatWithCommas()

                                // Calculate total change percentage
                                val totalChangePercentage = (totalPriceDiff / convertedPrice) * 100
                                val sign = if (totalChangePercentage > 0) "+" else ""
                                binding.totalChange.text = "${sign}%.2f%%".format(totalChangePercentage).formatWithCommas()
                                binding.totalChange.setTextColor(getColor(totalPriceDiff, requireContext()))

                                // Calculate today's change percentage
                                if (openingPrice != null) {
                                    item.openingPrice = openingPrice
                                    val convertedOpeningPrice = convertCurrency(openingPrice, location)
                                    val todayPriceDiff = convertedCurrPrice - convertedOpeningPrice
                                    val todayChange = (todayPriceDiff / convertedOpeningPrice) * 100

                                    // Display the percentage change
                                    val sign = if (todayChange > 0) "+" else ""
                                    binding.todayPrice.text = "${sign}%.2f%%".format(todayChange).formatWithCommas()
                                    binding.todayPrice.setTextColor(getColor(todayPriceDiff, requireContext()))
                                }

                                // Update item in ViewModel
                                viewModel.updateItem(item)
                            }
                        }
                    } else if (response.status is Error) {
                        val errorMessage = response.status.message

                        // Handle API error
                        item.currPrice = 0.0
                        binding.itemPrice.text = "0.0" // Update UI immediately
                        binding.itemAmountTotal.text = "Network Error"
                        binding.itemProfit.text = "Network Error"
                        binding.itemProfitTitle.text = "Network Error"

                        // Update item in ViewModel
                        viewModel.updateItem(item)

                        Log.d("PRICES", "API status = Error $errorMessage")
                    }
                } catch (e: Exception) {
                    Log.d("PRICES", "Exception in API request")
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
