package com.example.stocksapp

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

        return  binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.bottomNavigation.setOnItemSelectedListener { itemMenu ->

            when (itemMenu.itemId) {
                R.id.add -> {
                    true
                }

                R.id.home -> {
                    findNavController().navigate(R.id.action_addItemFragment_to_mainFragment)
                    true
                }

                //                R.id.favorite -> {
                //                    findNavController().navigate(R.id.action_allItemsFragment_to_favoriteItemsFragment)
                //                    true
                //                }

                else -> false
            }
        }


    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}