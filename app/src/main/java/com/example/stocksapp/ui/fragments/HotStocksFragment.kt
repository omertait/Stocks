package com.example.stocksapp.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import com.example.stocksapp.R
import com.example.stocksapp.databinding.HotStocksFragmentBinding
import com.example.stocksapp.ui.classes.HotStock
import com.example.stocksapp.ui.classes.HotStockAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlin.math.abs

@AndroidEntryPoint
class HotStocksFragment : Fragment() {

    private var _binding: HotStocksFragmentBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = HotStocksFragmentBinding.inflate(inflater, container, false)

        // Setting up bottom navigation item click listener
        binding.bottomNavigation.setOnItemSelectedListener { itemMenu ->

            when (itemMenu.itemId) {
                R.id.add -> {
                    findNavController().navigate(R.id.action_infoFragment_to_addItemFragment)
                    true
                }

                R.id.home -> {
                    findNavController().navigate(R.id.action_infoFragment_to_mainFragment)
                    true
                }

                R.id.total -> {
                    findNavController().navigate(R.id.action_infoFragment_to_totalFragment)
                    true
                }

                R.id.hotStocks -> {
                    // Already in hotStocks fragment, do nothing
                    true
                }

                else -> false
            }
        }

        return  binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Check the "hotStocks" item in the bottom navigation when fragment is created
        val checkedMenuItem = binding.bottomNavigation.menu.findItem(R.id.hotStocks)
        checkedMenuItem.setChecked(true)
        // List of hot stocks data (static list - not changing)
        val stocks = listOf(
            HotStock(R.drawable.nvidia_image, getString(R.string.NVIDIA_name), getString(R.string.NVIDIA_symbol), getString(R.string.NVIDIA_description)),
            HotStock(R.drawable.meta_image, getString(R.string.Meta_name), getString(R.string.Meta_symbol), getString(R.string.Meta_description)),
            HotStock(R.drawable.netflix_image, getString(R.string.Netflix_name), getString(R.string.Netflix_symbol), getString(R.string.Netflix_description)),
            HotStock(R.drawable.rivian_image, getString(R.string.Rivian_Automotive_name), getString(R.string.Rivian_Automotive_symbol), getString(R.string.Rivian_Automotive_description)),
            HotStock(R.drawable.microsoft_image, getString(R.string.Microsoft_name), getString(R.string.Microsoft_symbol), getString(R.string.Microsoft_description)),
            HotStock(R.drawable.alphabet_image, getString(R.string.Alphabet_name), getString(R.string.Alphabet_symbol), getString(R.string.Alphabet_description)),
            HotStock(R.drawable.amazon_image, getString(R.string.Amazon_name), getString(R.string.Amazon_symbol), getString(R.string.Amazon_description)),
            HotStock(R.drawable.apple_image, getString(R.string.Apple_name), getString(R.string.Apple_symbol), getString(R.string.Apple_description)),
            HotStock(R.drawable.tesla_image, getString(R.string.Tesla_name), getString(R.string.Tesla_symbol), getString(R.string.Tesla_description)),
            HotStock(R.drawable.disney_image, getString(R.string.Disney_name), getString(R.string.Disney_symbol), getString(R.string.Disney_description))
        )

        binding.recyclerView.layoutManager = LinearLayoutManager(requireActivity())
        val adapter = HotStockAdapter(stocks)
        binding.recyclerView.adapter = adapter



    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}