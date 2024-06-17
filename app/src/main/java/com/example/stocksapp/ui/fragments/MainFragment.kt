package com.example.stocksapp.ui.fragments

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import android.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.ItemTouchHelper
import com.example.stocksapp.databinding.MainFragmentBinding
import com.example.stocksapp.R
import com.example.stocksapp.data.remote_db.StockRemoteDataSource
import com.example.stocksapp.data.utils.Constants
import com.example.stocksapp.data.utils.Error
import com.example.stocksapp.data.utils.LocationCallback
import com.example.stocksapp.data.utils.Success
import com.example.stocksapp.data.utils.getLastLocation
import com.example.stocksapp.ui.classes.ItemAdapter
import com.example.stocksapp.ui.classes.MainFragmentViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@AndroidEntryPoint
class MainFragment : Fragment(), LocationCallback {

    private var _binding: MainFragmentBinding? = null
    private val binding get() = _binding!!

    private val viewModel: MainFragmentViewModel by activityViewModels()

    @Inject
    lateinit var stockRemoteDataSource: StockRemoteDataSource

    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private var location: String? = null

    private lateinit var adapter: ItemAdapter
    private val handler = Handler(Looper.getMainLooper())

    // Runnable to update the adapter every minute
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

        // Initialize the FusedLocationProviderClient
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())

        getLastLocation(this, requireContext(), fusedLocationClient, this)

        // Handle bottom navigation item selection
        binding.bottomNavigation.setOnItemSelectedListener { itemMenu ->
            when (itemMenu.itemId) {
                R.id.add -> {
                    findNavController().navigate(R.id.action_mainFragment_to_addItemFragment)
                    true
                }
                R.id.home -> {
                    // Already on the home screen
                    true
                }
                R.id.total -> {
                    findNavController().navigate(R.id.action_mainFragment_to_totalFragment)
                    true
                }
                R.id.hotStocks -> {
                    findNavController().navigate(R.id.action_mainFragment_to_infoFragment)
                    true
                }
                else -> false
            }
        }

        return binding.root
    }

    override fun onLocationRetrieved(country: String) {
        Log.d("location", country)
        location = country

        // set location in RecyclerView after location is retrieved
        (binding.recyclerView.adapter as? ItemAdapter)?.setLocation(location ?: "Unknown")

    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Set the home menu item as checked when the view is created
        val checkedMenuItem = binding.bottomNavigation.menu.findItem(R.id.home)
        checkedMenuItem.setChecked(true)

        // Set up RecyclerView with LinearLayoutManager
        binding.recyclerView.layoutManager = LinearLayoutManager(requireActivity())

        // Observe items LiveData from the ViewModel
        viewModel.items?.observe(viewLifecycleOwner) { items ->
            // Initialize the adapter with the list of items and an ItemListener
            adapter = ItemAdapter(items, object : ItemAdapter.ItemListener {
                override fun onItemClicked(index: Int) {
                    // Navigate to item detail fragment when an item is clicked
                    viewModel.setItem(items[index])
                    findNavController().navigate(R.id.action_mainFragment_to_itemDetailFragment)
                }

                override fun onItemAttrClicked(index: Int) {
                    // Make API call to update stock statistics
                    val stockSymbol = items[index].stockSymbol
                    val token = Constants.API_KEY
                    Log.d("PRICES", "Initiating API request")
                    lifecycleScope.launch(Dispatchers.IO) {
                        try {
                            val response = stockRemoteDataSource.getQuote(stockSymbol, token)

                            if (response.status is Success) {
                                val currPrice = response.status.data?.c
                                val openingPrice = response.status.data?.o
                                Log.d("PRICES", "currPrice, $stockSymbol: $$currPrice")
                                Log.d("PRICES", "openingPrice, $stockSymbol: $$openingPrice")

                                withContext(Dispatchers.Main) {
                                    if (currPrice != null && openingPrice != null) {
                                        // Update item properties
                                        items[index].currPrice = currPrice
                                        items[index].currPrice = currPrice
                                        items[index].openingPrice = openingPrice
                                        items[index].lastUpdateDate = System.currentTimeMillis()

                                        // Update item in ViewModel
                                        viewModel.updateItem(items[index])

                                        // Show toast message
                                        Toast.makeText(context, "Stats Updated", Toast.LENGTH_LONG).show()
                                    } else {
                                        // Show error toast message
                                        Toast.makeText(context, "Error getting data from API", Toast.LENGTH_LONG).show()
                                    }
                                }
                            } else if (response.status is Error) {
                                // Handle API error
                                val errorMessage = response.status.message
                                Log.d("PRICES", "API status = Error $errorMessage")
                                Toast.makeText(context, "Error $errorMessage", Toast.LENGTH_LONG).show()
                            }
                        } catch (e: Exception) {
                            // Handle exception
                            Log.d("PRICES", "Exception in API request: ${e.message}")
                        }
                    }
                    Log.d("api", "End API call")
                }
            }, location?: "Unknown")


            // Set the adapter to the RecyclerView
            binding.recyclerView.adapter = adapter

            // Start the handler to update time differences
            handler.post(updateTimeRunnable)
        }

        // Set up swipe to delete functionality using ItemTouchHelper
        ItemTouchHelper(object : ItemTouchHelper.Callback() {
            override fun getMovementFlags(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder
            ) = makeFlag(ItemTouchHelper.ACTION_STATE_SWIPE, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT)

            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val item = (binding.recyclerView.adapter as ItemAdapter).itemAt(viewHolder.adapterPosition)
                val deleteDialog: AlertDialog.Builder = AlertDialog.Builder(context)
                deleteDialog.apply {
                    setTitle(R.string.deletion_confirmation_title)
                    setMessage(R.string.deletion_confirmation)
                    setCancelable(false)
                    setIcon(R.drawable.delete)
                    setPositiveButton(R.string.deletion_confirmation_yes) { _, _ ->
                        viewModel.deleteItem(item)
                        binding.recyclerView.adapter!!.notifyItemRemoved(viewHolder.adapterPosition)
                    }
                    setNegativeButton(R.string.deletion_confirmation_no) { _, _ ->
                        binding.recyclerView.adapter!!.notifyItemChanged(viewHolder.adapterPosition)
                    }
                }
                deleteDialog.create().show()

            }
        }).attachToRecyclerView(binding.recyclerView)



    }

    override fun onDestroyView() {
        super.onDestroyView()
        handler.removeCallbacks(updateTimeRunnable) // Stop the handler when the view is destroyed
        _binding = null
    }
}
