package com.example.stocksapp.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
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

//    private val viewModel: MainFragmentViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = HotStocksFragmentBinding.inflate(inflater, container, false)

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
                    true
                }

                else -> false
            }
        }

        return  binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val checkedMenuItem = binding.bottomNavigation.menu.findItem(R.id.hotStocks)
        checkedMenuItem.setChecked(true)

        val stocks = listOf(
            HotStock(R.drawable.nvidia_image, getString(R.string.NVIDIA_name), "NVDA", getString(R.string.NVIDIA_description)),
            HotStock(R.drawable.meta_image, getString(R.string.Meta_name), "META", getString(R.string.Meta_description)),
            HotStock(R.drawable.netflix_image, getString(R.string.Netflix_name), "NFLX", getString(R.string.Netflix_description)),
            HotStock(R.drawable.rivian_image, getString(R.string.Rivian_Automotive_name), "RIVN", getString(R.string.Rivian_Automotive_description)),
            HotStock(R.drawable.microsoft_image, getString(R.string.Microsoft_name), "MSFT", getString(R.string.Microsoft_description)),
            HotStock(R.drawable.alphabet_image, getString(R.string.Alphabet_name), "GOOGL", getString(R.string.Alphabet_description)),
            HotStock(R.drawable.amazon_image, getString(R.string.Amazon_name), "AMZN", getString(R.string.Amazon_description)),
            HotStock(R.drawable.apple_image, getString(R.string.Apple_name), "AAPL", getString(R.string.Apple_description)),
            HotStock(R.drawable.tesla_image, getString(R.string.Tesla_name), "TSLA", getString(R.string.Tesla_description)),
            HotStock(R.drawable.disney_image, getString(R.string.Disney_name), "DIS", getString(R.string.Disney_description))
        )

        val adapter = HotStockAdapter(stocks)
        binding.viewPager.adapter = adapter

        // Set up page transformer for a cool scrolling effect
        val transformer = CompositePageTransformer()
        transformer.addTransformer { page, position ->
            val r = 1 - abs(position)
            page.scaleY = 0.85f + r * 0.15f
        }
        binding.viewPager.setPageTransformer(transformer)

        binding.viewPager.setOffscreenPageLimit(3)
        binding.viewPager.clipChildren = false
        binding.viewPager.clipToPadding = false

        binding.viewPager.getChildAt(0).overScrollMode = RecyclerView.OVER_SCROLL_NEVER

        // Start in the middle of the list to simulate infinite scrolling
        binding.viewPager.setCurrentItem(Integer.MAX_VALUE / 2, false)

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}