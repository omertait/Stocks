package com.example.stocksapp.ui.fragments

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.stocksapp.R
import com.example.stocksapp.data.model.Item
import com.example.stocksapp.data.utils.LocationCallback
import com.example.stocksapp.data.utils.convertCurrency
import com.example.stocksapp.databinding.TotalFragmentBinding
import com.example.stocksapp.ui.classes.MainFragmentViewModel
import dagger.hilt.android.AndroidEntryPoint
import com.example.stocksapp.data.utils.formatWithCommas
import com.example.stocksapp.data.utils.getColor
import com.example.stocksapp.data.utils.getCurrencySymbol
import com.example.stocksapp.data.utils.getLastLocation
import com.example.stocksapp.ui.MainActivity
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices

@AndroidEntryPoint
class TotalFragment : Fragment(), LocationCallback {

    private var _binding: TotalFragmentBinding? = null
    private val binding get() = _binding!!

    private val viewModel: MainFragmentViewModel by activityViewModels()

    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private var location: String = "Unknown"

    private var totalWorth: Double = 0.0
    private var totalChangePercentage: Double = 0.0
    private var totalProfit: Double = 0.0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = TotalFragmentBinding.inflate(inflater, container, false)

        // Initialize the FusedLocationProviderClient
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        getLastLocation(this, requireContext(), fusedLocationClient, this)

        binding.bottomNavigation.setOnItemSelectedListener { itemMenu ->

            when (itemMenu.itemId) {
                R.id.add -> {
                    findNavController().navigate(R.id.action_totalFragment_to_addItemFragment)
                    true
                }

                R.id.home -> {
                    findNavController().navigate(R.id.action_totalFragment_to_mainFragment)
                    true
                }

                R.id.total -> {
                    true
                }

                R.id.hotStocks -> {
                    findNavController().navigate(R.id.action_totalFragment_to_infoFragment)
                    true
                }

                else -> false
            }
        }

        return  binding.root
    }


    override fun onLocationRetrieved(country: String) {
        Log.d("location", country)
        location = country

        // calc totals after location is retrieved
        calcTotals()
    }

    private fun calcTotals() {
        viewModel.items?.observe(viewLifecycleOwner, Observer { itemList ->
            itemList?.let {
                for (item in it) {
                    updateTotals(it)
                    displayTotals()

                }
            }
        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val checkedMenuItem = binding.bottomNavigation.menu.findItem(R.id.total)
        checkedMenuItem.setChecked(true)

    }

    private fun updateTotals(items: List<Item>) {
        totalWorth = 0.0
        totalChangePercentage = 0.0
        totalProfit = 0.0



        for (item in items) {
            val convertedCurrPrice = convertCurrency(item.currPrice, location)
            val convertedStockPrice = convertCurrency(item.stockPrice, location)

            val worth = item.stockAmount.toFloat() * convertedCurrPrice.toFloat()
            val changePercentage = ((convertedCurrPrice.toFloat() - convertedStockPrice.toFloat()) / convertedStockPrice.toFloat()) * 100
            val profit = (convertedCurrPrice.toFloat() - convertedStockPrice.toFloat()) * item.stockAmount.toFloat()

            totalWorth += worth
            totalChangePercentage += changePercentage
            totalProfit += profit
        }

        totalChangePercentage /= items.size // Average percentage change
    }

    private fun displayTotals() {
        val currencySymbol = getCurrencySymbol(location)

        binding.totalWorth.text = "${currencySymbol}%.2f".format(totalWorth).formatWithCommas()
        binding.totalProfitLossPercentage.text = "%.2f%%".format(totalChangePercentage).formatWithCommas()
        binding.totalProfitLossValue.text = "${currencySymbol}%.2f".format(totalProfit).formatWithCommas()

        binding.totalProfitLossPercentage.setTextColor(getColor(totalChangePercentage, requireContext()))
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}