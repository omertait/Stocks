package com.example.stocksapp

import Stock
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.stocksapp.databinding.ItemStockAddFragmentBinding
import android.net.Uri
import androidx.fragment.app.activityViewModels
import com.example.stocksapp.data.model.Item

class AddItemFragment : Fragment() {

    private var _binding: ItemStockAddFragmentBinding? = null
    private val binding get() = _binding!!

    private val imageUri : Uri? = null

    private val viewModel : MainFragmentViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = ItemStockAddFragmentBinding.inflate(inflater, container, false)

        binding.addBtn.setOnClickListener {

            val value = binding.stockAmount.text.toString().toFloat() * binding.stockPrice.text.toString().toFloat()
            // name should set from api call
            val name = "stock name"
            val item = Item(
                stockName = name,
                stockSymbol = binding.stockSymbol.text.toString(),
                stockPrice = value.toDouble(), // Assuming value is a String that can be converted to Double
                stockAmount = binding.stockAmount.text.toString().toLong(), // You need to provide a valid Long value for stockAmount
                stockImage = R.drawable.default_image.toString(),
                currPrice = value.toDouble() // Assuming value is a String that can be converted to Double
            )
//            ItemsManager.add(item)
            viewModel.addItem(item)
            findNavController().navigate(R.id.action_addItemFragment_to_mainFragment)

        }

        binding.bottomNavigation.setOnItemSelectedListener { itemMenu ->

            when (itemMenu.itemId) {
                R.id.add -> {
                    true
                }

                R.id.home -> {
                    findNavController().navigate(R.id.action_addItemFragment_to_mainFragment)
                    true
                }

                R.id.total -> {
                    findNavController().navigate(R.id.action_addItemFragment_to_totalFragment)
                    true
                }

                R.id.alerts -> {
                    findNavController().navigate(R.id.action_addItemFragment_to_infoFragment)
                    true
                }

                else -> false
            }
        }

        return  binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val checkedMenuItem = binding.bottomNavigation.menu.findItem(R.id.add)
        checkedMenuItem.setChecked(true)

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}