package com.example.stocksapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.stocksapp.databinding.InfoFragmentBinding

class InfoFragment : Fragment() {

    private var _binding: InfoFragmentBinding? = null
    private val binding get() = _binding!!

//    private val viewModel: MainFragmentViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = InfoFragmentBinding.inflate(inflater, container, false)

        binding.bottomNavigation.setOnItemSelectedListener { itemMenu ->

            when (itemMenu.itemId) {
                R.id.add -> {
                    findNavController().navigate(R.id.action_infoFragment_to_addItemFragment)
                    true
                }

                R.id.home -> {
                    findNavController().navigate(R.id.action_infoFragment_to_mainFragment)
                    true
                }

                R.id.total -> {
                    findNavController().navigate(R.id.action_infoFragment_to_totalFragment)
                    true
                }

                R.id.alerts -> {
                    true
                }

                else -> false
            }
        }

        return  binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}