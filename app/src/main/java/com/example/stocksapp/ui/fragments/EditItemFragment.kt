package com.example.stocksapp.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.stocksapp.databinding.ItemStockEditFragmentBinding
import com.example.stocksapp.ui.classes.MainFragmentViewModel

class EditItemFragment : Fragment() {

    private var _binding: ItemStockEditFragmentBinding? = null
    private val binding get() = _binding!!

    private val viewModel: MainFragmentViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = ItemStockEditFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.chosenItem.observe(viewLifecycleOwner) { item ->
            // Update UI with the chosen item
            binding.stockName.setText(item.stockName)
            binding.stockSymbol.setText(item.stockSymbol)
            binding.stockAmount.setText(item.stockAmount.toString())
            binding.stockPrice.setText(item.stockPrice.toString())
            // Update other UI elements as needed
        }

        binding.saveBtn.setOnClickListener {
            // Retrieve the existing item from the ViewModel
            val existingItem = viewModel.chosenItem.value ?: return@setOnClickListener

            // Update only the non-null fields
            binding.stockName.text?.toString()?.let {
                if (it.isNotBlank()) existingItem.stockName = it
            }
            binding.stockSymbol.text?.toString()?.let {
                if (it.isNotBlank()) existingItem.stockSymbol = it
            }
            binding.stockAmount.text?.toString()?.let {
                if (it.isNotBlank()) existingItem.stockAmount = it.toLong()
            }
            binding.stockPrice.text?.toString()?.let {
                if (it.isNotBlank()) {
                    existingItem.stockPrice = it.toDouble()
                    existingItem.currPrice = it.toDouble()
                }
            }
            // Update other fields as needed

            // Update the item in the database
            viewModel.updateItem(existingItem)

            // Navigate back
            findNavController().navigateUp()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
