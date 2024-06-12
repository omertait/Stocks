package com.example.stocksapp.ui.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import androidx.fragment.app.DialogFragment
import com.example.stocksapp.R
import com.example.stocksapp.databinding.GridItemBinding
import com.example.stocksapp.databinding.ImagesGridBinding

class ImageSelectionDialogFragment : DialogFragment() {

    private var _binding: ImagesGridBinding? = null
    private val binding get() = _binding!!

    private val imageIds = arrayOf(
        R.drawable.circle_green, R.drawable.circle_orange, R.drawable.circle_pink,
        R.drawable.circle_purple, R.drawable.circle_yellow, R.drawable.circle_tourqise
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = ImagesGridBinding.inflate(inflater, container, false)

        binding.gridView.adapter = ImageAdapter(requireContext(), imageIds)

        binding.gridView.setOnItemClickListener { _, _, position, _ ->
            val selectedImageId = imageIds[position]
            parentFragmentManager.setFragmentResult(
                "requestKey",
                Bundle().apply { putInt("selectedImageId", selectedImageId) }
            )
            dismiss()
        }

        return binding.root
    }

    class ImageAdapter(private val context: Context, private val imageIds: Array<Int>) :
        BaseAdapter() {
        override fun getCount(): Int {
            return imageIds.size
        }

        override fun getItem(position: Int): Any {
            return imageIds[position]
        }

        override fun getItemId(position: Int): Long {
            return position.toLong()
        }

        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
            val binding: GridItemBinding

            if (convertView == null) {
                binding = GridItemBinding.inflate(LayoutInflater.from(context), parent, false)
                binding.root.tag = binding
            } else {
                binding = convertView.tag as GridItemBinding
            }

            binding.imageView.setImageResource(imageIds[position])
            return binding.root
        }
    }

}


