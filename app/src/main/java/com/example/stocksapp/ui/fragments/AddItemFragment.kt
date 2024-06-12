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
import java.nio.channels.NonReadableChannelException
import javax.inject.Inject

@AndroidEntryPoint
class AddItemFragment : Fragment() {

    private var _binding: ItemStockAddFragmentBinding? = null
    private val binding get() = _binding!!

    private var imageUri : Uri? = null

    @Inject
    lateinit var stockRemoteDataSource: StockRemoteDataSource

    private val pickLauncher: ActivityResultLauncher<Array<String>> =
        registerForActivityResult(ActivityResultContracts.OpenDocument()) {
            if (it != null) {
                binding.previewImage.setImageURI(it)
                requireActivity().contentResolver.takePersistableUriPermission(
                    it!!,
                    Intent.FLAG_GRANT_READ_URI_PERMISSION
                )
                imageUri = it
            } else {
                Toast.makeText(requireContext(), "select image", Toast.LENGTH_SHORT).show()
            }
        }



    private val viewModel : MainFragmentViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = ItemStockAddFragmentBinding.inflate(inflater, container, false)

        binding.imageBtn.setOnClickListener {
            pickLauncher.launch(arrayOf("image/*"))
        }
        binding.previewImage.setOnClickListener {
            ImageSelectionDialogFragment().show(parentFragmentManager, "imageSelection")
        }

        setFragmentResultListener("requestKey") { _, bundle ->
            val selectedImageId = bundle.getInt("selectedImageId")
            imageUri = Uri.parse(
                ContentResolver.SCHEME_ANDROID_RESOURCE + "://" +
                        resources.getResourcePackageName(selectedImageId) + "/" +
                        resources.getResourceTypeName(selectedImageId) + "/" +
                        resources.getResourceEntryName(selectedImageId)
            )
            binding.previewImage.setImageURI(imageUri)
        }




        binding.addBtn.setOnClickListener {

            // set the chosen image uri
            val currentImageUri = imageUri
            val chosenImageUri: Uri = if (currentImageUri != null) {
                currentImageUri
            } else {
                val resourceId = R.drawable.default_image
                Uri.parse(
                    ContentResolver.SCHEME_ANDROID_RESOURCE + "://" +
                            resources.getResourcePackageName(resourceId) + "/" +
                            resources.getResourceTypeName(resourceId) + "/" +
                            resources.getResourceEntryName(resourceId)
                )
            }


            val token = Constants.API_KEY
            val stockSymbol = binding.stockSymbol.text.toString()
            val stockName = binding.stockName.text.toString()
            val stockAmount = binding.stockAmount.text.toString().toLong()
            val stockPrice = binding.stockPrice.text.toString()
            var currPrice = 0.0
            var openingPrice = 0.0

            Log.d("PRICES", "Initiating API request")
            lifecycleScope.launch(Dispatchers.IO) {
                try {
                    val response = stockRemoteDataSource.getQuote(stockSymbol, token)

                    if (response.status is Success) {

                        val currPriceResponse = response.status.data?.c
                        val openingPriceResponse = response.status.data?.o

                        Log.d("PRICES", "currPrice, $stockSymbol: $$currPriceResponse")
                        Log.d("PRICES", "openingPrice, $stockSymbol: $$openingPriceResponse")

                        withContext(Dispatchers.Main) {
                            if (currPriceResponse != null && openingPriceResponse != null) {

                                currPrice = currPriceResponse
                                openingPrice = openingPriceResponse

                            }else{
                                Toast.makeText(context, "Error Getting stock price from api", Toast.LENGTH_LONG).show()

                            }



                        }
                    } else if (response.status is Error) {
                        val errorMessage = response.status.message

                        Log.d("PRICES", "API status = Error $errorMessage")
                        Toast.makeText(context, "Error $errorMessage", Toast.LENGTH_LONG).show()

                    }
                } catch (e: Exception) {
                    Log.d("PRICES", "Exception in API request")
                }

                val item = Item(
                    stockName = stockName,
                    stockSymbol = stockSymbol,
                    stockPrice = stockPrice.toDouble(), // Assuming value is a String that can be converted to Double
                    stockAmount = stockAmount, // You need to provide a valid Long value for stockAmount
                    stockImage = chosenImageUri.toString(),
                    currPrice = currPrice,
                    openingPrice = openingPrice,
                    lastUpdateDate = System.currentTimeMillis()
                )

                viewModel.addItem(item)
                withContext(Dispatchers.Main) {
                    findNavController().navigate(R.id.action_addItemFragment_to_mainFragment)
                }

            }







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