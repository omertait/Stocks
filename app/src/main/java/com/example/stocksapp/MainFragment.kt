package com.example.stocksapp

import ItemsManager
import Stock
import StockAdapter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.stocksapp.databinding.MainFragmentBinding
import androidx.recyclerview.widget.ItemTouchHelper


class MainFragment : Fragment() {

    private var _binding: MainFragmentBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = MainFragmentBinding.inflate(inflater, container, false)

        return  binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val stockList = listOf(
            Stock(R.drawable.default_image, "AAPL", "Apple", "$145.09", "+2.34%"),
            Stock(R.drawable.default_image, "GOOGL", "Alphabet", "$2745.80", "+1.12%"),
            Stock(R.drawable.default_image, "AMZN", "Amazon", "$3401.46", "-0.57%"),
            Stock(R.drawable.default_image, "AAPL", "Apple", "$145.09", "+2.34%"),
            Stock(R.drawable.default_image, "GOOGL", "Alphabet", "$2745.80", "+1.12%"),
            Stock(R.drawable.default_image, "AMZN", "Amazon", "$3401.46", "-0.57%"),
            Stock(R.drawable.default_image, "AAPL", "Apple", "$145.09", "+2.34%"),
            Stock(R.drawable.default_image, "GOOGL", "Alphabet", "$2745.80", "+1.12%"),
            Stock(R.drawable.default_image, "AMZN", "Amazon", "$3401.46", "-0.57%"),
            Stock(R.drawable.default_image, "AAPL", "Apple", "$145.09", "+2.34%"),
            Stock(R.drawable.default_image, "GOOGL", "Alphabet", "$2745.80", "+1.12%"),
            Stock(R.drawable.default_image, "AMZN", "Amazon", "$3401.46", "-0.57%"),
            Stock(R.drawable.default_image, "AAPL", "Apple", "$145.09", "+2.34%"),
            Stock(R.drawable.default_image, "GOOGL", "Alphabet", "$2745.80", "+1.12%"),
            Stock(R.drawable.default_image, "AMZN", "Amazon", "$3401.46", "-0.57%"),
            Stock(R.drawable.default_image, "AAPL", "Apple", "$145.09", "+2.34%"),
            Stock(R.drawable.default_image, "GOOGL", "Alphabet", "$2745.80", "+1.12%"),
            Stock(R.drawable.default_image, "AMZN", "Amazon", "$3401.46", "-0.57%"),

            )
            for (s in stockList){
                ItemsManager.add(s)
            }

            val adapter = StockAdapter(requireActivity(), ItemsManager.items)
            binding.recyclerView.layoutManager = LinearLayoutManager(requireActivity())
            binding.recyclerView.adapter = adapter



        binding.bottomNavigation.setOnItemSelectedListener { itemMenu ->

            when (itemMenu.itemId) {
                R.id.add -> {
                    findNavController().navigate(R.id.action_mainFragment_to_addItemFragment)
                    true
                }

                R.id.home -> {

                    true
                }

                //                R.id.favorite -> {
                //                    findNavController().navigate(R.id.action_allItemsFragment_to_favoriteItemsFragment)
                //                    true
                //                }

                else -> false
            }
        }
        // item movement gestures
        ItemTouchHelper(object : ItemTouchHelper.Callback(){
            override fun getMovementFlags(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder
            )= makeFlag(ItemTouchHelper.ACTION_STATE_SWIPE, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT)

            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                TODO("Not yet implemented")
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                ItemsManager.remove(viewHolder.adapterPosition)
                binding.recyclerView.adapter!!.notifyItemRemoved(viewHolder.adapterPosition)

            }
        }).attachToRecyclerView(binding.recyclerView)
        // handle db calls
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}