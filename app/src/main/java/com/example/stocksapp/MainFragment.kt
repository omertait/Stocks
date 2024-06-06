package com.example.stocksapp

import ItemsManager
import Stock
import StockAdapter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.stocksapp.databinding.MainFragmentBinding
import androidx.recyclerview.widget.ItemTouchHelper
import com.example.stocksapp.data.model.Item


class MainFragment : Fragment() {

    private var _binding: MainFragmentBinding? = null
    private val binding get() = _binding!!

    private val viewModel: MainFragmentViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = MainFragmentBinding.inflate(inflater, container, false)

        binding.bottomNavigation.setOnItemSelectedListener { itemMenu ->

            when (itemMenu.itemId) {
                R.id.add -> {
                    findNavController().navigate(R.id.action_mainFragment_to_addItemFragment)
                    true
                }

                R.id.home -> {

                    true
                }

                R.id.total -> {
                    findNavController().navigate(R.id.action_mainFragment_to_totalFragment)
                    true
                }

                R.id.alerts -> {
                    findNavController().navigate(R.id.action_mainFragment_to_infoFragment)
                    true
                }

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

        viewModel.items?.observe(viewLifecycleOwner) {
            val itemClickCallback: (Item) -> Unit = {item ->
                viewModel.setItem(item)
                findNavController().navigate(R.id.action_mainFragment_to_itemDetailFragment)
            }

            val deleteClickCallback: (Item) -> Unit = { item ->
                viewModel.deleteItem(item)
            }

            binding.recyclerView.adapter = StockAdapter(it, itemClickCallback, deleteClickCallback)
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
                val item = (binding.recyclerView.adapter as StockAdapter).itemAt(viewHolder.adapterPosition)
                viewModel.deleteItem(item)
//                binding.recyclerView.adapter!!.notifyItemRemoved(viewHolder.adapterPosition)
            }
        }).attachToRecyclerView(binding.recyclerView)
        // handle db calls
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}