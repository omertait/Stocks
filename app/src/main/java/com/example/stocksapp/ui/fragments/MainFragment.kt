package com.example.stocksapp.ui.fragments


import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.stocksapp.databinding.MainFragmentBinding
import androidx.recyclerview.widget.ItemTouchHelper
import com.bumptech.glide.Glide
import com.example.stocksapp.R
import com.example.stocksapp.data.remote_db.StockRemoteDataSource
import com.example.stocksapp.data.utils.Constants
import com.example.stocksapp.data.utils.Error
import com.example.stocksapp.data.utils.Success
import com.example.stocksapp.ui.classes.ItemAdapter
import com.example.stocksapp.ui.classes.MainFragmentViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.math.abs

@AndroidEntryPoint
class MainFragment : Fragment() {

    private var _binding: MainFragmentBinding? = null
    private val binding get() = _binding!!

    private val viewModel: MainFragmentViewModel by activityViewModels()

    @Inject
    lateinit var stockRemoteDataSource: StockRemoteDataSource

    private lateinit var adapter: ItemAdapter
    private val handler = Handler(Looper.getMainLooper())
    private val updateTimeRunnable = object : Runnable {
        override fun run() {
            adapter.notifyDataSetChanged()
            handler.postDelayed(this, 60000) // Update every minute
        }
    }

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

            adapter = ItemAdapter(it, object : ItemAdapter.ItemListener {
                override fun onItemClicked(index: Int) {
                    viewModel.setItem(it[index])
                    findNavController().navigate(R.id.action_mainFragment_to_itemDetailFragment)
                }

                override fun onItemAttrClicked(index: Int) {
                    // make api call for updating stats
                    // ************* SHOULD BE UPDATED INTO LOCAL DB ***************
                    // ************* ALSO UPDATE UI ***************
                    val stockSymbol = it[index].stockSymbol
                    val token = Constants.API_KEY
                    Log.d("PRICES", "Initiating API request")
                    lifecycleScope.launch(Dispatchers.IO) {
                        try {
                            val response = stockRemoteDataSource.getQuote(stockSymbol, token)

                            if (response.status is Success) {

                                val currPrice = response.status.data?.c
                                Log.d("PRICES", "currPrice, $stockSymbol: $$currPrice")

                                withContext(Dispatchers.Main) {
                                    if (currPrice != null) {
                                        it[index].currPrice = currPrice
                                        it[index].lastUpdateDate = System.currentTimeMillis()

                                        viewModel.updateItem(it[index])
                                    }
                                    Toast.makeText(context, "Stats Updated", Toast.LENGTH_LONG).show()

                                }
                            } else if (response.status is Error) {
                                val errorMessage = response.status.message
                                it[index].currPrice = 0.0
                                it[index].lastUpdateDate = System.currentTimeMillis()


                                viewModel.updateItem(it[index])
                                Log.d("PRICES", "API status = Error $errorMessage")
                                Toast.makeText(context, "Error $errorMessage", Toast.LENGTH_LONG).show()

                            }
                        } catch (e: Exception) {
                            Log.d("PRICES", "Exception in API request")
                        }
                    }
                    Log.d("api", "end api call")
                }

            })

            binding.recyclerView.adapter = adapter

            // Start the handler to update time differences
            handler.post(updateTimeRunnable)
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
                val item = (binding.recyclerView.adapter as ItemAdapter).itemAt(viewHolder.adapterPosition)
                viewModel.deleteItem(item)
//                binding.recyclerView.adapter!!.notifyItemRemoved(viewHolder.adapterPosition)
            }
        }).attachToRecyclerView(binding.recyclerView)
        // handle db calls
    }

    override fun onDestroyView() {
        super.onDestroyView()
        handler.removeCallbacks(updateTimeRunnable) // Stop the handler when the view is destroyed
        _binding = null
    }
}