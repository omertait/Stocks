package com.example.stocksapp.ui.fragments

import android.content.ContentResolver
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.stocksapp.databinding.ItemStockAddFragmentBinding
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.setFragmentResultListener
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.stocksapp.R
import com.example.stocksapp.data.model.Item
import com.example.stocksapp.data.remote_db.StockRemoteDataSource
import com.example.stocksapp.data.utils.Constants
import com.example.stocksapp.data.utils.Error
import com.example.stocksapp.data.utils.Success
import com.example.stocksapp.ui.classes.MainFragmentViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * A Fragment for adding new items.
 */
@AndroidEntryPoint
class AddItemFragment : Fragment() {

    private var _binding: ItemStockAddFragmentBinding? = null
    private val binding get() = _binding!!

    private var imageUri : Uri? = null

    private lateinit var defaultImageUri : Uri

    @Inject
    lateinit var stockRemoteDataSource: StockRemoteDataSource

    // Activity result launcher for picking an image
    private val pickLauncher: ActivityResultLauncher<Array<String>> =
        registerForActivityResult(ActivityResultContracts.OpenDocument()) {
            if (it != null) {
                binding.previewImage.setImageURI(it)
                requireActivity().contentResolver.takePersistableUriPermission(
                    it,
                    Intent.FLAG_GRANT_READ_URI_PERMISSION
                )
                imageUri = it
            } else {
                Toast.makeText(requireContext(), "Please select an image", Toast.LENGTH_SHORT).show()
            }
        }

    // View model shared with the parent activity's scope
    private val viewModel: MainFragmentViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = ItemStockAddFragmentBinding.inflate(inflater, container, false)

        // Click listener for picking an image
        binding.imageBtn.setOnClickListener {
            pickLauncher.launch(arrayOf("image/*"))
        }

        // Click listener for preview image
        binding.previewImage.setOnClickListener {
            ImageSelectionDialogFragment().show(parentFragmentManager, "imageSelection")
        }

        val resourceId = R.drawable.default_image
        defaultImageUri = Uri.parse(
            ContentResolver.SCHEME_ANDROID_RESOURCE + "://" +
                    resources.getResourcePackageName(resourceId) + "/" +
                    resources.getResourceTypeName(resourceId) + "/" +
                    resources.getResourceEntryName(resourceId)
        )

        // set the preview image to be the deafult at first
        Glide.with(binding.root)
            .load(defaultImageUri)
            .circleCrop()
            .into(binding.previewImage)

        // Fragment result listener for image selection
        setFragmentResultListener("requestKey") { _, bundle ->
            val selectedImageId = bundle.getInt("selectedImageId")
            imageUri = Uri.parse(
                ContentResolver.SCHEME_ANDROID_RESOURCE + "://" +
                        resources.getResourcePackageName(selectedImageId) + "/" +
                        resources.getResourceTypeName(selectedImageId) + "/" +
                        resources.getResourceEntryName(selectedImageId)
            )
            // Load the image into ImageView using Glide
            Glide.with(binding.root)
                .load(imageUri)
                .circleCrop()
                .into(binding.previewImage)
        }

        // Click listener for add button
        binding.addBtn.setOnClickListener {

            // Get input values
            val token = Constants.API_KEY
            val stockSymbol = binding.stockSymbol.text.toString()
            val stockName = binding.stockName.text.toString()
            val stockAmount = binding.stockAmount.text.toString()
            val stockPrice = binding.stockPrice.text.toString()
            var currPrice = 0.0
            var openingPrice = 0.0
            var isValidSymbol = true


            // Check if any field is empty
            if (stockSymbol.isEmpty() || stockName.isEmpty() || stockAmount.isEmpty() || stockPrice.isEmpty()) {
                Toast.makeText(requireContext(), R.string.Fill_All_Fields, Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            // Set the chosen image URI
            val chosenImageUri: Uri = imageUri ?: defaultImageUri


            // Log for API request initiation
            Log.d("PRICES", "Initiating API request")

            // Coroutine to handle API request
            lifecycleScope.launch(Dispatchers.IO) {
                try {
                    val response = stockRemoteDataSource.getQuote(stockSymbol, token)

                    // Handle API response
                    if (response.status is Success) {
                        val currPriceResponse = response.status.data?.c
                        val openingPriceResponse = response.status.data?.o

                        if (currPriceResponse != null && openingPriceResponse != null) {
                            currPrice = currPriceResponse
                            openingPrice = openingPriceResponse
                        } else {
                            Toast.makeText(
                                requireContext(),
                                R.string.Error_Toast_API,
                                Toast.LENGTH_LONG
                            ).show()
                        }

                    } else if (response.status is Error) {
                        val errorMessage = response.status.message

                        // Log API error message
                        Log.d("PRICES", "API status = Error $errorMessage")
                        Toast.makeText(
                            requireContext(),
                            R.string.Error_Toast_API,
                            Toast.LENGTH_LONG
                        ).show()
                    }
                } catch (e: Exception) {
                    // Log exception in API request
                    Log.d("PRICES", "Exception in API request")
                }

                if(currPrice == 0.0 && openingPrice == 0.0) {
                    isValidSymbol = false
                } else {
                    // Create Item object to add to database
                    val item = Item(
                        stockName = stockName,
                        stockSymbol = stockSymbol,
                        stockPrice = stockPrice.toDouble(),
                        stockAmount = stockAmount.toLong(),
                        stockImage = chosenImageUri.toString(),
                        currPrice = currPrice,
                        openingPrice = openingPrice,
                        lastUpdateDate = System.currentTimeMillis()
                    )

                    // Add item via ViewModel
                    viewModel.addItem(item)
                }
                    // Navigate back to mainFragment
                    withContext(Dispatchers.Main) {
                        if (isValidSymbol){
                            findNavController().navigate(R.id.action_addItemFragment_to_mainFragment)

                        } else{
                            Toast.makeText(requireContext(), R.string.Wrong_Symbol_Input, Toast.LENGTH_LONG)
                                .show()
                        }

                    }


            }
        }

        // Bottom navigation item selection listener
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

                R.id.hotStocks -> {
                    findNavController().navigate(R.id.action_addItemFragment_to_infoFragment)
                    true
                }

                else -> false
            }
        }


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Set checked menu item in bottom navigation
        val checkedMenuItem = binding.bottomNavigation.menu.findItem(R.id.add)
        checkedMenuItem.setChecked(true)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}