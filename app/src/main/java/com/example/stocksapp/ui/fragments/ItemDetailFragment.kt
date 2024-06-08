package com.example.stocksapp.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.stocksapp.databinding.ItemStockDetailFragmentBinding
import com.bumptech.glide.Glide
import com.example.stocksapp.ui.classes.MainFragmentViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ItemDetailFragment : Fragment() {

    private var _binding: ItemStockDetailFragmentBinding? = null
    private val binding get() = _binding!!

    private val viewModel: MainFragmentViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = ItemStockDetailFragmentBinding.inflate(inflater, container, false)
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
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}