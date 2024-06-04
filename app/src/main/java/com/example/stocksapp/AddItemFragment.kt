package com.example.stocksapp

import Stock
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.stocksapp.databinding.ItemStockAddFragmentBinding

class AddItemFragment : Fragment() {

    private var _binding: ItemStockAddFragmentBinding? = null
    private val binding get() = _binding!!

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
            val item = Stock(
                binding.previewImage.id,
                binding.stockSymbol.text.toString(),
                name,
                "$$value",
                "+2.55%"
            )
            ItemsManager.add(item)
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
                    true
                }

                R.id.alerts -> {
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