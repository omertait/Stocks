package com.example.stocksapp.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.stocksapp.R
import com.example.stocksapp.data.model.Item
import com.example.stocksapp.databinding.TotalFragmentBinding
import com.example.stocksapp.ui.classes.MainFragmentViewModel
import dagger.hilt.android.AndroidEntryPoint
import com.example.stocksapp.data.utils.formatWithCommas
import com.example.stocksapp.data.utils.getColor

@AndroidEntryPoint
class TotalFragment : Fragment() {

    private var _binding: TotalFragmentBinding? = null
    private val binding get() = _binding!!

    private val viewModel: MainFragmentViewModel by activityViewModels()

    private var totalWorth: Double = 0.0
    private var totalChangePercentage: Double = 0.0
    private var totalProfit: Double = 0.0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = TotalFragmentBinding.inflate(inflater, container, false)

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


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val checkedMenuItem = binding.bottomNavigation.menu.findItem(R.id.total)
        checkedMenuItem.setChecked(true)


        viewModel.items?.observe(viewLifecycleOwner, Observer { itemList ->
            itemList?.let {
                for (item in it) {
                    updateTotals(it)
                    displayTotals()

                }
            }
        })

    }

    private fun updateTotals(items: List<Item>) {
        totalWorth = 0.0
        totalChangePercentage = 0.0
        totalProfit = 0.0

        for (item in items) {
            val worth = item.stockAmount.toFloat() * item.currPrice.toFloat()
            val changePercentage = ((item.currPrice.toFloat() - item.stockPrice.toFloat()) / item.stockPrice.toFloat()) * 100
            val profit = (item.currPrice.toFloat() - item.stockPrice.toFloat()) * item.stockAmount.toFloat()

            totalWorth += worth
            totalChangePercentage += changePercentage
            totalProfit += profit
        }

        totalChangePercentage /= items.size // Average percentage change
    }

    private fun displayTotals() {
        binding.totalWorth.text = "$%.2f".format(totalWorth).formatWithCommas()
        binding.totalProfitLossPercentage.text = "%.2f%%".format(totalChangePercentage).formatWithCommas()
        binding.totalProfitLossValue.text = "$%.2f".format(totalProfit).formatWithCommas()

        binding.totalProfitLossPercentage.setTextColor(getColor(totalChangePercentage, requireContext()))
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}