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

//        val stockList = listOf(
//            Stock(R.drawable.blue_circle, "AAPL", "Apple", "$145.09", "+2.34%"),
//            Stock(R.drawable.orange_circle, "GOOGL", "Alphabet", "$2745.80", "+1.12%"),
//            Stock(R.drawable.pink_circle, "AMZN", "Amazon", "$3401.46", "-0.57%"),
//
//
//            )
//        for (s in stockList){
//            ItemsManager.add(s)
//        }

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




        return  binding.root
    }




    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val checkedMenuItem = binding.bottomNavigation.menu.findItem(R.id.home)
        checkedMenuItem.setChecked(true)

        binding.recyclerView.layoutManager = LinearLayoutManager(requireActivity())

        // Define the item click callback
        val itemClickCallback: (Stock) -> Unit = {
            // Handle item click here
            findNavController().navigate(R.id.action_mainFragment_to_itemDetailFragment)
        }
        binding.recyclerView.adapter = StockAdapter(ItemsManager.items, itemClickCallback)


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