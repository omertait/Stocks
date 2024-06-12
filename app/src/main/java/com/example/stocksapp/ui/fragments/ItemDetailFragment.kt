package com.example.stocksapp.ui.fragments

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.graphics.toColor
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.stocksapp.databinding.ItemStockDetailFragmentBinding
import com.bumptech.glide.Glide
import com.example.stocksapp.R
import com.example.stocksapp.data.remote_db.StockRemoteDataSource
import com.example.stocksapp.data.utils.Constants
import com.example.stocksapp.data.utils.Success
import com.example.stocksapp.data.utils.Error
import com.example.stocksapp.ui.classes.MainFragmentViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.math.abs
import com.example.stocksapp.data.utils.formatWithCommas

@AndroidEntryPoint
class ItemDetailFragment : Fragment() {

    private var _binding: ItemStockDetailFragmentBinding? = null
    private val binding get() = _binding!!

    private val viewModel: MainFragmentViewModel by activityViewModels()

    @Inject
    lateinit var stockRemoteDataSource: StockRemoteDataSource

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = ItemStockDetailFragmentBinding.inflate(inflater, container, false)
        binding.editBtn.setOnClickListener {
            findNavController().navigate(R.id.action_itemDetailFragment_to_editItemFragment)
        }
        return  binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        viewModel.chosenItem.observe(viewLifecycleOwner) {
            binding.itemName.text = it.stockName
            binding.stockSymbol.text = it.stockSymbol
            binding.itemAmountTotal.text = (it.stockAmount.toFloat() * it.currPrice.toFloat()).toString()
            binding.itemPrice.text = it.stockPrice.toFloat().toString()
            binding.itemAmount.text = it.stockAmount.toString()
            // add all api calls and all information
            Glide.with(requireContext()).load(it.stockImage).circleCrop().into(binding.itemImage)

            val stockSymbol = it.stockSymbol
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
                                it.currPrice = currPrice
                                it.lastUpdateDate = System.currentTimeMillis()

                                binding.itemPrice.text = "$%.2f".format(currPrice)
                                    .formatWithCommas() // update UI immediately
                                binding.itemAmountTotal.text =
                                    "$%.2f".format(it.currPrice * it.stockAmount).formatWithCommas()
                                val totalPriceDiff = it.currPrice - it.stockPrice
                                val profit = totalPriceDiff * it.stockAmount
                                if (profit >= 0) {
                                    binding.itemProfit.text =
                                        "$%.2f".format(profit).formatWithCommas()
//                                    binding.itemProfitTitle.text = getString(R.string.profit)
                                } else {
//                                    binding.itemProfitTitle.text = getString(R.string.loss)
                                    binding.itemProfit.text =
                                        "-$%.2f".format(abs(profit)).formatWithCommas()
                                }
                                val totalChangePercentage = (totalPriceDiff / it.stockPrice) * 100
                                if (totalPriceDiff >= 0) {
                                    binding.totalChange.text =
                                        "+%.2f%%".format(totalChangePercentage).formatWithCommas()
                                    binding.totalChange.setTextColor(ContextCompat.getColor(
                                        requireContext(), R.color.up))
                                } else {
                                    binding.totalChange.text =
                                        "%.2f%%".format(totalChangePercentage).formatWithCommas()
                                    binding.totalChange.setTextColor(ContextCompat.getColor(
                                        requireContext(), R.color.down))
                                }


                                if (openingPrice != null) {
                                    val todayPriceDiff =
                                        openingPrice.toDouble() - currPrice.toDouble()
                                    val todayChange =
                                        (todayPriceDiff / openingPrice.toDouble()) * 100
                                    // Display the percentage change
                                    if (todayPriceDiff >= 0) {
                                        binding.todayPrice.text =
                                            "+%.2f%%".format(todayChange).formatWithCommas()
                                        binding.todayPrice.setTextColor(ContextCompat.getColor(
                                            requireContext(), R.color.up))
                                    } else {
                                        binding.todayPrice.text =
                                            "%.2f%%".format(todayChange).formatWithCommas()
                                        binding.todayPrice.setTextColor(ContextCompat.getColor(
                                            requireContext(), R.color.down))
                                    }
                                }
                                viewModel.updateItem(it)
                            }
                        }
                    } else if (response.status is Error) {
                        val errorMessage = response.status.message
                        it.currPrice = 0.0
                        binding.itemPrice.text = "0.0" // update UI immediately
                        binding.itemAmountTotal.text = "network Error"
//                            R.string.networkError.toString()
                        val profit = 0.0
                        binding.itemProfit.text = "network Error"//R.string.networkError.toString()
                        binding.itemProfitTitle.text = "network Error"//getString(R.string.profit)
                        viewModel.updateItem(it)
                        Log.d("PRICES", "API status = Error ${errorMessage}")
                    }
                } catch (e: Exception) {
                    Log.d("PRICES", "Exception in API request")
                }
            }
            Log.d("api", "end api call")
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }




}