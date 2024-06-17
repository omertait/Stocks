package com.example.stocksapp.ui.fragments

import android.content.ContentResolver
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.setFragmentResultListener
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.stocksapp.databinding.ItemStockEditFragmentBinding
import com.example.stocksapp.ui.classes.MainFragmentViewModel

/**
 * Fragment for editing an existing item.
 */
class EditItemFragment : Fragment() {

    private var _binding: ItemStockEditFragmentBinding? = null
    private val binding get() = _binding!!

    // Shared ViewModel between fragments
    private val viewModel: MainFragmentViewModel by activityViewModels()

    // URI for the selected image
    private var imageUri : Uri? = null

    // Activity result launcher for image picking
    private val pickLauncher: ActivityResultLauncher<Array<String>> =
        registerForActivityResult(ActivityResultContracts.OpenDocument()) { uri ->
            if (uri != null) {
                binding.previewImage.setImageURI(uri)
                requireActivity().contentResolver.takePersistableUriPermission(
                    uri,
                    Intent.FLAG_GRANT_READ_URI_PERMISSION
                )
                imageUri = uri
            } else {
                Toast.makeText(requireContext(), "Please select an image", Toast.LENGTH_SHORT).show()
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = ItemStockEditFragmentBinding.inflate(inflater, container, false)

        // Click listener for image button to launch image picker
        binding.imageBtn.setOnClickListener {
            pickLauncher.launch(arrayOf("image/*"))
        }

        // Click listener for preview image to show image selection dialog
        binding.previewImage.setOnClickListener {
            ImageSelectionDialogFragment().show(parentFragmentManager, "imageSelection")
        }

        // Fragment result listener to get the selected image from ImageSelectionDialogFragment
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

        // Click listener for save button
        binding.saveBtn.setOnClickListener {
            // Retrieve the existing item from the ViewModel
            val existingItem = viewModel.chosenItem.value ?: return@setOnClickListener

            // Update only the non-null and non-blank fields
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
            imageUri?.let {
                existingItem.stockImage = it.toString()
            }

            // Update the item in the database via ViewModel
            viewModel.updateItem(existingItem)

            // Navigate back to previous fragment
            findNavController().navigateUp()
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Observe the chosenItem LiveData to update UI with item details
        viewModel.chosenItem.observe(viewLifecycleOwner) { item ->
            binding.stockName.setText(item.stockName)
            binding.stockSymbol.setText(item.stockSymbol)
            binding.stockAmount.setText(item.stockAmount.toString())
            binding.stockPrice.setText(item.stockPrice.toString())

            // Load the image into ImageView using Glide
            Glide.with(binding.root)
                .load(item.stockImage)
                .circleCrop()
                .into(binding.previewImage)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
