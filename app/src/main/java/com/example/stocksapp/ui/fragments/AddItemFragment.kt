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
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.setFragmentResultListener
import com.example.stocksapp.R
import com.example.stocksapp.data.model.Item
import com.example.stocksapp.ui.classes.MainFragmentViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddItemFragment : Fragment() {

    private var _binding: ItemStockAddFragmentBinding? = null
    private val binding get() = _binding!!

    private var imageUri : Uri? = null

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


//            val value = binding.stockAmount.text.toString().toFloat() * binding.stockPrice.text.toString().toFloat()
            val value = binding.stockPrice.text.toString()
            // name should set from api call
            val name = "stock name"
            val item = Item(
                stockName = name,
                stockSymbol = binding.stockSymbol.text.toString(),
                stockPrice = value.toDouble(), // Assuming value is a String that can be converted to Double
                stockAmount = binding.stockAmount.text.toString().toLong(), // You need to provide a valid Long value for stockAmount
                stockImage = chosenImageUri.toString(),
                currPrice = value.toDouble() // Assuming value is a String that can be converted to Double
            )
//            ItemsManager.add(item)
            viewModel.addItem(item)
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